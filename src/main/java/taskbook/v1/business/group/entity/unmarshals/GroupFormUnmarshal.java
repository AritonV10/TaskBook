package taskbook.v1.business.group.entity.unmarshals;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import taskbook.v1.business.group.entity.GroupForm;
import taskbook.v1.platform.utility.JSON;

@Provider
public class GroupFormUnmarshal implements MessageBodyReader<GroupForm>{

	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return GroupForm.class.isAssignableFrom(type);
	}
	
	@Override
	public GroupForm readFrom(Class<GroupForm> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		return JSON.deserialize(entityStream, GroupForm.class);
	}
}
