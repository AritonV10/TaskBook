package taskbook.v1.business.user.entity;

import javax.security.enterprise.credential.UsernamePasswordCredential;

public class LoginCredentials {
	private String email;
	private String password;
	
	public LoginCredentials(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public UsernamePasswordCredential getCredentials() {
		return new UsernamePasswordCredential(this.email, this.password);
	}
}
