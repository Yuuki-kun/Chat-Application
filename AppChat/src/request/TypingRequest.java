package request;

public class TypingRequest extends Request{

	String id, sendtoid;
	int dur;
	public TypingRequest(RequestType rqType, String id, String sendtoid, int dur) {
		super(rqType);
		this.id = id;
		this.sendtoid = sendtoid;
		this.dur = dur;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		return id;
	}
	public String getSendtoid() {
		return sendtoid;
	}
	public int getDur() {
		return dur;
	}
}
