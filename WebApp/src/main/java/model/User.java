package model;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {

	private int id;
	private String email;
	private byte[] photo;
	private Salt salt;
	
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
	
	public User(int id, String email, byte[] photo) {
		super();
		this.id = id;
		this.email = email;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public Salt getSalt() {
		return salt;
	}

	public void setSalt(Salt salt) {
		this.salt = salt;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", photo=" + Arrays.toString(photo) + "]";
	}
	
}