package taskbook.v1.business.group.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import taskbook.v1.business.task.entity.Task;
import taskbook.v1.platform.entity.AbstractObject;

@Entity
@Table(name = "sub_groups")
public class SubGroup extends AbstractObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	@ManyToOne
	private Group group;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();
	
	public SubGroup(String name) {
		this.name = name;
	}
	
	public SubGroup() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Group getGroups() {
		return group;
	}
	public void setGroups(Group groups) {
		this.group = groups;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	
}
