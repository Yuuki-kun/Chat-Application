package request;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServerMessage extends Request{

	private static final long serialVersionUID = 1L;
	private String message;
	private String timeSend;
	
	public ServerMessage(RequestType rqType, String message) {
		super(rqType);
		this.message = message;
		this.timeSend = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	

	
	public String getMessage() {
		return message;
	}
	
	public String getTimeSend() {
		return timeSend;
	}
}
