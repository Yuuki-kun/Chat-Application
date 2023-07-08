package request;

public class SendMessageStatus extends Request{

	private boolean sent = false;
	
	public SendMessageStatus(RequestType rqType, boolean sent) {
		super(rqType);
		this.sent = sent;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean getSent() {
		return this.sent;
	}

}
