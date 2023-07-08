package request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetSearchList extends Request{

	private static final long serialVersionUID = 1L;
	private Map<String, String> searchList = new HashMap<>();
	private String input;
	
	public GetSearchList(RequestType rqType, String input) {
		super(rqType);
		this.input = input;
	}
	

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSearchList(Map<String, String> searchList) {
		this.searchList = searchList;
	}
	public Map<String, String> getSearchList() {
		return searchList;
	}
	
	public String getInput() {
		return input;
	}

}
