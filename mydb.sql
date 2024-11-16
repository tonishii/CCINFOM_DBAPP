-- MySQL Script generated by MySQL Workbench
-- Wed Nov 13 07:52:18 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`couriers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`couriers` ;

CREATE TABLE IF NOT EXISTS `mydb`.`couriers` (
  `courier_id` INT UNSIGNED NOT NULL,
  `courier_name` VARCHAR(50) NOT NULL,
  `courier_email_address` VARCHAR(50) NULL,
  `courier_address` VARCHAR(50) NOT NULL,
  `courier_verified_status` BIT NOT NULL,
  PRIMARY KEY (`courier_id`),
  UNIQUE INDEX `courier_id_UNIQUE` (`courier_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`order_contents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`order_contents` ;

CREATE TABLE IF NOT EXISTS `mydb`.`order_contents` (
  `order_id` INT UNSIGNED NOT NULL,
  `product_id` INT UNSIGNED NOT NULL,
  `item_quantity` INT NOT NULL,
  `subtotal` FLOAT,
  `product_rating` INT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id` ASC),
  INDEX `product_id_idx` (`product_id` ASC),
  CONSTRAINT `order_id`
    FOREIGN KEY (`order_id`)
    REFERENCES `mydb`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `mydb`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`orders` ;

CREATE TABLE IF NOT EXISTS `mydb`.`orders` (
  `order_id` INT UNSIGNED NOT NULL,
  `user_id` INT UNSIGNED NOT NULL,
  `courier_id` INT UNSIGNED NOT NULL,
  `purchase_date` DATETIME NOT NULL,
  `total_price` FLOAT,
  `order_status` ENUM('BEING_PREPARED', 'FOR_DELIVERY', 'DELIVERED', 'REFUNDED') NOT NULL,
  `receive_date` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id` ASC),
  INDEX `user_id_idx` (`user_id` ASC),
  INDEX `courier_id_idx` (`courier_id` ASC),
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `courier_id`
    FOREIGN KEY (`courier_id`)
    REFERENCES `mydb`.`couriers` (`courier_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`products`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`products` ;

CREATE TABLE IF NOT EXISTS `mydb`.`products` (
  `product_id` INT UNSIGNED NOT NULL,
  `seller_id` INT UNSIGNED NOT NULL,
  `product_name` VARCHAR(50) NOT NULL,
  `product_price` FLOAT NOT NULL,
  `product_type` VARCHAR(50) NOT NULL,
  `average_rating` FLOAT,
  `quantity_stocked` INT NOT NULL,
  `listed_status` BIT NOT NULL,
  `description` TEXT NULL,
  PRIMARY KEY (`product_id`, `seller_id`),
  INDEX `seller_id_idx` (`seller_id` ASC),
  CONSTRAINT `seller_id`
    FOREIGN KEY (`seller_id`)
    REFERENCES `mydb`.`sellers` (`seller_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`returns`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`returns` ;

CREATE TABLE IF NOT EXISTS `mydb`.`returns` (
  `order_id` INT UNSIGNED NOT NULL,
  `product_id` INT UNSIGNED NOT NULL,
  `courier_id` INT UNSIGNED NOT NULL,
  `return_reason` ENUM('Damaged Item', 'Wrong Item', 'Change of Mind', 'Counterfeit Item') NOT NULL,
  `return_description` TEXT NULL,
  `return_date` DATETIME NOT NULL,
  `return_status` ENUM('PROCESSING', 'REFUNDED') NOT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX `product_id_idx` (`product_id` ASC),
  INDEX `courier_id_idx` (`courier_id` ASC),
  CONSTRAINT `order_id_c`
    FOREIGN KEY (`order_id`)
    REFERENCES `mydb`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_id_c`
    FOREIGN KEY (`product_id`)
    REFERENCES `mydb`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `courier_id_c`
    FOREIGN KEY (`courier_id`)
    REFERENCES `mydb`.`couriers` (`courier_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`sellers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`sellers` ;

CREATE TABLE IF NOT EXISTS `mydb`.`sellers` (
  `seller_id` INT UNSIGNED NOT NULL,
  `seller_name` VARCHAR(50) NOT NULL,
  `seller_address` VARCHAR(50) NULL,
  `seller_verified_status` BIT NOT NULL,
  `seller_phone_number` VARCHAR(20) NULL,
  `seller_creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`seller_id`),
  UNIQUE INDEX `seller_id_UNIQUE` (`seller_id` ASC));


-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`users` ;

CREATE TABLE IF NOT EXISTS `mydb`.`users` (
  `user_id` INT UNSIGNED NOT NULL,
  `user_name` VARCHAR(50) NOT NULL,
  `user_phone_number` VARCHAR(20) NULL,
  `user_address` VARCHAR(50) NULL,
  `user_verified_status` BIT NOT NULL,
  `user_creation_date` DATETIME NOT NULL,
  `user_firstname` VARCHAR(50) NOT NULL,
  `user_lastname` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;