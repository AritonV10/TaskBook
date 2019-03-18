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

import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.platform.utility.JSON;

@Provider
public class SubGroupUnmarshal implements MessageBodyReader<SubGroup>{
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return SubGroup.class.isAssignableFrom(type);
	}
	
	@Override
	public SubGroup readFrom(Class<SubGroup> arg0, Type arg1, Annotation[] arg2, MediaType arg3,
			MultivaluedMap<String, String> arg4, InputStream arg5) throws IOException, WebApplicationException {
		return JSON.deserialize(arg5, SubGroup.class, new Class<?>[] {String.class});
	}
}
