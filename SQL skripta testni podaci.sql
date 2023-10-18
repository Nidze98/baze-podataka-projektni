#ADMINISTRATOR 1
INSERT INTO `mydb`.`OSOBA` (`JMB`, `Ime`, `Prezime`, `DatumRodjenja`, `Pol`, `Drzavljanstvo`, `MjestoPrebivalista`)
VALUES (1, 'John', 'Doe', '2000-01-01', 'M', 'US', 'New York');

INSERT INTO `mydb`.`ZAPOSLENI` (`Osoba_JMB`, `KorisnickoIme`, `Lozinka`)
VALUES (1, 'admin', 'password');

INSERT INTO `mydb`.`ADMINISTRATOR` (`Zaposleni_Osoba_JMB`)
VALUES (1);

#ADMINISTRATOR 2
INSERT INTO `mydb`.`OSOBA` (`JMB`, `Ime`, `Prezime`, `DatumRodjenja`, `Pol`, `Drzavljanstvo`, `MjestoPrebivalista`)
VALUES (2, 'Jane', 'Smith', '1995-03-15', 'F', 'US', 'Los Angeles');

INSERT INTO `mydb`.`ZAPOSLENI` (`Osoba_JMB`, `KorisnickoIme`, `Lozinka`)
VALUES (2, 'a', 'a');

INSERT INTO `mydb`.`ADMINISTRATOR` (`Zaposleni_Osoba_JMB`)
VALUES (2);

#VOLONTER 1
INSERT INTO `mydb`.`OSOBA` (`JMB`, `Ime`, `Prezime`, `DatumRodjenja`, `Pol`, `Drzavljanstvo`, `MjestoPrebivalista`)
VALUES (3, 'Petar', 'Peric', '1990-01-01', 'M', 'SRB', 'Banja Luka');

INSERT INTO `mydb`.`ZAPOSLENI` (`Osoba_JMB`, `KorisnickoIme`, `Lozinka`)
VALUES (3, 'pero', 'lozinka');

INSERT INTO `mydb`.`VOLONTER` (`Zaposleni_Osoba_JMB`, `Status`)
VALUES (3, 'Active');

#VOLONTER 2
INSERT INTO `mydb`.`OSOBA` (`JMB`, `Ime`, `Prezime`, `DatumRodjenja`, `Pol`, `Drzavljanstvo`, `MjestoPrebivalista`)
VALUES (4, 'Darko', 'Daric', '1995-05-05', 'M', 'SRB', 'Belgrade');

INSERT INTO `mydb`.`ZAPOSLENI` (`Osoba_JMB`, `KorisnickoIme`, `Lozinka`)
VALUES (4, 'darko', 'sifra');

INSERT INTO `mydb`.`VOLONTER` (`Zaposleni_Osoba_JMB`, `Status`)
VALUES (4, 'Active');

#STATUS KAMPA
INSERT INTO `STATUS_KAMPA` (`idSTATUS_KAMPA`, `Status`)
VALUES (1, 'Aktivan'), (2, 'Neaktivan');

-- Inserting values into table MJESTO
INSERT INTO `MJESTO` (`idMjesto`, `ImeMjesta`, `Drzava`)
VALUES (1, 'Zagreb', 'Croatia'),
       (2, 'London', 'United Kingdom'),
       (3, 'Paris', 'France');

-- Inserting values into table KAMP
INSERT INTO `KAMP` (`idKamp`, `Mjesto_idMjesto`, `Administrator_Zaposleni_Osoba_JMB`, `Ime`, `STATUS_KAMPA_idSTATUS_KAMPA`)
VALUES (1, 1, 1, 'Camp 1', 1),
       (2, 2, 1, 'Camp 2', 2),
       (3, 3, 1, 'Camp 3', 1);