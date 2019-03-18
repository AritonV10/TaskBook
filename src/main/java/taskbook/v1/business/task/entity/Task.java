package taskbook.v1.business.task.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.entity.AbstractObject;
import taskbook.v1.platform.utility.JSONSkip;

@Entity
@Table(name = "tasks")
public class Task extends AbstractObject {
	
	@JSONSkip
	private static final long serialVersionUID = 720763252957390025L;
	
	@Transient
	@NotEmpty(message = "Invalid Category")
	private String category;
	
	@Transient @JSONSkip
	private Integer userID;
	
	@Transient
	private Integer transientID;
	
	@NotBlank(message = "Description field can't be empty")
	private String description;
	
	@Future(message = "Invalid Deadline")
	private LocalDate deadline;
	
	@ManyToOne @JSONSkip
	private SubGroup subGroup;
	
	@ManyToOne @JSONSkip
	private User user;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	// In case the options are modified
	//@ValidEnum(enumClass = Priority.class, message = "Invalid Priority option")
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	//@ValidEnum(enumClass = Difficulty.class, message = "Invalid Difficulty option")
	@Enumerated(EnumType.STRING)
	private Difficulty difficulty;
	
	public Task(
			final String category, 
			final Integer id, final String description, final LocalDate deadline,
			final SubGroup subGroup, final User user,
			final Status status, final Priority priority, final Difficulty difficulty) {
		this.category = category;
		super.setId(id);
		this.description = description;
		this.deadline = deadline;
		this.subGroup = subGroup;
		this.user = user;
		this.status = status;
		this.priority = priority;
		this.difficulty = difficulty;
	}
	
	public Task(String category, Difficulty difficulty, Priority priority, String description, LocalDate deadline) {
		this(category, null, description, deadline, null, null, Status.NOT_TAKEN, priority, difficulty);
	}
	
	public Task(Integer id, final LocalDate deadline, 
			final String description, final Difficulty difficulty, 
			final Status status, final Priority priority, final String category) {
		this(category, null, description, deadline, null, null, status, priority, difficulty);
		this.transientID = id;
	}
	
	public Task(final String description, final Integer id, final String difficulty, String category) {
		this(category, Difficulty.value(difficulty), null, description, null);
		super.setId(id);
	}
	
	public Task(String description) {
		this.description = description;
	}
	
	public Task(Integer userID) {
		this.userID = userID;
	}
	
	public void setTransientID(final Integer ID) {
		this.transientID = ID;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public Task() {}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getTransientSubgroupName() {
		return this.category;
	}
	
	public void setDifficulty(final Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public Integer getTransientUserID() {
		return this.userID;
	}
	
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	public SubGroup getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	
	
}
