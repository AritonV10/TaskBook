package taskbook.v1.business.user.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import taskbook.v1.business.user.entity.validators.UniqueEmail;

public class FormUser {
	
	@UniqueEmail(message = "Email already exists")
	@Pattern(regexp = ".*\\@.+\\.\\w+", message = "Invalid email address")
	private String email;
	
	@NotEmpty(message = "First name field can't be empty")
	private String firstName;
	
	@NotEmpty(message = "Last name field can't be empty")
	private String lastName;
	
	@NotEmpty(message = "Password field can't be empty")
	@Pattern(message = "Password must contain a digit and a special character", regexp = ".*\\d{2}+.*[!-+]|.*[!-+].*\\d{2}+")
	private String password;
	
	private String matchedPassword;
	
	public FormUser(String firstName, String lastName, String email, String password, String matchedPassword) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.matchedPassword = matchedPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMatchedPassword() {
		return matchedPassword;
	}
	public void setMatchedPassword(String matchedPassword) {
		this.matchedPassword = matchedPassword;
	}
	
	
}
