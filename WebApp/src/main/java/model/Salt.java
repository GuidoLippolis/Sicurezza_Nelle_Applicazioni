package model;

public class Salt {

	private int userId;
	private byte[] saltValue;
	
	public Salt() {
		super();
	}

	public Salt(int userId, byte[] salt) {
		super();
		this.userId = userId;
		this.saltValue = salt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getSaltValue() {
		return saltValue;
	}

	public void setSaltValue(byte[] salt) {
		this.saltValue = salt;
	}
	
}
