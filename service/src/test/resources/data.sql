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
-- -----------------------------------------------------
-- Schema adverts_service
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema adverts_service
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `adverts_service` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`profiles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`profiles` (
  `idprofile` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NULL,
  `rating` DOUBLE NULL,
  PRIMARY KEY (`idprofile`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`users` (
  `id` INT NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `profiles_id` INT NOT NULL,
  `is_active` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_profiles_idx` (`profiles_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_profiles`
    FOREIGN KEY (`profiles_id`)
    REFERENCES `mydb`.`profiles` (`idprofile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`premium_adverts_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`premium_adverts_details` (
  `id` INT NOT NULL,
  `is_active` TINYINT NOT NULL,
  `premium_started` DATE NOT NULL,
  `premium_end` DATE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`adverts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`adverts` (
  `idadverts` INT NOT NULL,
  `ad_name` VARCHAR(45) NOT NULL,
  `ad_price` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL,
  `status` VARCHAR(45) NOT NULL,
  `profiles_idprofile` INT NOT NULL,
  `create_date` DATE NOT NULL,
  `close_date` DATE NULL,
  `premium_adverts_details_id` INT NOT NULL,
  PRIMARY KEY (`idadverts`),
  INDEX `fk_adverts_profiles1_idx` (`profiles_idprofile` ASC) VISIBLE,
  INDEX `fk_adverts_premium_adverts_details1_idx` (`premium_adverts_details_id` ASC) VISIBLE,
  CONSTRAINT `fk_adverts_profiles1`
    FOREIGN KEY (`profiles_idprofile`)
    REFERENCES `mydb`.`profiles` (`idprofile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_adverts_premium_adverts_details1`
    FOREIGN KEY (`premium_adverts_details_id`)
    REFERENCES `mydb`.`premium_adverts_details` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`category` (
  `category_id` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `category_category_id` INT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `fk_category_category1_idx` (`category_category_id` ASC) VISIBLE,
  CONSTRAINT `fk_category_category1`
    FOREIGN KEY (`category_category_id`)
    REFERENCES `mydb`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `mydb`.`adverts_has_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`adverts_has_category` (
  `adverts_idadverts` INT NOT NULL,
  `category_category_id` INT NOT NULL,
  PRIMARY KEY (`adverts_idadverts`, `category_category_id`),
  INDEX `fk_adverts_has_category_category1_idx` (`category_category_id` ASC) VISIBLE,
  INDEX `fk_adverts_has_category_adverts1_idx` (`adverts_idadverts` ASC) VISIBLE,
  CONSTRAINT `fk_adverts_has_category_adverts1`
    FOREIGN KEY (`adverts_idadverts`)
    REFERENCES `mydb`.`adverts` (`idadverts`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_adverts_has_category_category1`
    FOREIGN KEY (`category_category_id`)
    REFERENCES `mydb`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chat` (
  `idchat` INT NOT NULL,
  PRIMARY KEY (`idchat`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`profiles_chats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`profiles_chats` (
  `profiles_idprofile` INT NOT NULL,
  `chat_idchat` INT NOT NULL,
  PRIMARY KEY (`profiles_idprofile`, `chat_idchat`),
  INDEX `fk_profiles_has_chat_chat1_idx` (`chat_idchat` ASC) VISIBLE,
  INDEX `fk_profiles_has_chat_profiles1_idx` (`profiles_idprofile` ASC) VISIBLE,
  CONSTRAINT `fk_profiles_has_chat_profiles1`
    FOREIGN KEY (`profiles_idprofile`)
    REFERENCES `mydb`.`profiles` (`idprofile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_profiles_has_chat_chat1`
    FOREIGN KEY (`chat_idchat`)
    REFERENCES `mydb`.`chat` (`idchat`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`message` (
  `message_idprofile` INT NOT NULL,
  `chat_idchat` INT NOT NULL,
  `message_text` VARCHAR(45) NOT NULL,
  `message_datetime` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`message_idprofile`, `chat_idchat`),
  INDEX `fk_profiles_has_chat1_chat1_idx` (`chat_idchat` ASC) VISIBLE,
  INDEX `fk_profiles_has_chat1_profiles1_idx` (`message_idprofile` ASC) VISIBLE,
  CONSTRAINT `fk_profiles_has_chat1_profiles1`
    FOREIGN KEY (`message_idprofile`)
    REFERENCES `mydb`.`profiles` (`idprofile`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_profiles_has_chat1_chat1`
    FOREIGN KEY (`chat_idchat`)
    REFERENCES `mydb`.`chat` (`idchat`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`roles` (
  `idroles` INT NOT NULL,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idroles`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`users_has_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`users_has_roles` (
  `users_id` INT NOT NULL,
  `roles_idroles` INT NOT NULL,
  PRIMARY KEY (`users_id`, `roles_idroles`),
  INDEX `fk_users_has_roles_roles1_idx` (`roles_idroles` ASC) VISIBLE,
  INDEX `fk_users_has_roles_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_roles_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY (`roles_idroles`)
    REFERENCES `mydb`.`roles` (`idroles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `adverts_service` ;

-- -----------------------------------------------------
-- Table `adverts_service`.`profiles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`profiles` (
  `id` BIGINT NOT NULL,
  `create_date` DATETIME(6) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `phone_number` VARCHAR(255) NULL DEFAULT NULL,
  `rating` DOUBLE NULL DEFAULT NULL,
  `surname` VARCHAR(255) NULL DEFAULT NULL,
  `update_date` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`premium_adverts_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`premium_adverts_details` (
  `id` BIGINT NOT NULL,
  `is_active` BIT(1) NOT NULL,
  `premium_end` DATETIME(6) NULL DEFAULT NULL,
  `premium_started` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`adverts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`adverts` (
  `id` BIGINT NOT NULL,
  `ad_name` VARCHAR(255) NOT NULL,
  `ad_price` DOUBLE NOT NULL,
  `create_date` DATETIME NULL DEFAULT NULL,
  `update_date` DATETIME NULL DEFAULT NULL,
  `close_date` DATETIME(6) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `status` VARCHAR(255) NOT NULL,
  `advert_premium_id` BIGINT NULL DEFAULT NULL,
  `profile_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK9ofgk5epf2oa6i0ddm7bdmtov` (`advert_premium_id` ASC) VISIBLE,
  INDEX `FK2sdlg09ua1vme2ed1xarm4roc` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `FK2sdlg09ua1vme2ed1xarm4roc`
    FOREIGN KEY (`profile_id`)
    REFERENCES `adverts_service`.`profiles` (`id`),
  CONSTRAINT `FK9ofgk5epf2oa6i0ddm7bdmtov`
    FOREIGN KEY (`advert_premium_id`)
    REFERENCES `adverts_service`.`premium_adverts_details` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`categories` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `category_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK1e7hbubpwyuq2199b8jrx42h8` (`category_id` ASC) VISIBLE,
  CONSTRAINT `FK1e7hbubpwyuq2199b8jrx42h8`
    FOREIGN KEY (`category_id`)
    REFERENCES `adverts_service`.`categories` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`adverts_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`adverts_categories` (
  `adverts_id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL,
  INDEX `FKjsxahciaqbx79k0qodhqfseqq` (`category_id` ASC) VISIBLE,
  INDEX `FK8cd2934uitymm4evq8jxd8tfj` (`adverts_id` ASC) VISIBLE,
  CONSTRAINT `FK8cd2934uitymm4evq8jxd8tfj`
    FOREIGN KEY (`adverts_id`)
    REFERENCES `adverts_service`.`adverts` (`id`),
  CONSTRAINT `FKjsxahciaqbx79k0qodhqfseqq`
    FOREIGN KEY (`category_id`)
    REFERENCES `adverts_service`.`categories` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`chat` (
  `id` BIGINT NOT NULL,
  `created` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`comments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `comment_text` VARCHAR(255) NOT NULL,
  `comment_create` DATETIME(6) NULL DEFAULT NULL,
  `comment_delete` DATETIME(6) NULL DEFAULT NULL,
  `comment_update` DATETIME(6) NULL DEFAULT NULL,
  `advert_id` BIGINT NULL DEFAULT NULL,
  `profile_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKsj7n9haex0iuyxhc550c7u96q` (`advert_id` ASC) VISIBLE,
  INDEX `FKop4uhnf2cick4rclmibccg0s5` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `FKop4uhnf2cick4rclmibccg0s5`
    FOREIGN KEY (`profile_id`)
    REFERENCES `adverts_service`.`profiles` (`id`),
  CONSTRAINT `FKsj7n9haex0iuyxhc550c7u96q`
    FOREIGN KEY (`advert_id`)
    REFERENCES `adverts_service`.`adverts` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`hibernate_sequence` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`messages` (
  `id` BIGINT NOT NULL,
  `message_text` VARCHAR(255) NULL DEFAULT NULL,
  `message_write_time` DATETIME(6) NULL DEFAULT NULL,
  `chat_id` BIGINT NULL DEFAULT NULL,
  `profile_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKdumcu11xf8ldun8i4xpd04j43` (`chat_id` ASC) VISIBLE,
  INDEX `FKhisnpkjwl1jfmtdyysw0q3upw` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `FKdumcu11xf8ldun8i4xpd04j43`
    FOREIGN KEY (`chat_id`)
    REFERENCES `adverts_service`.`chat` (`id`),
  CONSTRAINT `FKhisnpkjwl1jfmtdyysw0q3upw`
    FOREIGN KEY (`profile_id`)
    REFERENCES `adverts_service`.`profiles` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`profiles_chats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`profiles_chats` (
  `profile_id` BIGINT NOT NULL,
  `chat_id` BIGINT NOT NULL,
  INDEX `FKqoi6plyg6nsci3mckup5jssol` (`chat_id` ASC) VISIBLE,
  INDEX `FKftalmppikv4dgirahdpflnu3y` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `FKftalmppikv4dgirahdpflnu3y`
    FOREIGN KEY (`profile_id`)
    REFERENCES `adverts_service`.`profiles` (`id`),
  CONSTRAINT `FKqoi6plyg6nsci3mckup5jssol`
    FOREIGN KEY (`chat_id`)
    REFERENCES `adverts_service`.`chat` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`ratings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`ratings` (
  `id` INT NOT NULL,
  `rating` DOUBLE NULL DEFAULT NULL,
  `profile_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ratings_profiles1_idx` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `fk_ratings_profiles1`
    FOREIGN KEY (`profile_id`)
    REFERENCES `adverts_service`.`profiles` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `roles` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`seq_adverts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`seq_adverts` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`seq_adverts_premium`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`seq_adverts_premium` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`seq_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`seq_category` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`seq_chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`seq_chat` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`seq_profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`seq_profile` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`seq_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`seq_user` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`users` (
  `id` BIGINT NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `status` INT NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `profile_id` BIGINT NULL DEFAULT NULL,
  `generated_code` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_6dotkott2kjsp8vw4d0m25fb7` (`email` ASC) VISIBLE,
  UNIQUE INDEX `UK_r43af9ap4edm43mmtq01oddj6` (`username` ASC) VISIBLE,
  INDEX `FKq2e6rj0p6p1gec2cslmaxugw1` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `FKq2e6rj0p6p1gec2cslmaxugw1`
    FOREIGN KEY (`profile_id`)
    REFERENCES `adverts_service`.`profiles` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `adverts_service`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `adverts_service`.`users_roles` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  INDEX `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id` ASC) VISIBLE,
  INDEX `FK2o0jvgh89lemvvo17cbqvdxaa` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa`
    FOREIGN KEY (`user_id`)
    REFERENCES `adverts_service`.`users` (`id`),
  CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy`
    FOREIGN KEY (`role_id`)
    REFERENCES `adverts_service`.`roles` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
