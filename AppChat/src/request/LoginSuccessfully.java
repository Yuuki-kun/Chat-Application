package request;

import accounttype.AccountType;

public class LoginSuccessfully extends Request{

	private AccountType accountType;
	private String usernameFromDB;
	
	public LoginSuccessfully(RequestType rqType, AccountType accType, String usrn) {
		super(rqType);
		this.accountType = accType;
		this.usernameFromDB = usrn;
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
}
