package taskbook.v1.business.user.control;

import java.util.List;
import java.util.Optional;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.entity.FormUser;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.persistence.JPAHelperMethods;

public interface UserStore extends JPAHelperMethods {
	
	/**
	 * Find a User by It's ID
	 * @param id
	 * @return User
	 */
	User findbyId(final Integer id);
	
	/**
	 * Save a {@link User} entity
	 * @param user
	 */
	void save(final User user);
	
	/**
	 * Check if passwords match
	 * @param actualPassword 
	 * @param toCheck
	 */
	boolean login(String actualPassword, String toCheck);
	
	/**
	 * Return a {@link Optional} of {@link User} if the {@link User} exists
	 * Else returns null
	 * @param email
	 * @return User
	 */
	Optional<User> getCredentialsForLogin(final String email);
	
	/**
	 * Register a User
	 * @param user
	 * @return the detached registered {@link User}
	 */
	User register(final FormUser user);
	
	/**
	 * Checks if the email exists
	 * Used for validation
	 * @param email
	 * @return if the email exists or not
	 */
	String existsEmail(final String email);
	
	/**
	 * Find a {@link User} entity by It's {@value value}
	 * @param email
	 * @return {@User}
	 */
	User findByEmail(final String email);
	
	/**
	 * Update an existing {@link User} entity
	 * @param user
	 */
	void merge(final User user);
	
	/**
	 * Find {@link Task} objects that are taken by the user by their
	 * {@value userID} and {@value groupName}
	 * @param userId	the user's ID
	 * @param groupName	the group name
	 * @return	{@link List} of {@link Task}
	 */
	List<Task> findTasksByUserIdAndGroupName(final Integer userId, final String groupName);
	
	public boolean addTask(final Integer taskID, final String groupName, final Integer userID);
	public boolean returnTask(final Integer taskID, final Integer userID);
	public boolean finishTask(final Integer taskID, final Integer userID);
	public void addGroup(final Group group, final Integer userID);
	void joinGroup(final Integer groupID, final Integer userID);
}
