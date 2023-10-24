package com.example.projekatv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DBUtils {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/mydb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
  //  "jdbc:mysql://localhost:3306/mydb", "root", ""
    public static String user = null;

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            Parent root = loader.load();

            if (username != null) {
                LoggedInAdminController loggedInAdminController = loader.getController();
                loggedInAdminController.setUserInformation(username);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void changeSceneVolounteer(ActionEvent event, String fxmlFile, String title, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            Parent root = loader.load();

            if (username != null) {
                VolounteerMenuController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CallableStatement cstmt = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT lozinka FROM zaposleni WHERE KorisnickoIme = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
             //   System.out.println("User not found");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Pogresni podaci");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("lozinka");
                    if (retrievedPassword.equals(password)) {
                        /////////////////////////
                        String procedureCall = "{ CALL CheckUserRole(?, ?) }";
                        cstmt = connection.prepareCall(procedureCall);
                        cstmt.setString(1, username);
                        cstmt.registerOutParameter(2, Types.VARCHAR);
                        cstmt.execute();
                        String role = cstmt.getString(2);
                   //     System.out.println("Role: " + role);
                        cstmt.close();
                        ///////////////////////
                        if ("admin".equals(role)) {
                            user = username;
                            changeScene(event, "loggedIn_admin.fxml", "Welcome", username);
                        } else if ("volonter".equals(role)) {
                            user = username;
                            changeSceneVolounteer(event, "volounteer_menu.fxml", null, username);
                        }
                    } else {
                   //     System.out.println("Wrong user password");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Pogresni podaci");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ObservableList<String> getCampNames(String table) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ObservableList<String> campList = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            statement = connection.createStatement();
            String query = "SELECT Ime FROM " + table;
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String campName = resultSet.getString("Ime");
                campList.add(campName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return campList;
    }

    public static ObservableList<String> getCampDetails(String campName) {
        ObservableList<String> detailsList = FXCollections.observableArrayList();
        CallableStatement cstmt = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            String query = "SELECT * FROM KAMP WHERE Ime = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, campName);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int campId = resultSet.getInt("idKamp");
                int placeId = resultSet.getInt("Mjesto_idMjesto");
                int administratorId = resultSet.getInt("Administrator_Zaposleni_Osoba_JMB");
                int statusId = resultSet.getInt("STATUS_KAMPA_idSTATUS_KAMPA");
                int numberOfPeople = resultSet.getInt("KorisnikKampaCount");

                String procedureCall = "{ CALL GetPlaceName(?, ?) }";
                cstmt = connection.prepareCall(procedureCall);
                cstmt.setInt(1, placeId);
                cstmt.registerOutParameter(2, Types.VARCHAR);
                cstmt.execute();
                String placeName = cstmt.getString(2);
                cstmt.close();

                String procedureCall1 = "{ CALL GetAdminName(?, ?) }";
                cstmt = connection.prepareCall(procedureCall1);
                cstmt.setInt(1, administratorId);
                cstmt.registerOutParameter(2, Types.VARCHAR);
                cstmt.execute();
                String administratorName = cstmt.getString(2);
                cstmt.close();

                String procedureCall2 = "{ CALL get_camp_status(?, ?) }";
                cstmt = connection.prepareCall(procedureCall2);
                cstmt.setInt(1, statusId);
                cstmt.registerOutParameter(2, Types.VARCHAR);
                cstmt.execute();
                String status = cstmt.getString(2);
                cstmt.close();


                detailsList.add(campName);
                detailsList.add(placeName);
                detailsList.add(administratorName);
                detailsList.add(status);
                detailsList.add(Integer.toString(numberOfPeople));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return detailsList;
    }

    public static List<String> getMjesto() {

        List<String> listMjesta = new ArrayList<String>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT ImeMjesta FROM Mjesto");
            while (resultSet.next()) {
                String campName = resultSet.getString("ImeMjesta");
                listMjesta.add(campName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listMjesta;


    }

    public static void createCamp(String campName, int kampStatus, String place) {
        CallableStatement cstmt = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);

            String procedureCall = "{ CALL getMjestoId(?, ?) }";
            cstmt = connection.prepareCall(procedureCall);
            cstmt.setString(1, place);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();
            int place1 = cstmt.getInt(2);
            cstmt.close();

            String procedureCall2 = "{ CALL GetAdminIdFromUsername(?, ?) }";
            cstmt = connection.prepareCall(procedureCall2);
            cstmt.setString(1, user);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();
            int adminID = cstmt.getInt(2);
            cstmt.close();

            String query = "INSERT INTO `KAMP` (`Mjesto_idMjesto`, `Administrator_Zaposleni_Osoba_JMB`, `Ime`, `STATUS_KAMPA_idSTATUS_KAMPA`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, place1);
            preparedStatement.setInt(2, adminID);
            preparedStatement.setString(3, campName);
            preparedStatement.setInt(4, kampStatus);
            // Execute the statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully.");
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Uspjesno");
                a.show();
            } else {
                System.out.println("Data insertion failed.");
            }
        //    System.out.println(adminID + " " + place1 + " " + kampStatus + " " + campName);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void deleteCamp(String camp) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            String query = "DELETE FROM `KAMP` WHERE `Ime` = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, camp);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record deleted successfully.");
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Uspjesno");
                a.show();
            } else {
                System.out.println("No record found with the given CampName.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void updateCamp(String campName, int kampStatus, String place) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);

            String procedureCall = "{ CALL getMjestoId(?, ?) }";
            CallableStatement cstmt = connection.prepareCall(procedureCall);
            cstmt.setString(1, place);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();
            int placeId = cstmt.getInt(2);
            cstmt.close();

            String query = "UPDATE `KAMP` SET `Mjesto_idMjesto` = ?, `STATUS_KAMPA_idSTATUS_KAMPA` = ? WHERE `Ime` = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, placeId);
            preparedStatement.setInt(2, kampStatus);
            preparedStatement.setString(3, campName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Camp updated successfully.");
                Alert a=new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Uspjesno");
                a.show();
            } else {
                System.out.println("Failed to update camp.");
            }
            //  System.out.println(adminID + " " + placeId + " " + kampStatus + " " + campName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ObservableList<String> getVolunteerNames() {

        ObservableList<String> volunteerNames = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // Establish database connection
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);

            // Create SQL query
            String query = "SELECT CONCAT(o.Ime, ' ', o.Prezime) AS VolunteerName " +
                    "FROM VOLONTER v " +
                    "INNER JOIN ZAPOSLENI z ON v.Zaposleni_Osoba_JMB = z.Osoba_JMB " +
                    "INNER JOIN OSOBA o ON z.Osoba_JMB = o.JMB";

            // Execute the query
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Get the volunteer names
            while (resultSet.next()) {
                String volunteerName = resultSet.getString("VolunteerName");
                volunteerNames.add(volunteerName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return volunteerNames;
        }
    }

    public static ObservableList<String> getVolonteerDetails(String volunteerName) {
        ObservableList<String> detailsList = FXCollections.observableArrayList();
        //volunteerName=volunteerName.split(" ", 2)[0];
        CallableStatement cstmt = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT o.Ime, o.Prezime, o.JMB, o.MjestoPrebivalista, k.Ime AS Kamp, va.StartDate, va.EndDate " +
                "FROM VOLONTER v " +
                "JOIN ZAPOSLENI z ON v.Zaposleni_Osoba_JMB = z.Osoba_JMB " +
                "JOIN OSOBA o ON z.Osoba_JMB = o.JMB " +
                "left JOIN VOLONTERSKI_ANGAZMAN va ON v.Zaposleni_Osoba_JMB = va.VOLONTER_Zaposleni_Osoba_JMB " +
                "left JOIN KAMP k ON va.KAMP_idKamp = k.idKamp " +
                "WHERE o.Ime='" + volunteerName + "';";
        try {

            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                detailsList.add(resultSet.getString("Ime"));
                detailsList.add(resultSet.getString("Prezime"));
                detailsList.add(resultSet.getString("JMB"));
                detailsList.add(resultSet.getString("MjestoPrebivalista"));
                detailsList.add(resultSet.getString("Kamp"));
                detailsList.add(resultSet.getString("StartDate"));
                detailsList.add(resultSet.getString("EndDate"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
      //  System.out.println(detailsList);
        return detailsList;
    }

    public static void updateVolounteerAssignment(LocalDate to, LocalDate from, String camp, String volounteer) {
        Connection connection = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement insertStatement = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);

            // Delete existing data
            String deleteQuery = "DELETE FROM VOLONTERSKI_ANGAZMAN " +
                    "WHERE VOLONTER_Zaposleni_Osoba_JMB IN " +
                    "(SELECT Zaposleni_Osoba_JMB FROM VOLONTER WHERE Zaposleni_Osoba_JMB IN (SELECT JMB FROM OSOBA WHERE Ime = ?))";
            deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, volounteer);
            //  deleteStatement.setString(2, camp);
            int rowsDeleted = deleteStatement.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " rows.");

            // Insert new data
            String insertQuery = "INSERT INTO volonterski_angazman (StartDate, EndDate, VOLONTER_Zaposleni_Osoba_JMB, KAMP_idKamp) " +
                    "VALUES (?, ?, (SELECT v.Zaposleni_Osoba_JMB FROM volonter v " +
                    "JOIN zaposleni z ON v.Zaposleni_Osoba_JMB = z.Osoba_JMB " +
                    "JOIN osoba o ON z.Osoba_JMB = o.JMB WHERE o.Ime = ?), " +
                    "(SELECT idKamp FROM kamp WHERE Ime = ?))";
            insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setDate(1, java.sql.Date.valueOf(to));
            insertStatement.setDate(2, java.sql.Date.valueOf(from));
            insertStatement.setString(3, volounteer);
            insertStatement.setString(4, camp);
            int rowsInserted = insertStatement.executeUpdate();
            System.out.println("Inserted " + rowsInserted + " rows.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (deleteStatement != null) {
                try {
                    deleteStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (insertStatement != null) {
                try {
                    insertStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static ObservableList<String> getVolonteerDetailsUsername(String username) {
        ObservableList<String> detailsList = FXCollections.observableArrayList();
        //volunteerName=volunteerName.split(" ", 2)[0];
        CallableStatement cstmt = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT o.Ime, o.Prezime, o.JMB, o.MjestoPrebivalista, k.Ime AS Kamp, va.StartDate, va.EndDate " +
                "FROM VOLONTER v " +
                "JOIN ZAPOSLENI z ON v.Zaposleni_Osoba_JMB = z.Osoba_JMB " +
                "JOIN OSOBA o ON z.Osoba_JMB = o.JMB " +
                "left JOIN VOLONTERSKI_ANGAZMAN va ON v.Zaposleni_Osoba_JMB = va.VOLONTER_Zaposleni_Osoba_JMB " +
                "left JOIN KAMP k ON va.KAMP_idKamp = k.idKamp " +
                "WHERE z.KorisnickoIme='" + username + "';";
        try {

            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                detailsList.add(resultSet.getString("Ime"));
                detailsList.add(resultSet.getString("Prezime"));
                detailsList.add(resultSet.getString("JMB"));
                detailsList.add(resultSet.getString("MjestoPrebivalista"));
                detailsList.add(resultSet.getString("Kamp"));
                detailsList.add(resultSet.getString("StartDate"));
                detailsList.add(resultSet.getString("EndDate"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
      //  System.out.println(detailsList);
        return detailsList;
    }

    public static void addPersonToCamp(int JMB, String ime, String prezime, LocalDate datumRodjenja, String pol, String drzavljanstvo, String mjestoPrebivalista, String tipNesrece,
                                       String status, LocalDate datumDolaska, LocalDate datumOdlaska, String campName, String username) {
        CallableStatement cstmt = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            String procedureCall = "{ CALL getCampId(?, ?) }";
            cstmt = connection.prepareCall(procedureCall);
            cstmt.setString(1, campName);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();
            int campId = cstmt.getInt(2);
            cstmt.close();

            String procedureCall2 = "{ CALL GetVolounteerIdFromUsername(?, ?) }";
            cstmt = connection.prepareCall(procedureCall2);
            cstmt.setString(1, username);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();
            int volounteerID = cstmt.getInt(2);
            cstmt.close();

            String insertOsobaQuery = "INSERT INTO OSOBA (JMB, Ime, Prezime, DatumRodjenja, Pol, Drzavljanstvo, MjestoPrebivalista) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertOsobaQuery);
            preparedStatement.setInt(1, JMB);
            preparedStatement.setString(2, ime);
            preparedStatement.setString(3, prezime);
            preparedStatement.setDate(4, java.sql.Date.valueOf(datumRodjenja));
            preparedStatement.setString(5, pol);
            preparedStatement.setString(6, drzavljanstvo);
            preparedStatement.setString(7, mjestoPrebivalista);
            preparedStatement.executeUpdate();

            String insertKorisnikKampaQuery = "INSERT INTO `KORISNIK KAMPA` (Osoba_JMB, TipNesreće, Status, VOLONTER_Zaposleni_Osoba_JMB) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertKorisnikKampaQuery);
            preparedStatement.setInt(1, JMB);
            preparedStatement.setString(2, tipNesrece);
            preparedStatement.setString(3, status);
            preparedStatement.setInt(4, volounteerID);
            preparedStatement.executeUpdate();

            // Insert into PERIOD BORAVKA table
            String insertPeriodBoravkaQuery = "INSERT INTO `PERIOD BORAVKA` (`Korisnik kampa_Osoba_JMB`, `Kamp_idKamp`, `DatumDolaska`, `DatumOdlaska`) " +
                    "VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertPeriodBoravkaQuery);
            preparedStatement.setInt(1, JMB);
            preparedStatement.setInt(2, campId);
            preparedStatement.setDate(3, java.sql.Date.valueOf(datumDolaska));
            preparedStatement.setDate(4, java.sql.Date.valueOf(datumOdlaska));
            preparedStatement.executeUpdate();

            System.out.println("Data inserted successfully.");
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Uspjesno");
            a.show();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static ObservableList<ObservableList<String>> getKorisnikKampaWithPeriodBoravka(String campName) {
        ObservableList<ObservableList<String>> result = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
            String query = "SELECT o.Ime,o.Prezime,o.JMB,Drzavljanstvo,DatumDolaska,DatumOdlaska,Status \n" +
                    "                    FROM `KORISNIK KAMPA` kk\n" +
                    "                    JOIN `OSOBA` o ON kk.`Osoba_JMB` = o.`JMB` \n" +
                    "                    JOIN `PERIOD BORAVKA` pb ON kk.`Osoba_JMB` = pb.`Korisnik kampa_Osoba_JMB` \n" +
                    "                    JOIN `KAMP` k ON pb.`Kamp_idKamp` = k.`idKamp` \n" +
                    "                    WHERE k.`Ime` = ?;\n";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, campName);
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create data rows
            while (resultSet.next()) {
                ObservableList<String> rowData = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(resultSet.getString(i));
                }
                result.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
      //  System.out.println(result);
        return result;
    }


}
