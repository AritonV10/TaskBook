package taskbook.v1.business.socket.control;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class SocketEvent {
	
	@Inject @SocketPayload
	private Event<Payload> event;
	
	public void fire(final Payload payload) {
		event.fire(payload);
	}
}
