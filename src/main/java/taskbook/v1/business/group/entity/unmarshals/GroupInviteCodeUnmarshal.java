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

import taskbook.v1.business.group.entity.GroupInviteCode;
import taskbook.v1.platform.utility.JSON;

@Provider
public class GroupInviteCodeUnmarshal implements MessageBodyReader<GroupInviteCode>{

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return GroupInviteCode.class.isAssignableFrom(type);
	}

	@Override
	public GroupInviteCode readFrom(Class<GroupInviteCode> type, Type genericType,
			Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException, WebApplicationException {
		return JSON.deserialize(entityStream, GroupInviteCode.class);
	}

}
