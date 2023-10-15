package model;

public class UploadedFile {

	private int id;
	private String fileName;
	private byte[] fileData;
	private String username;
	
	public UploadedFile() {
		super();
	}

	public UploadedFile(String fileName, byte[] fileData, String username) {
		super();
		this.fileName = fileName;
		this.fileData = fileData;
		this.username = username;
	}
	
	public UploadedFile(int id, String fileName, byte[] fileData, String username) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileData = fileData;
		this.username = username;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}