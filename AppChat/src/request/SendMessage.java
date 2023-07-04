package request;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SendMessage extends Request{

	private String message;
	private String timeSend;
	private String sendID;
	
	public SendMessage(RequestType rqType, String message) {
		super(rqType);
		this.message = message;
		this.timeSend = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		// TODO Auto-generated constructor stub
	}

	public SendMessage(RequestType rqType, String message, String sendID) {
		super(rqType);
		this.message = message;
		this.timeSend = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		this.sendID = sendID;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public String getSendID() {
		return sendID;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTimeSend() {
		return timeSend;
	}
}
