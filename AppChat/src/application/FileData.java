package application;

import java.io.Serializable;

public class FileData implements Serializable {
    private static final long serialVersionUID = 1L;
	private String fileName;
    private byte[] fileData;

    public FileData(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }
}

