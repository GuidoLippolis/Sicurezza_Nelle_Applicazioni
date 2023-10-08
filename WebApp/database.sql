DROP DATABASE IF EXISTS passwords_db;
DROP DATABASE IF EXISTS salts_db;
DROP DATABASE IF EXISTS cookies_db;
DROP DATABASE IF EXISTS users_db;

-- Creazione dei vari database ciascuno contenente una tabella

CREATE DATABASE IF NOT EXISTS users_db;
CREATE DATABASE IF NOT EXISTS passwords_db;
CREATE DATABASE IF NOT EXISTS salts_db;
CREATE DATABASE IF NOT EXISTS cookies_db;

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
    FOREIGN KEY (user_id) REFERENCES users_db.users(id) ON DELETE CASCADE

);

USE salts_db;

CREATE TABLE IF NOT EXISTS salts (

	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    salt LONGBLOB NOT NULL,
    FOREIGN KEY (username) REFERENCES users_db.users(username) ON DELETE CASCADE

);

USE cookies_db;

CREATE TABLE IF NOT EXISTS cookies (

	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cookie_value VARCHAR(50) NOT NULL UNIQUE,
    expiration_date DATE NOT NULL,
	user_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_db.users(id) ON DELETE CASCADE

);

-- Creazione utenti (esguire tramite utente root)

CREATE USER 'users_usr'@'localhost' IDENTIFIED BY 'users_usr_password';
CREATE USER 'passwords_usr'@'localhost' IDENTIFIED BY 'passwords_usr_password';
CREATE USER 'salts_usr'@'localhost' IDENTIFIED BY 'salts_usr_password';
CREATE USER 'cookies_usr'@'localhost' IDENTIFIED BY 'cookies_usr_password';

/*
	Per la tabella degli utenti, fornisco all'utente "users_usr@localhost"
	il permesso di INSERT sui database: users_db (tabella users); passwords_db
	(tabella passwords) e salts_db (tabella salts). Fornisco inoltre il permesso
	di SELECT sul database users_db (tabella users)

*/

GRANT INSERT ON users_db.users TO 'users_usr'@'localhost';
GRANT INSERT ON passwords_db.passwords TO 'users_usr'@'localhost';
GRANT INSERT ON salts_db.salts TO 'users_usr'@'localhost';
GRANT SELECT ON users_db.users TO 'users_usr'@'localhost';

/*
	Per la tabella dei valori di valori di salt (tabella salts) fornisco all'utente 
	"salts_usr@localhost" il permesso di SELECT

*/

GRANT SELECT ON salts_db.salts TO 'salts_usr'@'localhost';

/*
	Per la tabella dei valori di valori delle password (tabella passwords) fornisco all'utente 
	"passwords_usr@localhost" il permesso di SELECT

*/

GRANT SELECT ON passwords_db.passwords TO 'passwords_usr'@'localhost';

/*
	Per la tabella dei cookie (tabella cookies) fornisco all'utente 
	"cookies_usr@localhost" il permesso di INSERT

*/

GRANT INSERT ON cookies_db.cookies TO 'cookies_usr'@'localhost';
GRANT SELECT ON cookies_db.cookies TO 'cookies_usr'@'localhost';

select * from users_db.users; select * from passwords_db.passwords; select * from salts_db.salts; select * from cookies_db.cookies;
DELETE FROM passwords_db.passwords; DELETE FROM salts_db.salts; DELETE FROM cookies_db.cookies; DELETE FROM users_db.users;


