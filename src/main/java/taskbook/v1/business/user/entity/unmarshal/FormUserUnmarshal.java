package taskbook.v1.business.user.entity.unmarshal;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import taskbook.v1.business.user.entity.FormUser;
import taskbook.v1.platform.utility.JSON;

@Provider
public class FormUserUnmarshal implements MessageBodyReader<FormUser>{

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return FormUser.class.isAssignableFrom(type);
	}

	@Override
	public FormUser readFrom(Class<FormUser> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		FormUser user = JSON.deserialize(entityStream, FormUser.class);
		return user;
	}

}
