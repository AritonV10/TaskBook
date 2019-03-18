package taskbook.v1.business.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.business.task.entity.FinishedTasks;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.database.entity.AbstractConnectionManager;
import taskbook.v1.platform.database.mysql.MySQLDB;
import taskbook.v1.platform.utility.Files;


@RunWith(MockitoJUnitRunner.class)
public class UserTest extends AbstractConnectionManager{
	
	private EntityManager manager;
	
	@InjectMocks
	private MySQLDB database;
	
	@Mock
	private Files file;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(file);
		MockitoAnnotations.initMocks(database);
		super.database = this.database;
		this.manager = super.bootstrap().createEntityManager();
	}
	
	@After
	public void after() {
		super.closeEntityManagerFactory();
	}
	
	@Test
	public void notNull() {
		assertNotNull(this.manager);
	}
	
	@Test
	public void persistenceTest() {
		
		User user = new User();
		user.setFirstName("Ariton");
		user.setLastName("Vio");
		user.setPassword("123");
		user.setEmail("email");
		manager.getTransaction().begin();
		manager.persist(user);
		manager.getTransaction().commit();
		manager.close();
	}
	
	@Test @Ignore
	public void getByIdTest() {
		Object s = this.manager
				.createNativeQuery("SELECT firstname FROM users AS u"
						+ "WHERE u.id = :id")
				.setParameter("id", 1)
				.getSingleResult();
		this.manager.close();
		assertEquals("Ariton", String.valueOf(s));
	}

	@Override
	protected Class<?>[] mappedClasses() {
		Class<?>[] clasz = { User.class, FinishedTasks.class, Task.class, Group.class, SubGroup.class};
		return clasz;
	}
}
