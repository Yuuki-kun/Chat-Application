package request;

public class SeenStatus extends Request{

	private boolean seen = false;
	private String id;
	
	public SeenStatus(RequestType rqType, boolean seen, String id) {
		super(rqType);
		this.seen = seen;
		this.id = id;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean getSeen() {
		return this.seen;
	}

	public String getId() {
		return id;
	}
}
