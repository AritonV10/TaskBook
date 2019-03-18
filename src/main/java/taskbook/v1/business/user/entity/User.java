package taskbook.v1.business.user.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.task.entity.FinishedTasks;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.platform.entity.AbstractObject;


@Entity
@Table(name = "users")
public class User extends AbstractObject {

	private static final long serialVersionUID = 3492917829621069363L;
	
	@Transient
	private Integer id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	private String password;
	private String email;
	
	@Column(name = "is_banned")
	private boolean isBanned;
	
	@Enumerated(EnumType.STRING)
	private Role role;
    
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "administrator", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Group> createdGroups = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private List<Task> takenTasks = new ArrayList<>();

	@OneToMany(targetEntity = FinishedTasks.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FinishedTasks> finishedTasks = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "group_members", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
	private List<Group> joinedGroups = new ArrayList<>();
	
	public User(final String password, final Role role, final boolean isBanned) {
		this.password = password;
		this.role = role;
		this.isBanned = isBanned;
	}
	
	public User(final String password, final String email) {
		this(password, Role.USER, false);
		this.email = email;
	}
	
	public User(final Integer ID, final String email) {
		this.id = ID;
		this.email = email;
	}
	
	public User(Integer id) {
		super.setId(id);
	}
	
	public User() {}
	
	public void addGroup(Group group) {
		this.getCreatedGroups()
		.add(group);
		group
			.setAdministrator(this);
	}
	
	public Integer getTransientId() {
		return this.id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Role getRank() {
		return role;
	}

	public void setRank(Role rank) {
		this.role = rank;
	}

	public List<FinishedTasks> getFinishedTasks() {
		return finishedTasks;
	}

	public void setFinishedTasks(List<FinishedTasks> finishedTasks) {
		this.finishedTasks = finishedTasks;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public boolean isBanned() {
		return isBanned;
	}

	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Group> getJoinedGroups() {
		return joinedGroups;
	}

	public void setJoinedGroups(List<Group> joinedGroups) {
		this.joinedGroups = joinedGroups;
	}

	public List<Group> getCreatedGroups() {
		return createdGroups;
	}

	public void setCreatedGroups(List<Group> createdGroups) {
		this.createdGroups = createdGroups;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Task> getTakenTasks() {
		return takenTasks;
	}

	public void setTakenTasks(List<Task> takenTasks) {
		this.takenTasks = takenTasks;
	}	
}
