package taskbook.v1.business.group.control;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.business.group.entity.SubGroup;
import taskbook.v1.platform.persistence.PersistenceDAO;

@Stateless
public class GroupStoreImpl implements GroupStore{

	@Inject
	private PersistenceDAO persistence;
	
	
	public void save(final Group form) {
		persistence
			.save(Group.class, entityManager -> {
				entityManager
					.persist(form);
			});
	}

	@Override
	public boolean existsGroup(String name) {
		return
				persistence
				.find(String.class, (entityManager) -> {
					return getSingleResult(entityManager
							.createNativeQuery("SELECT name FROM groups AS g " +
					"WHERE g.name = :name")
							.setParameter("name", name));
				}) == null;
	}

	@Override
	public List<Group> findGroupNamesByUserId(Integer id) {
		return this
			.persistence
			.findAll(Group.class, (entityManager) -> {
				return
						entityManager
						.createQuery("SELECT new taskbook.v1.business.group.entity.Group(g.name, g.id) FROM Group AS g "
								+ "INNER JOIN g.members m WHERE m.id = :id", Group.class)
						.setParameter("id", id)
						.getResultList();
			});
	}
	
	@Override
	public Group findGroupByInviteCode(final String inviteCode) {
		return
				this.persistence
				.find(Group.class, entityManager -> {
					List<Object[]> ob = 
							entityManager
								.createNativeQuery("SELECT g.id, g.name FROM groups AS g WHERE g.invite_code = :code")
								.setParameter("code", inviteCode)
								.getResultList();
					if(!ob.isEmpty()) {
						Object[] b = ob.get(0);
						return new Group((String) b[1], (String) b[1], (Integer) b[0]);
					}
					
					return null;
						
				}).orElse(null);
	}
	
	@Override
	public void merge(Group group) {
		this
			.persistence
			.save(Group.class, (em) -> {
				em.merge(group);
			});
	}

	@Override
	public Group findSubgroupsByName(String name) {
		return
				persistence
				.find(Group.class, em -> {
					return
							getSingleResult(em.createQuery(
									"FROM Group g "
									+ "LEFT JOIN FETCH g.subGroups s "
									+ "WHERE g.name = :name", Group.class)
							.setParameter("name", name));
				}).orElse(null);
	}

	@Override
	public SubGroup findSubgroupByGroupName(String groupName, String subGroupName) {
		return new SubGroup(
				persistence
				.find(String.class, em -> {
							return getSingleResult(em.createNativeQuery(
									"SELECT s.name FROM sub_groups AS s "
									+ "INNER JOIN groups AS g ON g.id = s.group_id "
									+ "WHERE g.name = :name AND s.name = :sname")
							.setParameter("name", groupName)
							.setParameter("sname", subGroupName));
				}).orElse(null));
	}

	@Override
	public void saveSubGroup(SubGroup subGroup, Integer groupID) {
		this
			.persistence
			.save(SubGroup.class, em -> {
				em
					.createNativeQuery(
							"INSERT INTO sub_groups("
							+ "date_created, date_updated, name, group_id"
							+ ") VALUES(?, ?, ?, ?)")
					.setParameter(1, LocalDate.now())
					.setParameter(2, LocalDate.now())
					.setParameter(3, subGroup.getName())
					.setParameter(4, groupID)
					.executeUpdate();
			});
	}

	@Override
	public void editInviteKey(String groupName, String inviteKey) {
		this
			.persistence
			.merge(Group.class, em -> {
				return em.createNativeQuery("UPDATE groups SET invite_code = :inviteCode WHERE name = :name")
				.setParameter("inviteCode", inviteKey)
				.setParameter("name", groupName)
				.executeUpdate() == 1;
			});
		
	}
}
