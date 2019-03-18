package taskbook.v1.business.group.entity;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import taskbook.v1.business.user.entity.CurrentUser;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.persistence.PersistenceDAO;
import taskbook.v1.platform.utility.BinaryTree;
import taskbook.v1.platform.utility.UniqueBinaryTree;

public class GroupFactory {

	@Inject
	private PersistenceDAO dao;
	
	@Inject @CurrentUser
	private User user;
	
	@Produces
	@RequestScoped
	@UserGroups
	public BinaryTree<Group> getUserGroups() {
		BinaryTree<Group> groups = new UniqueBinaryTree<Group>((a, b) -> a.getName().compareTo(b.getName()));
		dao
			.findAll(Group.class, em -> {
				@SuppressWarnings("unchecked")
				List<Object[]> objects = em
					.createNativeQuery(
							"SELECT g.id, g.name, g.administrator_id FROM group_members AS gm " + 
							"RIGHT OUTER JOIN groups AS g ON gm.group_id = g.id " + 
							"WHERE gm.user_id = :userID OR g.administrator_id = :uID")
					.setParameter("userID", user.getTransientId())
					.setParameter("uID", user.getTransientId())
					.getResultList();
				if(!objects.isEmpty()) {
					for(Object[] ob : objects) {
						groups.add(new Group((Integer) ob[0], (Integer) ob[0], (String) ob[1], (Integer) ob[2]));
					}
				}
				return null;
			});
		return groups;
	}
}
