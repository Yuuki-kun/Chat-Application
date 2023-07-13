package request;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VideoRequest extends Request{

	private String sendToUserID;
	private String senderId;
	byte[] buffer;
    int bytesRead;
    String fileName;
    String timeSend;
	public VideoRequest(RequestType rqType, String sendtoid,	byte[] buffer, String fileName) {
		super(rqType);
		this.sendToUserID = sendtoid;
		this.buffer = buffer;
		this.fileName = fileName;
		this.timeSend = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	public String getTimeSend() {
		return timeSend;
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
	
	public String getFileName() {
		return fileName;
	}
}
