package request;

import java.util.Map;

public class GetFriendList extends Request{

	private Map<String, String> friendList; 
	private Map<String, Boolean> friendListOnlineStatus; 

	public GetFriendList(RequestType rqType, Map<String, String> friendList, Map<String, Boolean> friendListOnlineStatus) {
		super(rqType);
		this.friendList = friendList;
		this.friendListOnlineStatus = friendListOnlineStatus;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		
	}

	public Map<String, String> getFriendList() {
		return friendList;
	}
	
	public Map<String, Boolean> getFriendListOnlineStatus() {
		return friendListOnlineStatus;
	}
}
