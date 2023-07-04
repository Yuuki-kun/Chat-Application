package request;

import java.util.Map;

public class GetFriendList extends Request{

	private Map<String, String> friendList; 
	
	public GetFriendList(RequestType rqType, Map<String, String> friendList) {
		super(rqType);
		this.friendList = friendList;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		
	}

	public Map<String, String> getFriendList() {
		return friendList;
	}
}
