package model;

import java.io.Serializable;

public class Password implements Serializable {

	private int userId;
	private byte[] passwordValue;
	
	public Password() {
		super();
	}

	public Password(int userId, byte[] passwordValue) {
		super();
		this.userId = userId;
		this.passwordValue = passwordValue;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getPasswordValue() {
		return passwordValue;
	}

	public void setPasswordValue(byte[] passwordValue) {
		this.passwordValue = passwordValue;
	}
	
}
