package taskbook.v1.platform.persistence;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.security.control.PasswordStore;
import taskbook.v1.business.user.entity.Role;
import taskbook.v1.business.user.entity.Salt;
import taskbook.v1.business.user.entity.User;

@WebListener
public class BootstrapData implements ServletContextListener {

	
	private @Inject PersistenceDAO dao;
	
	@Inject
	private PasswordStore pw;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		User user = new User();
		user.setEmail("t@gmail.com");
		user.setBanned(false);
		user.setRole(Role.USER);
		try {
			user.setPassword(pw.hashPassword("t1330!", Salt.SALT));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Group group = new Group();
		group.setName("UVT");
		group.setAdministrator(user);
		user.getCreatedGroups().add(group);
		
		
		dao.save(User.class, em -> {
			em.persist(user);
		});
	}
}
