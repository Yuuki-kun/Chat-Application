package request;

public class AudioRequest extends Request{

	private String sendToId;
	private String fileName;
	private byte[] fileData;
	private String senderId;
	
	public AudioRequest(RequestType rqType, String sendtoid, String filename, byte[] fileData) {
		super(rqType);
		this.sendToId = sendtoid;
		this.fileName = filename;
		this.fileData = fileData;
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	public String getSenderId() {
		return senderId;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public String getSendToId() {
		return sendToId;
	}

	public String getFileName() {
		return fileName;
	}
	public byte[] getFileData() {
		return fileData;
	}
}
