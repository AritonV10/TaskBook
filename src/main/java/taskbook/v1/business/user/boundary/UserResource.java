package taskbook.v1.business.user.boundary;
 
import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import taskbook.v1.business.algorithm.control.Algorithm;
import taskbook.v1.business.group.control.GroupStore;
import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.GroupForm;
import taskbook.v1.business.group.entity.GroupInviteCode;
import taskbook.v1.business.group.entity.UserGroups;
import taskbook.v1.business.user.control.UserStore;
import taskbook.v1.business.user.entity.AuthEvent;
import taskbook.v1.business.user.entity.CurrentUser;
import taskbook.v1.business.user.entity.FormUser;
import taskbook.v1.business.user.entity.LoginCredentials;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.utility.BinaryTree;
import taskbook.v1.platform.utility.JSONR;



@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource {
	
	@Inject
	private JSONR jsonResponse;
	
	@Inject
	private GroupStore groupStore;
	
	@Inject 
	private UserStore userStore;
	
	@Inject
	private SecurityContext context;
	
	@Inject @AuthEvent
	private Event<User> user;
	
	@Inject @CurrentUser
	private User currentUser;
	
	@Inject @UserGroups
	private BinaryTree<Group> groups;
	
	@GET
	@Path("logged")
	@Produces(MediaType.TEXT_HTML)
	public String currentUser() {
		return String.format("Hello %s", currentUser.getEmail());
	}
	
	/**
	 * Registers a {@link User} if the {@link FormUser} has passed the validation process
	 * @param user the {@link User} to be saved
	 * @return	{@link Response}
	 */
	@POST
	@Path("/")
	public Response register(@Valid final FormUser user) {
		userStore.register(user);
		return jsonResponse
				.onSuccess("Account successfully created. You will be redirected shortly");
	}
	
	/**
	 * Authenticates a client and notifies the container about their authentication if success
	 * If success, it puts the client's {@link User} object in session
	 * @param request
	 * @param response
	 * @param loginCredentials	the client's credentials
	 * @return
	 */
	@Path("auth")
	@POST
	public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response, final LoginCredentials loginCredentials) {
		if(context.getCallerPrincipal() != null) {
			return jsonResponse
					.onError(Arrays.asList("You are already signed in"));
		}
		AuthenticationStatus authStatus = context
				.authenticate(request, response, AuthenticationParameters
						.withParams()
						.credential(loginCredentials.getCredentials())
					);
		if(authStatus == AuthenticationStatus.SUCCESS) {
			User user = userStore
						.findByEmail(loginCredentials.getEmail());
			this.user.fire(user);
			return jsonResponse
					.onSuccessRedirect("You have successfully signed in. You will be redirected shortly", String.format("%s/en/u", JSONR.HOST_NAME));
		}
		return jsonResponse
				.onError(Arrays.asList("Invalid email or password"));
	}
	
	/**
	 * Creates a {@link Group} object if {@link GroupForm} passed the validation process
	 * @param groupForm	the object to be saved
	 * @return {@link Response}
	 */
	@Path("user/groups")
	@POST
	public Response createGroup(@Valid GroupForm groupForm) {
		Group group = new Group();
		group.setName(groupForm.getName());
		System.out.println(groups.size());
		if(groups.get(new Group(group.getName(), null)) != null) {
			return
					jsonResponse
					.onError(Arrays.asList(String.format("You have already created %s", group.getName())));
		}
		userStore
			.addGroup(group, currentUser.getTransientId());
		return jsonResponse
				.onSuccess(String.format("%s successfully created. You will be redirected in a second", groupForm.getName()));
	}
	
	/**
	 * Join a {@link Group} based on a {@link GroupInviteCode}
	 * If the code is valid, the {@link User} is added to the {@link Group} 
	 * @param code
	 * @return
	 */
	@Path("user/groups")
	@PUT
	public Response joinGroup(@Valid final GroupInviteCode code) {
		
		// searching for the group based on It's invite code
		Group group
			= groupStore
				.findGroupByInviteCode(code.getInviteCode());
		if(group != null) {
			Group g = groups.get(new Group(group.getName(), null));
			if(g != null) {
				return jsonResponse
						.onError(Arrays.asList(String.format("You are already in %s", group.getName())));
			} else {
				userStore.joinGroup(group.getTransientID(), currentUser.getTransientId());
				return jsonResponse
						.onSuccess(String.format("You have successfully joined %s", group.getName()));
			}
			
		}
		return jsonResponse
				.onError(Arrays.asList("Invalid invite code"));
	}
}
