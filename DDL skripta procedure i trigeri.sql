DELIMITER //
CREATE PROCEDURE CheckUserRole(IN username VARCHAR(45), OUT userRole VARCHAR(45))
BEGIN
    DECLARE tmp VARCHAR(45);
    -- Check if the user is an admin
    SELECT 'admin' INTO tmp
    FROM ADMINISTRATOR a 
    JOIN ZAPOSLENI z ON a.Zaposleni_Osoba_JMB = z.Osoba_JMB
    WHERE z.KorisnickoIme = username;
    
    IF tmp IS NULL THEN
        -- Check if the user is a volunteer
        SELECT 'volonter' INTO tmp
        FROM VOLONTER v
        JOIN ZAPOSLENI z ON v.Zaposleni_Osoba_JMB = z.Osoba_JMB
        WHERE z.KorisnickoIme = username;
    END IF;
    
    SET userRole = tmp;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE get_camp_status(IN p_kamp_id INT, OUT p_camp_status VARCHAR(45))
BEGIN
    SELECT Status INTO p_camp_status
    FROM `mydb`.`STATUS_KAMPA`
    WHERE idSTATUS_KAMPA = (SELECT STATUS_KAMPA_idSTATUS_KAMPA FROM `mydb`.`KAMP` WHERE idKamp = p_kamp_id);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GetPlaceName(IN mjestoid INT, OUT ime VARCHAR(45))
BEGIN
  SELECT ImeMjesta INTO ime FROM MJESTO WHERE idMjesto = mjestoid;
END//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE GetAdminName(IN adminId INT, OUT adminName VARCHAR(45))
BEGIN
  SELECT Ime INTO adminName
  FROM OSOBA o
  INNER JOIN ZAPOSLENI z ON o.JMB = z.Osoba_JMB
  INNER JOIN ADMINISTRATOR a ON z.Osoba_JMB = a.Zaposleni_Osoba_JMB
  WHERE o.JMB = adminId;
END //
DELIMITER ;

DELIMITER //
CREATE procedure getMjestoId(IN mjesto VARCHAR(45), OUT id INT)
BEGIN
    SELECT idMjesto INTO id 
    FROM mjesto m
    where m.ImeMjesta = mjesto;
END//
DELIMITER ;

delimiter //
CREATE PROCEDURE GetAdminIdFromUsername(IN adminUsername VARCHAR(45), OUT adminId INT)
BEGIN
  SELECT z.Osoba_JMB INTO adminId
  FROM ZAPOSLENI z
  INNER JOIN ADMINISTRATOR a ON z.Osoba_JMB = a.Zaposleni_Osoba_JMB
  WHERE z.KorisnickoIme = adminUsername;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GetVolounteerIdFromUsername(IN volounteerUsername VARCHAR(45), OUT volounteerId INT)
BEGIN
  SELECT z.Osoba_JMB INTO volounteerId
  FROM ZAPOSLENI z
  INNER JOIN volonter v ON z.Osoba_JMB = v.Zaposleni_Osoba_JMB
  WHERE z.KorisnickoIme = volounteerUsername;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GetCampId(IN campName VARCHAR(45), OUT campId INT)
BEGIN
    SELECT idKamp INTO campId
    FROM `KAMP`
    WHERE Ime = campName;
END //
DELIMITER ;

ALTER TABLE `mydb`.`KAMP` ADD COLUMN `KorisnikKampaCount` INT DEFAULT 0;

DELIMITER //
CREATE TRIGGER update_broj_korisnika
AFTER INSERT ON `PERIOD BORAVKA`
FOR EACH ROW
BEGIN
    DECLARE BrojLjudiuKampu INT;

    SELECT COUNT(Osoba_JMB) INTO BrojLjudiuKampu
    FROM `mydb`.`KORISNIK KAMPA`
    INNER JOIN `mydb`.`PERIOD BORAVKA` ON `PERIOD BORAVKA`.`Korisnik kampa_Osoba_JMB` = `KORISNIK KAMPA`.`Osoba_JMB`
    WHERE Kamp_idKamp = NEW.Kamp_idKamp
    GROUP BY Kamp_idKamp;

    UPDATE `KAMP` k
    SET k.`KorisnikKampaCount` = BrojLjudiuKampu
    WHERE idKamp = NEW.Kamp_idKamp;
END //
DELIMITER ;
