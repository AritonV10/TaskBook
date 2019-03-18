package taskbook.v1.business.task.control;

import java.util.List;

import taskbook.v1.business.task.entity.Task;

public interface TaskStore {
	
	/**
	 * Saving a {@link Task} to a specific {@link Group} based on the {@link Group} ID
	 * and the {@link Subgroup} name (returned by the {@link Task})
	 * @param task	the Task object to be saved
	 * @param groupID	the group's ID
	 * @return	true if the save was a success or false otherwise
	 */
	public boolean save(Task task, final Integer groupID);
	
	/**
	 * Find {@link Task} objects based on their {@link Subgroup} name and {@link Group} ID
	 * @param subGroupName	the subgroup name
	 * @param groupID	the group ID
	 * @return	{@link List} of {@link Task}
	 */
	public List<Task> findTasksBySubgroupNameAndGroupId(final String subGroupName, final Integer groupID);
	
	
	
	Integer selectIdOfLatestTaskAdded(final String subgroupName);
}
