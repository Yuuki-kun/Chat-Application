package request;

public class VideoRequest extends Request{

	private String sendToUserID;
	private String senderId;
	byte[] buffer;
    int bytesRead;
	public VideoRequest(RequestType rqType, String sendtoid,	byte[] buffer) {
		super(rqType);
		this.sendToUserID = sendtoid;
		this.buffer = buffer;
	}
	

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	public String getSenderID() {
		return senderId;
	}
	
	public String getSendToID() {
		return this.sendToUserID;
	}
	public byte[] getBuffer() {
		return buffer;
	}
	public int getBytesRead() {
		return bytesRead;
	}
}
