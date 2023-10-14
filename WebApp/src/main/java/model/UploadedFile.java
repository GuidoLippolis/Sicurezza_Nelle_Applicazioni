package model;

public class UploadedFile {

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