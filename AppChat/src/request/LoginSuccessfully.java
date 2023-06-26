package request;

import accounttype.AccountType;

public class LoginSuccessfully extends Request{

	private AccountType accountType;
	
	public LoginSuccessfully(RequestType rqType, AccountType accType) {
		super(rqType);
		this.accountType = accType;
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
}
