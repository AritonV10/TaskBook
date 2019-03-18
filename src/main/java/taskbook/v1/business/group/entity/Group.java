package taskbook.v1.business.group.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.entity.AbstractObject;

@Entity
@Table(name = "groups")

public class Group extends AbstractObject implements Comparable<Group>{
	
	private static final long serialVersionUID = 1012155770198181993L;
	
	private String name;
	
	@Transient
	private Integer adminID;
	
	@Transient
	private Integer transientID;
	
	@Column(name = "invite_code")
	private String inviteCode;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "group_members", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> members = new HashSet<>();
	
	@ManyToOne
	private User administrator;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SubGroup> subGroups = new ArrayList<>();
	
	public Group(final String name, final Integer administratorId) {
		this.name = name;
		this.administrator = new User(administratorId);
	}
	
	public Group(String duplicate, final String name, final Integer ID) {
		this.transientID = ID;
		this.name = name;
	}
	
	public Group(final Integer ID, final String name, final Integer userID) {
		this(name, userID);
		this.transientID = ID;
	}
	
	public Group(final Integer duplicate, final Integer ID, final String name, final Integer adminID) {
		this.name = name;
		this.transientID = ID;
		this.adminID = adminID;
	}
	
	public Group() {
		
	}
	
	@PrePersist
	public void initCode() {
		this.inviteCode = generateInviteCode();
	}
	
	public Integer getTransientID() {
		return this.transientID;
	}
	
	public Integer getAdminID() {
		return this.adminID;
	}
	
	public String generateInviteCode() {
		String c = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
	    for(int i = 1; i <= 9; ++i) {
	    	builder.append(c.charAt(random.nextInt(c.length())));
	    	if(i % 3 == 0) {
	    	    builder.append("-");
	    	}
	    }
	    return builder.toString();
	}
	
	public boolean compareToName(Group a) {
		if(this.getName().compareTo(a.getName()) <= -1) {
			return false;
		}
		return true;
	}
	
	public boolean equalNames(Group other) {
		return this.name.equals(other.getName());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getMemebers() {
		return members;
	}

	public void setMemebers(Set<User> memebers) {
		this.members = memebers;
	}
	
	
	public void addSubgroup(SubGroup sub) {
		sub.setGroups(this);
		this.getSubGroups().add(sub);
	}
	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public User getAdministrator() {
		return administrator;
	}

	public void setAdministrator(User administrator) {
		this.administrator = administrator;
	}

	public List<SubGroup> getSubGroups() {
		return subGroups;
	}

	public void setSubGroups(List<SubGroup> subGroups) {
		this.subGroups = subGroups;
	}

	@Override
	public int compareTo(Group arg0) {
		return 0;
	}
}
