package model;

public class Salt {

	private int userId;
	private byte[] salt;
	
	public Salt() {
		super();
	}

	public Salt(int userId, byte[] salt) {
		super();
		this.userId = userId;
		this.salt = salt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
}
