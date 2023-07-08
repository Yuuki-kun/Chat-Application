package request;

public class ResponeFriendRq extends Request{

	//friend with id
	private String id;
	private String name;
	public ResponeFriendRq(RequestType rqType, String id) {
		super(rqType);
		this.id = id;
	}
	
	public ResponeFriendRq(RequestType rqType, String id, String name) {
		super(rqType);
		this.id = id;
		this.name = name;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
	}
	
	public String getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}