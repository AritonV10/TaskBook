package taskbook.v1.business.user.entity;

public enum Role {
	ADMIN("Administrator"), USER("User");
	
	private final String toString;
	Role(final String toString) {
		this.toString = toString;
	}
	public String toString() {
		return this.toString;
	}
}
