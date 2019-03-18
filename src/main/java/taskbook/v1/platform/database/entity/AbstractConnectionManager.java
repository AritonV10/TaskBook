package taskbook.v1.platform.database.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import taskbook.v1.platform.annotations.GuardedBy;
import taskbook.v1.platform.annotations.Logger;
import taskbook.v1.platform.database.mysql.MySQL;


public abstract class AbstractConnectionManager {
	
	
	@GuardedBy(guardian = "this")
	private static EntityManagerFactory entityManagerFactory = null;
	
	@Inject @Logger
	private org.jboss.logging.Logger LOG;
	
	@Inject @MySQL
	protected Database database;
	
	
	public EntityManagerFactory getEntityManagerFactory() {
		if(entityManagerFactory == null) {
			entityManagerFactory = bootstrap();
			
		}
		return entityManagerFactory;
	}
	
	protected abstract Class<?>[] mappedClasses();
	
	public EntityManagerFactory bootstrap() {
		if(entityManagerFactory == null) {
			synchronized (AbstractConnectionManager.class) {
				if(entityManagerFactory == null) {
					LOG.infof("Connecting %s", this.database.name());
					PersistenceUnitInfo persistenceUnitInfo = buildPersistenceUnit(getClass().getName());
					Map<String, Object> configuration = new HashMap<>();
					Integrator integrator = getIntegrator();
					if(integrator != null) {
						configuration.put("hibernate.integrator.provider", (IntegratorProvider)() -> {
							return Collections.singletonList(integrator);
						});
					}
					return new HibernatePersistenceProvider()
							.createContainerEntityManagerFactory(persistenceUnitInfo, configuration);
				}
			}
		}
		return entityManagerFactory;
	}
	
	public static void closeEntityManagerFactory() {
		if(entityManagerFactory != null && entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}
	}
	
	public List<String> getMappedClasses() {
		return Arrays.asList(mappedClasses())
				.stream()
				.map(Class::getName)
				.collect(Collectors.toList());
	}
	
	protected Integrator getIntegrator() {
		return null;
	}

	protected PersistenceUnitInfo buildPersistenceUnit(String name) {
		return new PersistenceUnitImpl(name, getMappedClasses(), getProperties());
	}
	
	protected Properties getProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", this.database.getDialect().toString());
		properties.put("hibernate.connection.datasource", this.database.getDataSource());
		
		//TODO: change after testing
		properties.put("hibernate.hbm2ddl.auto", "create-drop");
		properties.put("hibernate.generate_statistics", Boolean.TRUE.toString());
		return properties;
	}
}
