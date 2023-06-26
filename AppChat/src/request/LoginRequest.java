package request;

import java.io.IOException;

import application.models.Model;

public class LoginRequest extends Request{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	public LoginRequest(RequestType rqType, String username, String password) {
		super(rqType);
		this.username = username;
		this.password = password;
	}
	@Override
	public void writeRequest() {
		try {
			Model.getInstance().getClient().getOut().writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("send login request failed.");
			e.printStackTrace();
		}
	}

}
