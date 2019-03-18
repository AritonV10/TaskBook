package taskbook.v1.business.group.boundary;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import taskbook.v1.business.algorithm.control.Algorithm;
import taskbook.v1.business.group.control.GroupStore;
import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.business.group.entity.UserGroups;
import taskbook.v1.business.socket.control.Payload;
import taskbook.v1.business.socket.control.SocketEvent;
import taskbook.v1.business.task.control.TaskStore;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.business.user.control.UserStore;
import taskbook.v1.business.user.entity.CurrentUser;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.utility.BinaryTree;
import taskbook.v1.platform.utility.JSON;
import taskbook.v1.platform.utility.JSONR;
import taskbook.v1.platform.utility.UniqueBinaryTree;


//TODO: use AOP so that I don't have to check in every method if the user
// 		contains the group or not
@Path("groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class GroupResource {
	
	@Inject @CurrentUser
	private User user;
	
	@Inject @UserGroups
	private BinaryTree<Group> groups;
	
	@Inject
	private GroupStore groupStore;
	
	@Inject
	private UserStore userStore;
	
	@Inject
	private TaskStore taskStore;
	
	@Inject
	private Algorithm algorithm;
	
	@Inject
	private JSONR json;
	
	@Inject
	private SocketEvent event;
	
	
	
	private static final BinaryTree<String> availableSortingOrders
		= UniqueBinaryTree.asTree("asc", "desc");
	
	/**
	 * Creating a subgroup
	 * @param name		the group's name
	 * @param subGroup	the subgroup to be added
	 * @return	{@link Response}	
	 */
	@Path("{name}/subgroup")
	@POST
	public Response createSubgroup(@PathParam(value = "name") String name, @Valid SubGroup subGroup) {
		Group group = groups.get(new Group(name, null)); 
		if(group == null) {
			return
					json.onError(Arrays.asList("Invalid action"));
		} else if(groupStore.findSubgroupByGroupName(name, subGroup.getName()).getName() != null) {
			return json
					.onError(Arrays.asList(String.format("%s already exists in your group", subGroup.getName())));
		}
		groupStore.saveSubGroup(subGroup, group.getTransientID());
		return
				json
					.onSuccess(String.format("%s successfully added", subGroup.getName()));
	}
	
	/**
	 * Saving a task
	 * @param name	the group's name
	 * @param task	the task to be saved
	 * @return {@link Response}
	 */
	@Path("{name}/subgroup/task")
	@POST
	public Response createTask(@PathParam(value = "name") String name, Task task) {
		// if there has been no problems saving the task
		// then we return success, else fail
		boolean ok = taskStore
			.save(task, groups.get(new Group(name, null)).getTransientID());
		
		if(ok) {
			task.setTransientID(taskStore.selectIdOfLatestTaskAdded(task.getCategory()));
			event.fire(new Payload(Payload.ADD, name, String.format("A new task has been added in %s", task.getCategory()), "Task", JSON.serialize(task).toString()));
			return json.onSuccess("Task successfully created");
		}
		return json.onError(Arrays.asList("An error has occured"));
	}
	
	@Path("{name}/invite")
	@PUT
	public Response generateNewInviteCode(@PathParam(value = "name") String name) {
		// searching to check if the group exists in the users list
		if(groups.get(new Group(name, null)) == null) {
			return
					json.onError(Arrays.asList("Invalid action"));
		}
		String code = new Group().generateInviteCode();
		groupStore.editInviteKey(name, code);
		return Response
				.ok()
				.entity(Json.createObjectBuilder().add("code", code).build()).build();
	}
	
	/**
	 * Fetching the tasks for a certain group and subgroup of that group
	 * @param groupName		the group's name
	 * @param subgroupName	the subgroup's name
	 * @return
	 */
	@GET
	@Path("{name}/{subgroup}/tasks")
	public Response getTasksBySubGroup(
			@PathParam(value = "name") String name, 
			@PathParam(value = "subgroup") String subgroupName,
			@QueryParam(value = "sort") String sort,
			 @DefaultValue(value = "asc")  @QueryParam(value = "order") String order)  {
		Group group = groups.get(new Group(name, null));
		// checking to see if the group exists in the user's list
		if(group == null) {
			return
					json.onError(Arrays.asList("Invalid Action"));
		}
		List<Task> tasks = taskStore.findTasksBySubgroupNameAndGroupId(subgroupName, group.getTransientID());
		if(sort != null) {
			sort(tasks, sort, order);
		}
		return
				Response
				.ok()
				.entity(JSON.serializeList(tasks).toString())
				.build();
	}
	
	@Path("{name}/subgroups/tasks/{id}")
	@POST
	public Response updateTask(@PathParam("name") String name, @PathParam("id") String id) {
		// checking to see if the group exists in the user's list
		if(groups.get(new Group(name, null)) == null) {
			return
					json.onError(Arrays.asList("Invalid Action"));
		}
		//TODO: fix the user ID since it returns null and change it back to ID
		boolean ok = userStore.addTask(Integer.valueOf(id), name, user.getTransientId());
		if(ok) {
			return
					json.onSuccess("You have taken a new task");
		}

		
		return json.onError(Arrays.asList("Invalid action"));
	}
	
	/**
	 * Sets back a {@link Task} {@value userID} back to {@value NULL}
	 * @param name	the group's name
	 * @param id	task's ID
	 * @return	{@link Response} if the operation was a success or not
	 */
	@Path("{name}/subgroups/tasks/{id}")
	@PUT
	public Response returnTask(@PathParam("name") String name, @PathParam("id") String id) {
		// checking to see if the group exists in the user's list
		if(groups.get(new Group(name, null)) == null) {
			return
					json.onError(Arrays.asList("Invalid Action"));
		}
		boolean ok = userStore.returnTask(Integer.valueOf(id), user.getTransientId());
		if(ok) {
			return
					json.onSuccess("You have returned the task back");
		}
		return json.onError(Arrays.asList("Invalid action"));
	}
	
	/**
	 * Finishes a {@link Task}
	 * @param name group's name
	 * @param id	task's ID
	 * @return	{@link Response} if the operation was a success or not
	 */
	@Path("{name}/subgroups/tasks/{id}")
	@DELETE
	public Response finishTask(@PathParam("name") String name, @PathParam("id") String id) {
		// checking to see if the group exists in the user's list
		if(groups.get(new Group(name, null)) == null) {
			return
					json.onError(Arrays.asList("Invalid Action"));
		}
		boolean ok = userStore.finishTask(Integer.valueOf(id), user.getTransientId());
		if(ok) {
			return
					json.onSuccess("Task finished. Well done!");
		}
		return json.onError(Arrays.asList("Invalid action"));
	}
	
	private boolean isOrderValid(final String order) {
		return availableSortingOrders.get(order) != null;
	}
	
	public void sort(List<Task> entities, String sort, String order) {
			if(isOrderValid(order)) {
					BiFunction<Task, Task, Integer> func = null;
					switch(sort) {
						case "priority": 
							func = (a, b) -> a.getPriority().getRank().compareTo(b.getPriority().getRank());
							break;
						case "difficulty":
							func = (a, b) -> a.getDifficulty().getRank().compareTo(b.getDifficulty().getRank());
							break;
						case "status":
							func = (a, b) -> a.getStatus().getRank().compareTo(b.getStatus().getRank());
							break;
						case "deadline":
							func = (a, b) -> a.getDeadline().compareTo(b.getDeadline());
							break;
					}
					algorithm.insertionSort(entities, func, order);
				}
	}
	
	
}
