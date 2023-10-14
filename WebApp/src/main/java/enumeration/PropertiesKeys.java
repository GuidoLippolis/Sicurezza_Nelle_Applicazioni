package enumeration;

public enum PropertiesKeys {

	JDCB_URL,
	PATH_TO_PROPERTIES_FILE,
	
	/*
	 * Properties per il database degli utenti
	 * 
	 * */
	
	USERS_DB_USERNAME,
	USERS_DB_PASSWORD,
	USERS_DB_NAME,
	
	/*
	 * Properties per il database delle password
	 * 
	 * */
	
	PASSWORDS_DB_USERNAME,
	PASSWORDS_DB_PASSWORD,
	PASSWORDS_DB_NAME,
	
	/*
	 * Properties per il database dei salt
	 * 
	 * */
	
	SALTS_DB_USERNAME,
	SALTS_DB_PASSWORD,
	SALTS_DB_NAME,
	
	/*
	 * Properties per il database dei cookie
	 * 
	 * */
	
	COOKIES_DB_USERNAME,
	COOKIES_DB_PASSWORD,
	COOKIES_DB_NAME,
	
	/*
	 * Algoritmo di hashing
	 * 
	 * */
	
	HASHING_ALGORITHM,
	
	/*
	 * Passphrase per crittografia
	 * 
	 * */
	
	PASSPHRASE,
	
	/*
	 * Percorso per i file
	 * 
	 * */
	
	PATH_UPLOADED_FILES
	
}