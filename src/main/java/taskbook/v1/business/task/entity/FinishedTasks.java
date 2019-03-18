package taskbook.v1.business.task.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.entity.AbstractObject;

@Entity
@Table(name = "finished_tasks")
public class FinishedTasks extends AbstractObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private User user;
	@Column(name = "date_finished")
	private LocalDate finishedDate;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LocalDate getFinishedDate() {
		return finishedDate;
	}
	public void setFinishedDate(LocalDate finishedDate) {
		this.finishedDate = finishedDate;
	}
	
	
	
}
