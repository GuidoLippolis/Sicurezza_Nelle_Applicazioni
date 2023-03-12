package model;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {

	private int id;
	private String username;
	private byte[] photo;
	
	public User() {
		super();
	}

	public User(String email) {
		super();
		this.username = email;
	}

	public User(String email, byte[] photo) {
		super();
		this.username = email;
		this.photo = photo;
	}
	
	public User(int id, String email, byte[] photo) {
		super();
		this.id = id;
		this.username = email;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	@Override
	public String toString() {
		return "User [email=" + username + ", photo=" + Arrays.toString(photo) + "]";
	}
	
}