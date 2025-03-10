-- -----------------------------------------------------
-- Schema opticyou
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS opticyou CASCADE;

-- -----------------------------------------------------
-- Schema opticyou
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS opticyou;

-- Establecemos el esquema en uso
SET search_path TO opticyou;

-- -----------------------------------------------------
-- Table "opticyou"."usuari"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.usuari;

CREATE TABLE IF NOT EXISTS opticyou.usuari (
  idusuari SERIAL PRIMARY KEY,
  nom VARCHAR(45),
  email VARCHAR(45) NOT NULL,
  contrasenya VARCHAR(45) NOT NULL,
  rol VARCHAR(11) CHECK (rol IN ('ADMIN', 'CLIENT','TREBALLADOR'))DEFAULT 'CLIENT'
);

-- -----------------------------------------------------
-- Table "opticyou"."clinica"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.clinica;

CREATE TABLE IF NOT EXISTS opticyou.clinica (
  idclinica SERIAL PRIMARY KEY,
  nom VARCHAR(45) NOT NULL,
  direccio VARCHAR(45),
  telefon VARCHAR(45),
  horai_opertura VARCHAR(45),
  horari_tancament VARCHAR(45),
  email VARCHAR(45) NOT NULL
);

-- -----------------------------------------------------
-- Table "opticyou"."treballador"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.treballador;

CREATE TABLE IF NOT EXISTS opticyou.treballador (
  idtreballador SERIAL PRIMARY KEY,
  usuari_idusuari INT NOT NULL,
  especialitat VARCHAR(45),
  estat VARCHAR(10) CHECK (estat IN ('actiu', 'inactiu')),
  inici_jornada VARCHAR(45),
  dies_jornada VARCHAR(45),
  fi_jornada VARCHAR(45),
  clinica_idclinica INT,
  FOREIGN KEY (usuari_idusuari) REFERENCES opticyou.usuari(idusuari) ON DELETE CASCADE,
  FOREIGN KEY (clinica_idclinica) REFERENCES opticyou.clinica(idclinica) ON DELETE SET NULL
);

-- -----------------------------------------------------
-- Table "opticyou"."historial"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.historial;

CREATE TABLE IF NOT EXISTS opticyou.historial (
  idhistorial SERIAL PRIMARY KEY,
  data_creacio TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  patologies VARCHAR(450)
);

-- -----------------------------------------------------
-- Table "opticyou"."client"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.client;

CREATE TABLE IF NOT EXISTS opticyou.client (
  idclient SERIAL PRIMARY KEY,
  usuari_idusuari INT NOT NULL,
  data_naixament DATE,
  sexe VARCHAR(10) CHECK (sexe IN ('Home', 'Dona', 'Altres')),
  telefon VARCHAR(45),
  clinica_idclinica INT,
  historial_idhistorial INT NOT NULL,
  FOREIGN KEY (usuari_idusuari) REFERENCES opticyou.usuari(idusuari) ON DELETE CASCADE,
  FOREIGN KEY (clinica_idclinica) REFERENCES opticyou.clinica(idclinica) ON DELETE SET NULL,
  FOREIGN KEY (historial_idhistorial) REFERENCES opticyou.historial(idhistorial) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table "opticyou"."prova"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.prova;

CREATE TABLE IF NOT EXISTS opticyou.prova (
  idprova SERIAL PRIMARY KEY,
  nom VARCHAR(45) NOT NULL,
  descripcio VARCHAR(450)
);

-- -----------------------------------------------------
-- Table "opticyou"."cita"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.cita;

CREATE TABLE IF NOT EXISTS opticyou.cita (
  idcita SERIAL PRIMARY KEY,
  date TIMESTAMP NOT NULL,
  tipus VARCHAR(15) CHECK (tipus IN ('prova', 'seguiment', 'basica')) NOT NULL,
  estat VARCHAR(15) CHECK (estat IN ('reservada', 'cancelada', 'completada')) NOT NULL,
  prova_idprova INT,
  clinica_idclinica INT NOT NULL,
  FOREIGN KEY (prova_idprova) REFERENCES opticyou.prova(idprova) ON DELETE SET NULL,
  FOREIGN KEY (clinica_idclinica) REFERENCES opticyou.clinica(idclinica) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table "opticyou"."diagnostic"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.diagnostic;

CREATE TABLE IF NOT EXISTS opticyou.diagnostic (
  iddiagnostic SERIAL PRIMARY KEY,
  descripcio VARCHAR(450),
  date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  historial_idhistorial INT NOT NULL,
  FOREIGN KEY (historial_idhistorial) REFERENCES opticyou.historial(idhistorial) ON DELETE CASCADE
);

-- -----------------------------------------------------
-- Table "opticyou"."exercici"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.exercici;

CREATE TABLE IF NOT EXISTS opticyou.exercici (
  idexercici SERIAL PRIMARY KEY,
  nom VARCHAR(45) NOT NULL,
  descripcio VARCHAR(450) NOT NULL
);

-- -----------------------------------------------------
-- Table "opticyou"."medicacio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.medicacio;

CREATE TABLE IF NOT EXISTS opticyou.medicacio (
  idmedicacio SERIAL PRIMARY KEY,
  nom VARCHAR(45) NOT NULL,
  descripcio VARCHAR(450)
);

-- -----------------------------------------------------
-- Table "opticyou"."pauta"
-- -----------------------------------------------------
DROP TABLE IF EXISTS opticyou.pauta;

CREATE TABLE IF NOT EXISTS opticyou.pauta (
  idpauta SERIAL PRIMARY KEY,
  date_inici TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  descripcio VARCHAR(450) NOT NULL,
  dies INT,
  frequencia INT,
  date_fi TIMESTAMP,
  exercici_idexercici INT,
  medicacio_idmedicacio INT,
  diagnostic_iddiagnostic INT NOT NULL,
  FOREIGN KEY (exercici_idexercici) REFERENCES opticyou.exercici(idexercici) ON DELETE SET NULL,
  FOREIGN KEY (medicacio_idmedicacio) REFERENCES opticyou.medicacio(idmedicacio) ON DELETE SET NULL,
  FOREIGN KEY (diagnostic_iddiagnostic) REFERENCES opticyou.diagnostic(iddiagnostic) ON DELETE CASCADE
);
