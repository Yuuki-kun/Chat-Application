package request;

public class SendImageRequest extends Request{

	private String sendToID;
	private String senderId;
	private byte[] buffer;
	private String fileName;
	public SendImageRequest(RequestType rqType, String sendToID, String fileName, byte[] buffer) {
		super(rqType);
		this.sendToID = sendToID;
		this.fileName = fileName;
		this.buffer = buffer;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		
	}

	public String getSendToID() {
		return sendToID;
	}
	public byte[] getBuffer() {
		return buffer;
	}
	public void setSendToID(String sendToID) {
		this.sendToID = sendToID;
	}
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	public String getFileName() {
		return fileName;
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	public String getSenderId() {
		return this.senderId;
	}
	
}
