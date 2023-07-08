package request;

public class FriendRequest extends Request{

	private String friendId;
	private String name;
	
	public FriendRequest(RequestType rqType, String id) {
		super(rqType);
		this.friendId = id;
	}
	
	public FriendRequest(RequestType rqType, String id, String name) {
		super(rqType);
		this.friendId = id;
		this.name = name;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public String getFriendId() {
		return friendId;
	}
	
	public String getName() {
		return name;
	}
	

}
