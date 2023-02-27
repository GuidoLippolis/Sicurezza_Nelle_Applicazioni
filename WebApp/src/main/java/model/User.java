package model;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {

	private String email;
	private byte[] photo;
	
	public User() {
		super();
	}

	public User(String email) {
		super();
		this.email = email;
	}

	public User(String email, byte[] photo) {
		super();
		this.email = email;
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", photo=" + Arrays.toString(photo) + "]";
	}
	
}