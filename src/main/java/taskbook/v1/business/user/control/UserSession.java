package taskbook.v1.business.user.control;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

import taskbook.v1.business.user.entity.AuthEvent;
import taskbook.v1.business.user.entity.CurrentUser;
import taskbook.v1.business.user.entity.User;

@SessionScoped
public class UserSession implements Serializable {

	
	private User user;
	
	@Produces @CurrentUser
	public User userProducer() {
		if(user != null) {
			return this.user;
		}
		return new User();
	}
	
	public void userEvent(@Observes @AuthEvent User user) {
		this.user = user;
	}
}
