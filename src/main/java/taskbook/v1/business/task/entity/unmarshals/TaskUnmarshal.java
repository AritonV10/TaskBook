package taskbook.v1.business.task.entity.unmarshals;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import taskbook.v1.business.task.entity.Difficulty;
import taskbook.v1.business.task.entity.Priority;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.platform.validation.control.ManualValidationService;

@Provider
public class TaskUnmarshal implements MessageBodyReader<Task>{

	@Inject
	private ManualValidationService service;
	
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Task.class.isAssignableFrom(type);
	}

	@Override
	public Task readFrom(Class<Task> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		try(JsonReader reader = Json.createReader(entityStream)) {
			JsonObject object = reader.readObject();
			return new
					Task(object.getString("category"),
							Difficulty.value(object.getString("difficulty")),
							Priority.value(object.getString("priority")),
							object.getString("description"),
							LocalDate.parse(object.getString("deadline", LocalDate.now().minusMonths(-1).toString())));
		}
	}

}
