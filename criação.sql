-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema apartamento
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema apartamento
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `apartamento` DEFAULT CHARACTER SET utf8 ;
USE `apartamento` ;

-- -----------------------------------------------------
-- Table `apartamento`.`Conta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `apartamento`.`Conta` (
  `idConta` INT NOT NULL COMMENT '',
  `divida` FLOAT NOT NULL COMMENT '',
  `numeroPrestacoes` INT NOT NULL COMMENT '',
  PRIMARY KEY (`idConta`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `apartamento`.`Inquilino`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `apartamento`.`Inquilino` (
  `numCC` INT NOT NULL COMMENT '',
  `email` VARCHAR(45) NOT NULL COMMENT '',
  `numTelemovel` INT NOT NULL COMMENT '',
  `nome` VARCHAR(60) NOT NULL COMMENT '',
  `password` VARCHAR(60) NOT NULL COMMENT '',
  `idConta` INT NOT NULL COMMENT '',
  `admin` BOOLEAN NOT NULL COMMENT '',
  PRIMARY KEY (`numCC`)  COMMENT '',
  INDEX `fk_Inquilino_Conta_idx` (`idConta` ASC)  COMMENT '',
  CONSTRAINT `fk_Inquilino_Conta`
    FOREIGN KEY (`idConta`)
    REFERENCES `apartamento`.`Conta` (`idConta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `apartamento`.`Comprovativo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `apartamento`.`Comprovativo` (
  `idComprovativo` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `montante` FLOAT NOT NULL COMMENT '',
  `data` VARCHAR(10) NOT NULL COMMENT '',
  `idConta` INT NOT NULL COMMENT '',
  PRIMARY KEY (`idComprovativo`)  COMMENT '',
  INDEX `fk_Comprovativo_Conta1_idx` (`idConta` ASC)  COMMENT '',
  CONSTRAINT `fk_Comprovativo_Conta1`
    FOREIGN KEY (`idConta`)
    REFERENCES `apartamento`.`Conta` (`idConta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `apartamento`.`Despesa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `apartamento`.`Despesa` (
  `idDespesa` VARCHAR(45) NOT NULL COMMENT '',
  `valor` FLOAT NOT NULL COMMENT '',
  PRIMARY KEY (`idDespesa`)  COMMENT '',
  UNIQUE INDEX `idDespesa_UNIQUE` (`idDespesa` ASC)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `apartamento`.`Taxa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `apartamento`.`Taxa` (
  `taxa` FLOAT NOT NULL COMMENT '',
  `idConta` INT NOT NULL COMMENT '',
  `idDespesa` VARCHAR(45) NOT NULL COMMENT '',
  INDEX `fk_Taxa_Conta1_idx` (`idConta` ASC)  COMMENT '',
  INDEX `fk_Taxa_Despesa1_idx` (`idDespesa` ASC)  COMMENT '',
  PRIMARY KEY (`idConta`, `idDespesa`)  COMMENT '',
  CONSTRAINT `fk_Taxa_Conta1`
    FOREIGN KEY (`idConta`)
    REFERENCES `apartamento`.`Conta` (`idConta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Taxa_Despesa1`
    FOREIGN KEY (`idDespesa`)
    REFERENCES `apartamento`.`Despesa` (`idDespesa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
