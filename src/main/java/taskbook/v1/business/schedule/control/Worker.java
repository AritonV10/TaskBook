package taskbook.v1.business.schedule.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import taskbook.v1.platform.persistence.PersistenceDAO;


public class Worker {
/*
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	@Inject
	private PersistenceDAO dao;
	
	@PostConstruct
	public void init() {
		executor
			.schedule(new Runnable() {
				
				@Override
				public void run() {
					dao
					.merge(null, em -> {
						em.createNativeQuery("DELETE FROM tasks WHERE deadline = CURDATE()")
						.executeUpdate();
						return true;
					});
				}
				
			}, 24, TimeUnit.HOURS);
	}
	*/
}
