package request;

import java.util.ArrayList;

public class GetFriendList extends Request{

	private ArrayList<String> friendList; 
	
	public GetFriendList(RequestType rqType, ArrayList<String> friendList) {
		super(rqType);
		this.friendList = friendList;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		
	}

	public ArrayList<String> getFriendList() {
		return friendList;
	}
}
