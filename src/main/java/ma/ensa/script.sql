--MySQL
CREATE TABLE utilisateur (
                             id INT PRIMARY KEY AUTO_INCREMENT, -- Pour PostgreSQL, remplacer par SERIAL
                             nom VARCHAR(100) NOT NULL,
                             email VARCHAR(150) NOT NULL UNIQUE,
                             age INT NOT NULL
);
--PostgreSQL
CREATE TABLE utilisateur (
                             id SERIAL PRIMARY KEY,
                             nom VARCHAR(100) NOT NULL,
                             email VARCHAR(150) NOT NULL UNIQUE,
                             age INT NOT NULL
);
--SQLServer
CREATE TABLE utilisateur (
                             id INT IDENTITY(1,1) PRIMARY KEY,
                             nom NVARCHAR(100) NOT NULL,
                             email NVARCHAR(150) NOT NULL UNIQUE,
                             age INT NOT NULL
);
--oracle
CREATE TABLE utilisateur (
                             id NUMBER PRIMARY KEY,
                             nom VARCHAR2(100) NOT NULL,
                             email VARCHAR2(150) NOT NULL UNIQUE,
                             age INT NOT NULL
);

CREATE SEQUENCE utilisateur_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER utilisateur_bi
BEFORE INSERT ON utilisateur
FOR EACH ROW
BEGIN
SELECT utilisateur_seq.NEXTVAL INTO :NEW.id FROM dual;
END;


---insertinto
INSERT INTO utilisateur (nom, email, age) VALUES ('Fatima', 'fatima@mail.com', 25);
INSERT INTO utilisateur (nom, email, age) VALUES ('Ali', 'ali@mail.com', 23);
INSERT INTO utilisateur (nom, email, age) VALUES ('Youssef', 'youssef@mail.com', 27);
