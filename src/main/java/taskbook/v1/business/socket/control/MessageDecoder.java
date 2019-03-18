package taskbook.v1.business.socket.control;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<String>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String decode(String s) throws DecodeException {
		return s;
	}

	@Override
	public boolean willDecode(String s) {
		return s != null;
	}

}
