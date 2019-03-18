package taskbook.v1.platform.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FilesTest {
	
	
	private Files file;
	
	@Before
    public void init() {
		this.file = new Files();
	}
	
	@Test
	public void isNull() {
		assertNotNull(this.file);
	}
	
	@Test @Ignore
	public void isNotNull() {
		assertNull(this.file);
	}
	
	@Test
	public void getProperties() throws FileNotFoundException, IOException {
		Map<Object, Object> properties = file.getProperties("mysql_database.properties");
		assertEquals(String.valueOf(properties.get("mysql.db")), "task_book");
	}
	
}
