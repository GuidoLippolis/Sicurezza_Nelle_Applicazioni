DROP DATABASE IF EXISTS passwords_db;
DROP DATABASE IF EXISTS salts_db;
DROP DATABASE IF EXISTS users_db;

-- Creazione dei vari database ciascuno contenente una tabella

CREATE DATABASE IF NOT EXISTS users_db;
CREATE DATABASE IF NOT EXISTS passwords_db;
CREATE DATABASE IF NOT EXISTS salts_db;

USE users_db;

CREATE TABLE users (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  photo LONGBLOB
);

USE passwords_db;

CREATE TABLE passwords (

	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    password LONGBLOB NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_db.users(id)

);

USE salts_db;

CREATE TABLE IF NOT EXISTS salts (

	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    salt LONGBLOB NOT NULL,
    FOREIGN KEY (username) REFERENCES users_db.users(username)

);

-- Creazione utenti (esguire tramite utente root)

CREATE USER 'users_usr'@'localhost' IDENTIFIED BY 'users_usr_password';
CREATE USER 'passwords_usr'@'localhost' IDENTIFIED BY 'passwords_usr_password';
CREATE USER 'salts_usr'@'localhost' IDENTIFIED BY 'salts_usr_password';