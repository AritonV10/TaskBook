package taskbook.v1.platform.config;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import taskbook.v1.business.group.boundary.GroupResource;
import taskbook.v1.business.group.entity.unmarshals.GroupFormUnmarshal;
import taskbook.v1.business.group.entity.unmarshals.GroupInviteCodeUnmarshal;
import taskbook.v1.business.group.entity.unmarshals.SubGroupUnmarshal;
import taskbook.v1.business.task.entity.unmarshals.TaskUnmarshal;
import taskbook.v1.business.user.boundary.UserResource;
import taskbook.v1.business.user.entity.unmarshal.FormUserUnmarshal;
import taskbook.v1.business.user.entity.unmarshal.LoginCredentialsUnmarshal;
import taskbook.v1.platform.exceptions.ValidationException;
import taskbook.v1.platform.utility.Sets;
import taskbook.v1.platform.web.security.WebAutheticationFilter;

@ApplicationPath(value = "v1")
public class ApplicationConfig extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		return Sets.asSet(
				FormUserUnmarshal.class, UserResource.class, 
				ValidationException.class, LoginCredentialsUnmarshal.class,
				GroupFormUnmarshal.class,
				GroupInviteCodeUnmarshal.class,
				GroupResource.class,
				TaskUnmarshal.class,
				SubGroupUnmarshal.class,
				WebAutheticationFilter.class);
	}
}
