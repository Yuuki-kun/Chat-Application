package request;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message extends Request{

	private String message;
	private String timeSend;
	private String sendID;
	private String fromID;
	public Message(RequestType rqType, String message) {
		super(rqType);
		this.message = message;
		this.timeSend = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		// TODO Auto-generated constructor stub
	}

	public Message(RequestType rqType, String message, String sendID, String timesend, String fromID) {
		super(rqType);
		this.message = message;
		this.timeSend = timesend;
		this.sendID = sendID;
		this.fromID = fromID;
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
	public String getFromID() {
		return fromID;
	}
}
