package taskbook.v1.business.user.control;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.hibernate.Session;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.security.control.PasswordStore;
import taskbook.v1.business.task.control.TaskStore;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.entity.FormUser;
import taskbook.v1.business.user.entity.Role;
import taskbook.v1.business.user.entity.Salt;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.persistence.PersistenceDAO;


@Stateless
public class UserStoreImpl implements UserStore {

	
	@Inject
	private PersistenceDAO persistence;
	
	@Inject
	private PasswordStore passwordStore;
	
	@Inject
	private TaskStore store;
	
	@Override
	public User findbyId(Integer id) {
		return null;
	}
    
	@Override
	public void save(final User user) {
		this.persistence
			.save(User.class, (entityManager) -> {
				entityManager
					.persist(user);
			});
			
	}
	
	@Override
	public Optional<User> getCredentialsForLogin(final String email) {
		return this.persistence
			.find(User.class, (entityManager) -> {
				return getSingleResult(entityManager
						.createQuery(
								"SELECT new taskbook.v1.business.user.entity.User(u.password, u.role, u.isBanned) FROM User AS u " + 
						"WHERE u.email = :email", User.class)
						.setParameter("email", email));	
			});
	}
	
	@Override
	public boolean login(String actualPassword, String toCheck) {
		return passwordStore.isMatch(actualPassword, toCheck);
	} 

	@Override
	public String existsEmail(String email) {
		return this.persistence
						.find(String.class, (entityManager) -> { 
							return getSingleResult(entityManager
								.createNativeQuery("SELECT email FROM users AS u"
										+ " WHERE u.email = :email")
										.setParameter("email", email));
						}).orElse(null);
	}

	@Override
	public void merge(final User user) {
		this.persistence
			.save(User.class, entityManager -> {
				entityManager.unwrap(Session.class)
					.update(user);
			});
		
	}
	
	@Override
	public User findByEmail(String email) {
		User user = this.persistence
				.find(User.class, (entityManager) -> { 
					List<Object[]> ob = entityManager
						.createNativeQuery("SELECT u.id, u.email FROM users AS u WHERE u.email = :email")
						.setParameter("email", email)
						.getResultList();
					for(Object[] obj : ob) {
						return new User((Integer) obj[0], (String) obj[1]);
					}
					return null;
				}).orElse(null);
		return user;
	}
	
	@Override
	public void joinGroup(final Integer groupID, final Integer userID) {
		this.persistence
		.save(User.class, em -> {
			em.createNativeQuery("INSERT INTO group_members(group_id, user_id) VALUES (?, ?)")
			.setParameter(1, groupID)
			.setParameter(2, userID)
			.executeUpdate();
		});
	}
	
	@Override
	public boolean returnTask(final Integer taskID, final Integer userID) {
		return 
				persistence.merge(Task.class, em -> {
					return
							em.createNativeQuery("UPDATE tasks SET user_id = NULL, status = 'Not Taken' WHERE id = :id AND user_id = :userID")
							.setParameter("id", taskID)
							.setParameter("userID", userID)
							.executeUpdate() == 1;
				});
	}

	@Override
	public User register(FormUser user) {
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setBanned(false);
		newUser.setRank(Role.USER);
		try {
			newUser.setPassword(passwordStore.hashPassword(user.getPassword(), Salt.SALT));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		save(newUser);
		return newUser;
	}

	@Override
	public List<Task> findTasksByUserIdAndGroupName(Integer userId, String groupName) {
		return
				persistence
				.findAll(Task.class, em -> {
					@SuppressWarnings("unchecked")
					List<Object[]> objects
						= em.unwrap(Session.class).createNativeQuery(
								"SELECT t.description, t.id, t.difficulty, s.name FROM tasks AS t "
								+ "INNER JOIN sub_groups AS s ON s.id = t.subGroup_id "
								+ "INNER JOIN groups AS g ON g.id = s.group_id "
								+ "WHERE t.user_id = :id AND g.name = :name")
						.setParameter("id", userId)
						.setParameter("name", groupName)
						.getResultList();
					List<Task> tasks = new ArrayList<>();
					for(Object[] arr : objects) {
						tasks.add(new Task((String) arr[0], (Integer) arr[1], (String) arr[2], (String) arr[3]));
					}
					return tasks;
				});
	}

	@Override
	public boolean addTask(Integer taskID, String groupName, Integer userID) {
		return this.persistence
				.merge(Task.class, em -> {
					return em
						.createNativeQuery(
								"UPDATE tasks SET status = 'Taken', user_id = :userID "
								+ "WHERE id = "
								+ "("
								+ "SELECT t1.id FROM (SELECT t2.id, t2.subGroup_id, t2.user_id, t2.status FROM tasks AS t2) AS t1 "
								+ "INNER JOIN sub_groups AS s ON s.id = t1.subGroup_id "
								+ "INNER JOIN groups AS g ON g.id = s.group_id "
								+ "WHERE t1.id = :id AND g.name = :name AND t1.user_id IS NULL AND t1.status = 'Not Taken'"
								+ ")")
						.setParameter("userID", userID)
						.setParameter("id", taskID)
						.setParameter("name", groupName)
						.executeUpdate() == 1;
				});
	}
	
	public boolean finishTask(final Integer taskID, final Integer userID) {
		return persistence
				.merge(Task.class, em -> {
						em.createNativeQuery(
								"INSERT INTO finished_tasks(date_updated, date_created, date_finished, user_id) "
								+ "SELECT CURDATE(), CURDATE(), CURDATE(), user_id FROM tasks AS t "
								+ "WHERE t.id = :id AND t.user_id = :userID")
						.setParameter("id", taskID)
						.setParameter("userID", userID)
						.executeUpdate();
						return em
							.createNativeQuery("DELETE FROM tasks WHERE tasks.id = :id AND tasks.user_id = :userID")
							.setParameter("id", taskID)
							.setParameter("userID", userID)
							.executeUpdate() == 1;
				});
	}

	@Override
	public void addGroup(Group group, Integer userID) {
		this.persistence
		.save(Group.class, em -> {
			em
				.createNativeQuery("INSERT INTO groups(date_created, date_updated, name, invite_code, administrator_id) "
						+ "VALUES (CURDATE(), CURDATE(), ?, ?, ?)")
				.setParameter(1, group.getName())
				.setParameter(2, group.generateInviteCode())
				.setParameter(3, userID)
				.executeUpdate();
		});
		
	}
	
}
