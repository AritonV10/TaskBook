package taskbook.v1.business.socket.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import taskbook.v1.platform.utility.Sets;

@ApplicationScoped
@ServerEndpoint(value = "/taskbook_socket", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class SocketEndpoint {
	
	private static final Map<String, Set<Session>> userAddr
		= new ConcurrentHashMap<>();
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		String source = session
				.getQueryString()
				.split("=")[1];
		if(userAddr.containsKey(source)) {
			userAddr.get(source)
				.remove(session);
			if(userAddr.get(source).isEmpty()) {
				userAddr.remove(source);
			}
		}
	}
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		String source = session
					.getQueryString()
					.split("=")[1];
		if(userAddr.containsKey(source)) {
			userAddr.get(source)
				.add(session);
		} else {
			userAddr.put(source, Sets.asSet(session));
		}
	}
	 
	@OnMessage
	public void onMessage(String message) {
		userAddr
		.get(getSource(message))
		.forEach((session) -> {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public void event(@Observes @SocketPayload Payload payload) {
		onMessage(payload.toString());
	}
	
	private String split(String URI) {
		return
			URI.substring(URI.lastIndexOf("/") + 1, URI.length());
	}
	
	private String getSource(String json) {
		try(JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes()))) {
			return reader.readObject()
					.getString("source");
		}
	}
	
	
}
