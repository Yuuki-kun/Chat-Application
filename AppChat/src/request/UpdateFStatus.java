package request;

public class UpdateFStatus extends Request{

	private String id;
	private boolean status = false;
	
	public UpdateFStatus(RequestType rqType, String id, boolean status) {
		super(rqType);
		this.id = id;
		this.status = status;
	}

	private static final long serialVersionUID = 8759063454035152616L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
	}
	
	public String getId() {
		return id;
	}
	public boolean getStatus() {
		return this.status;
	}
	
}
