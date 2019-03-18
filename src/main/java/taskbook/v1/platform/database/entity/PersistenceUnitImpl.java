package taskbook.v1.platform.database.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

public class PersistenceUnitImpl implements PersistenceUnitInfo {

	public static final String JPA_VERSION = "2.1";
	
	private PersistenceUnitTransactionType type = PersistenceUnitTransactionType.RESOURCE_LOCAL;
	
	private String persistenceUnitName;
	private DataSource jtaDataSource;
	private DataSource nonJtaDataSource;
	private List<String> mappedClasses;
	private List<String> mappingFileNames = new ArrayList<String>();
	private final Properties properties;
	
	public PersistenceUnitImpl(String persistenceUnitName, List<String> mappedClasses, Properties properties) {
		this.persistenceUnitName = persistenceUnitName;
		this.mappedClasses = mappedClasses;
		this.properties = properties;
	}

	public String getPersistenceUnitName() {
		return this.persistenceUnitName;
	}

	public String getPersistenceProviderClassName() {
		return HibernatePersistenceProvider.class.getName();
	}

	public PersistenceUnitTransactionType getTransactionType() {
		return this.type;
	}

	public DataSource getJtaDataSource() {
		return jtaDataSource;
	}

	public DataSource getNonJtaDataSource() {
		return nonJtaDataSource;
	}

	public List<String> getMappingFileNames() {
		return this.mappingFileNames;
	}

	public List<URL> getJarFileUrls() {
		return Collections.emptyList();
	}

	public URL getPersistenceUnitRootUrl() {
		return null;
	}

	public List<String> getManagedClassNames() {
		return this.mappedClasses;
	}

	public boolean excludeUnlistedClasses() {
		return false;
	}

	public SharedCacheMode getSharedCacheMode() {
		return SharedCacheMode.UNSPECIFIED;
	}

	public ValidationMode getValidationMode() {
		return ValidationMode.AUTO;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public String getPersistenceXMLSchemaVersion() {
		return JPA_VERSION;
	}

	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public void addTransformer(ClassTransformer transformer) {
		
	}

	public ClassLoader getNewTempClassLoader() {
		return null;
	}

}
