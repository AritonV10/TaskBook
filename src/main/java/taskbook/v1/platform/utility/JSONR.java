package taskbook.v1.platform.utility;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


/**
 * 
 * @author vio
 * Utility class that returns {@link Response} objects
 */
public class JSONR {

	private static final String STATUS = "status";
	private static final String MESSAGE = "message";
	private static final String ERRORS = "errors";
	private static final String REDIRECT_URL = "redirect";
	public static final String HOST_NAME = "http://127.0.0.1:8080";
	
	private JsonObjectBuilder builder;
	
	JSONR() {
		this.builder = Json.createObjectBuilder();
	}
	
	public <T> JSONR add(final String key, T value) {
		builder.add(key, String.valueOf(value));
		return this;
	}
	
	public Response build() {
		return Response
				.ok()
				.entity(this.builder
				.build())
				.build();
	}
	
	public Response onSuccess(final String message) {
		successObject(message);
		return
				Response
				.ok()
				.entity(builder
						.build())
				.build();
	}
	
	public Response onSuccessRedirect(final String message, final String url) {
		successObject(message);
		return
				Response
				.ok()
				.entity(builder.add(REDIRECT_URL, url)
						.build()).build();
				
	}
	
	public Response onError(final List<String> errors) {
		return
				Response
				.status(Status.BAD_REQUEST)
				.entity(builder
						.add(STATUS, Status.BAD_REQUEST.getStatusCode())
						.add(ERRORS, toJsonArray(errors))
						.build())
				.build();
	}
	
	private void successObject(final String message) {
		builder
		.add(STATUS, Status.ACCEPTED.getStatusCode())
		.add(MESSAGE, message);
	}
	
	private JsonArray toJsonArray(final List<String> errors) {
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		errors
			.forEach((error) -> jsonArrayBuilder.add(error));
		return jsonArrayBuilder.build();
	}
}
