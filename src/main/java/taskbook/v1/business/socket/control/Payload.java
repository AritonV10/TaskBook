package taskbook.v1.business.socket.control;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class Payload {
	
	public static final String ADD = "Add";
	public static final String UPDATE = "Update";
	
	private String action;
	private String target;
	private String source;
	private String message;
	private String payload;
	private JsonObjectBuilder builder = Json.createObjectBuilder();
	
	public Payload(String action, String source, String message, String target, String payload) {
		this.action = action;
		this.source = source;
		this.message = message;
		this.target = target;
		this.payload = payload;
		builder.add("action", action)
		.add("source", source)
		.add("message", message)
		.add("target", target)
		.add("payload", payload);
		
	}

	public String toString() {
		return builder.build().toString();
	}
	
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getPayload() {
		return payload;
	}
}
