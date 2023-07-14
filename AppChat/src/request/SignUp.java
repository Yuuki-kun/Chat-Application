package request;

public class SignUp extends Request{

	String username, password, name, city, district, district2, street,type,time;
	
	
	public SignUp(RequestType rqType,
			String username, 
			String password, 
			String name,
			String city, 
			String district, 
			String district2, 
			String street,
			String type
			
			) {
		super(rqType);
		this.username = username;
		this.password = password;
		this.name = name;
		this.city = city;
		this.district = district;
		this.district2 = district2;
		this.street = street;
		this.type = type;
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void writeRequest() {
		// TODO Auto-generated method stub
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDistrict2() {
		return district2;
	}

	public void setDistrict2(String district2) {
		this.district2 = district2;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
//	
//	public String getTime() {
//		return time;
//	}
//
//	public void setTime(String time) {
//		this.time = time;
//	}
//	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
