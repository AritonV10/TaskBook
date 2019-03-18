package taskbook.v1.business.group.entity;

import javax.validation.constraints.NotEmpty;

public class GroupInviteCode {
	
	@NotEmpty(message = "Field can't be empty")
	private String inviteCode;
	
	public GroupInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	
	public String getInviteCode() {
		return this.inviteCode;
	}
	
	public void setInviteCode(final String inviteCode) {
		this.inviteCode = inviteCode;
	}
}
