package model;

import java.util.Arrays;

public class User {

	private int id;
	private String username;
	private byte[] photo;
	
	public User() {
		super();
	}

	public User(String username) {
		super();
		this.username = username;
	}

	public User(String username, byte[] photo) {
		super();
		this.username = username;
		this.photo = photo;
	}
	
	
	public User(int id, String username) {
		super();
		this.id = id;
		this.username = username;
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
		return "User [username=" + username + ", photo=" + Arrays.toString(photo) + "]";
	}
	
}