package taskbook.web.security;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;

import taskbook.v1.platform.utility.Sets;

@Provider
public class WebSecurityFilter implements ContainerRequestFilter{
	private static final Set<String> authPaths =
			Sets.asSet("/*");
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getSecurityContext().getUserPrincipal() == null) {
			requestContext
				.abortWith(Response.temporaryRedirect(UriBuilder.fromUri("127.0.0.1:8080/login.html").build()).build());
		}
	}
}
