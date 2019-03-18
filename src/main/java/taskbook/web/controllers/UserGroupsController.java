package taskbook.web.controllers;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.View;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import taskbook.v1.business.algorithm.control.Algorithm;
import taskbook.v1.business.group.control.GroupStore;
import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.UserGroups;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.control.UserStore;
import taskbook.v1.business.user.entity.CurrentUser;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.utility.BinaryTree;

@Controller
@Produces(MediaType.TEXT_HTML)
@Path("u")
@RequestScoped
public class UserGroupsController {
	
	@Inject
	private Models model;
	
	@Inject @CurrentUser
	private User user;
	
	@Inject
	private UserStore userStore;
	
	@Inject
	private GroupStore groupStore;
	
	@Inject
	private Algorithm algorithm;
	
	@Inject @UserGroups
	private BinaryTree<Group> groups;
	
	@GET
	@View("communities.html")
	public void getPage() {
		System.out.println(groups.size());
		System.out.println(groups.toList().size());
		model.put("groups", groups.toList());
		model.put("userID", user.getTransientId());
	}
	
	@GET
	@Path("/{name}")
	public String getDashboard(@PathParam(value = "name") String name) {
		Group group = groups.get(new Group(name, null));
		groups
			.toList()
			.forEach(em -> System.out.println(em.getAdminID()));
		if(group != null) {
			System.out.println(user.getTransientId() + " " + group.getAdminID());
			boolean isAdministrator = group.getAdminID() == user.getTransientId();
			List<Task> userTasks = userStore.findTasksByUserIdAndGroupName(user.getTransientId(), name);
			Group group_ = groupStore.findSubgroupsByName(name);
			model.put("tasks", userTasks);
			model.put("group", group_);
			model.put("subgroups", group_.getSubGroups());
			model.put("isAdministrator", isAdministrator);
			return "dashboard.html";
		}
		return "redirect:/u";
	}
	
	@POST
	@Path("/logout")
	public String logout(@Context HttpServletRequest request) {
		try {
			request
				.logout();
			request
				.getSession()
				.invalidate();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return "login.html";
	}
}