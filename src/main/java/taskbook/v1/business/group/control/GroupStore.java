package taskbook.v1.business.group.control;

import java.util.List;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.platform.persistence.JPAHelperMethods;

public interface GroupStore extends JPAHelperMethods {
	/**
	 * <p> Persist {@link Group} entity
	 * @param group
	 */
	public void save(final Group group);
	
	/**
	 * Check if the group exists already or not
	 * @param name
	 * @return if the group exists or not
	 */
	public boolean existsGroup(final String name);
	
	public List<Group> findGroupNamesByUserId(final Integer id);
	
	public void editInviteKey(String groupName, String inviteKey);
	
	/**
	 * Search a {@link Group} by It's invitation code
	 * @param inviteCode 	the group's invitation code
	 * @return {@link Group} the {@link Group} entity else null	
	 */
	public Group findGroupByInviteCode(final String inviteCode);
	
	/**
	 * Updates a {@link Group} entity
	 * @param group the {@link Group} to be updated
	 */
	public void merge(Group group);
	
	Group findSubgroupsByName(final String name);
	
	SubGroup findSubgroupByGroupName(final String groupName, final String subGroupName);
	
	/**
	 * Saving a subgroup to a group based on the group's ID
	 * @param subGroup	the subgroup to be saved
	 * @param groupID	the group's ID
	 */
	void saveSubGroup(SubGroup subGroup, final Integer groupID);
}
