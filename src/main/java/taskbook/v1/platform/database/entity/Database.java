package taskbook.v1.platform.database.entity;

import javax.sql.DataSource;

public interface Database {
	
	/**
	 * The {@link Dialect} used by the database
	 * @return {@link Dialect}
	 */
	Dialect getDialect();
	
	/**
	 * A {@link DataSource} object containing information about the database
	 * (how to connect)
	 * @return {@link DataSource}
	 */
	DataSource getDataSource();
	
	/**
	 * The database' name - used for logging mostly
	 * @return {@link String} database name
	 */
	String name();
}
