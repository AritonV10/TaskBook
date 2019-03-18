package taskbook.v1.platform.utility;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import taskbook.v1.business.task.entity.Difficulty;
import taskbook.v1.business.task.entity.Priority;
import taskbook.v1.business.task.entity.Status;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.entity.FormUser;
import taskbook.v1.business.user.entity.LoginCredentials;

@RunWith(JUnit4.class)
public class JSONTest {

	@Test
	public void deserialize() {
		try(InputStream stream = new ByteArrayInputStream("{\"email\":\"vio\",\"firstName\":\"Ariton\", \"lastName\":\"Vio\",\"password\":\"password\",\"matchedPassword\":\"matchedPassword\"}".getBytes(StandardCharsets.UTF_8))) {
			FormUser c = JSON.deserialize(stream, FormUser.class);
			assertEquals("Ariton", c.getFirstName());
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	  
	@Test
	public void serialize() {
		LoginCredentials login = new LoginCredentials("email", "password");
		String s = JSON.serialize(login).toString();
		assertEquals("{\"password\":\"password\",\"email\":\"email\"}", s);
	}
	// Integer id, final LocalDate deadline, 
	// final String description, final Difficulty difficulty, 
	// final Status status, final Priority priority, final String category
	@Test
	public void serializeList() {
		List<Task> tasks = Arrays.asList(
				new Task(1, LocalDate.now(), 
						"Description1", Difficulty.EASY, 
						Status.TAKEN, Priority.HIGH, "category1"));
		String s = JSON.serializeList(tasks).toString();
		System.out.println(s);
		
	}
	
}
