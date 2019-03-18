package taskbook.v1.platform.web.security;

import java.io.IOException;
import java.util.Set;

import javax.json.Json;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import taskbook.v1.platform.utility.Sets;

@Provider
public class WebAutheticationFilter implements ContainerRequestFilter {

	private static final Set<String> authPaths =
			Sets.asSet("/users", "/users/auth");
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getSecurityContext().getUserPrincipal() == null) {
			if(!authPaths.contains(requestContext
				.getUriInfo()
				.getPath().toString())) {
				requestContext.abortWith(
						Response.status(Status.UNAUTHORIZED)
						.entity(Json.createObjectBuilder().add("status", Status.UNAUTHORIZED.getStatusCode()).add("error", "Not authorized"))
						.build());
			}
		}
		
	}
}
