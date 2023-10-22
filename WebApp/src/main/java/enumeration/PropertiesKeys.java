package enumeration;

public enum PropertiesKeys {

	// Property per l'URL al database
	JDCB_URL,
	
	// Utilizzata per il recupero del percorso al file application.properties
	
	PATH_TO_PROPERTIES_FILE,
	
	// Properties per il database degli utenti
	
	USERS_DB_USERNAME,
	USERS_DB_PASSWORD,
	USERS_DB_NAME,
	
	// Properties per il database delle password
	
	PASSWORDS_DB_USERNAME,
	PASSWORDS_DB_PASSWORD,
	PASSWORDS_DB_NAME,
	
	// Properties per il database dei salt
	
	SALTS_DB_USERNAME,
	SALTS_DB_PASSWORD,
	SALTS_DB_NAME,
	
	// Properties per il database dei cookie
	
	COOKIES_DB_USERNAME,
	COOKIES_DB_PASSWORD,
	COOKIES_DB_NAME,
	
	// Algoritmo di hashing
	
	HASHING_ALGORITHM,
	
	// Modalità di crittografia
	
	CRYPTOGRAPHY_MODE,
	
	/*
	 * Algoritmo di crittografia simmetrica
	 * 
	 * */
	
	SYMMETRIC_ENCRYPTION_ALGORITM,
	
	/*
	 * Passphrase per crittografia
	 * 
	 * */
	
	PASSPHRASE
	
}