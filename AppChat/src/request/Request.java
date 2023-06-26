package request;

import java.io.Serializable;

public abstract class Request implements Serializable{
	private RequestType rqType;
	public Request(RequestType rqType) {
		this.rqType = rqType;
	}
	public void sendRequest() {
		writeRequest();
	}
	public abstract void writeRequest();
	public RequestType getRqType() {
		// TODO Auto-generated method stub
		return this.rqType;
	}
}
