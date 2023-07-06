package request;

import accounttype.AccountType;

public class LoginSuccessfully extends Request{

	private AccountType accountType;
	private String usernameFromDB;
	private String id;
	
	public LoginSuccessfully(RequestType rqType, AccountType accType, String usrn, String id) {
		super(rqType);
		this.accountType = accType;
		this.usernameFromDB = usrn;
		this.id = id;
		// TODO Auto-generated constructor stub
	}

	
	
	private static final long serialVersionUID = -5729914166601472389L;

	@Override
	public void writeRequest() {
		
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
	public AccountType getAccountType() {
		return accountType;
	}
	
	public String getUsernameFromDB() {
		return usernameFromDB;
	}
	
	public String getId() {
		return id;
	}
}
