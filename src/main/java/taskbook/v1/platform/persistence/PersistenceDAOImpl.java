package taskbook.v1.platform.persistence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.business.task.entity.FinishedTasks;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.database.entity.AbstractConnectionManager;

public class PersistenceDAOImpl extends AbstractConnectionManager implements PersistenceDAO {
	
	@Override
	protected Class<?>[] mappedClasses() {
		Class<?>[] clasz = {User.class, FinishedTasks.class, Group.class, SubGroup.class, Task.class};
		return clasz;
	}
	
	public <T> Optional<T> find(Class<T> clasz, Function<EntityManager, T> bifunction) {
		EntityManager entityManager = null;
		T object = null;
		try {
			entityManager = getEntityManager();
			object = bifunction.apply(entityManager);
		} finally {
			entityManager.close();
		}
		return Optional.ofNullable(object);
	}

	public <T> List<T> findAll(final Class<?> clasz, final Function<EntityManager, List<T>> bifunction) {
		EntityManager entityManager = null;
		List<T> objects = Collections.emptyList();
		try {
			entityManager = getEntityManager();
			objects = bifunction.apply(entityManager);
		} finally {
			if(entityManager.isOpen()) {
				entityManager.close();
			}
		}
		return objects;
	}
	
	
	public void save(final Class<?> clasz, final Consumer<EntityManager> consumer) {
		EntityManager entityManager = null;
		try {
			entityManager = getEntityManager();
			entityManager.getTransaction().begin();
			consumer.accept(entityManager);
			entityManager.getTransaction().commit();
		} finally {
			if(entityManager.isOpen() && entityManager != null) {
				entityManager.close();			}
		}
	}
	
	private EntityManager getEntityManager() {
		return super.getEntityManagerFactory().createEntityManager();
	}

	@Override
	public boolean merge(Class<?> clasz, Function<EntityManager, Boolean> bifunction) {
		EntityManager entityManager = null;
		boolean ok;
		try {
			entityManager = getEntityManager();
			entityManager.getTransaction().begin();
			ok = bifunction.apply(entityManager);
			entityManager.getTransaction().commit();
		} finally {
			if(entityManager.isOpen() && entityManager != null) {
				entityManager.close();			}
		}
		return ok;
	}
	
}
