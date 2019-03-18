package taskbook.web.config;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import taskbook.v1.platform.utility.Sets;
import taskbook.web.controllers.UserGroupsController;
import taskbook.web.security.WebSecurityFilter;

@ApplicationPath(value = "/en")
public class ApplicationControllersConfig extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
		return
				Sets.asSet(
						UserGroupsController.class,
						WebSecurityFilter.class);
	}
}
