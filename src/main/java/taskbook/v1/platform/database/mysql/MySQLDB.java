package taskbook.v1.platform.database.mysql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import taskbook.v1.platform.database.entity.Database;
import taskbook.v1.platform.database.entity.Dialect;
import taskbook.v1.platform.utility.Files;

@MySQL
public class MySQLDB implements Database{
	
	private static final String NAME = "MySQL";
	
	@Inject
	private Files file;
	
	@Override   
	public Dialect getDialect() {
		return Dialect.MySQL;
	}
	
	@Override
	public DataSource getDataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		Map<Object, Object> properties = getProperties();
		dataSource.setPassword("Taskbook1330!");
		dataSource.setUser("taskbook");
		dataSource.setURL("jdbc:mysql://task_book_db:3306/task_book?autoReconnect=true");
		dataSource.setDatabaseName("task_book");
		try {
			dataSource.setMaxReconnects(20);
			dataSource.setUseSSL(false);
			dataSource.setCharacterEncoding("UTF-8");
			dataSource.setPortNumber(3306);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dataSource;
	}
	
	private Map<Object, Object> getProperties() {
		try {
			return this.file.getProperties("mysql_database.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyMap();
	}

	@Override
	public String name() {
		return NAME;
	}
}
