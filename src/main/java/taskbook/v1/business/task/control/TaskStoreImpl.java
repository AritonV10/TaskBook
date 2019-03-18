package taskbook.v1.business.task.control;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import taskbook.v1.business.task.entity.Difficulty;
import taskbook.v1.business.task.entity.Priority;
import taskbook.v1.business.task.entity.Status;
import taskbook.v1.business.task.entity.Task;
import taskbook.v1.platform.persistence.JPAHelperMethods;
import taskbook.v1.platform.persistence.PersistenceDAO;

@Stateless
public class TaskStoreImpl implements TaskStore, JPAHelperMethods {

	@Inject
	private PersistenceDAO dao;
	
	@Override
	public boolean save(Task task, Integer groupID) {
		// checking to see if the subgroup exists
		// before saving the task
		Integer subId =
				dao
					.find(Integer.class, em -> {
						return
								getSingleResult(
										em.createNativeQuery(
												"SELECT s.id FROM sub_groups AS s "
												+ "WHERE s.name = :name AND s.group_id = :id"
														)
										.setParameter("name", task.getTransientSubgroupName())
										.setParameter("id", groupID));
								
					}).orElse(null);
		
		if(subId != null) {
			dao.save(Task.class, em -> {
				// native query to avoid the annoying transient exception thrown by JPA
				//TODO: make this into one query
				em
					.createNativeQuery(
							"INSERT INTO tasks("
							+ "date_updated, date_created, "
							+ "deadline, description, difficulty, "
							+ "priority, status, subGroup_id) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)")
					.setParameter(1, LocalDate.now())
					.setParameter(2, LocalDate.now())
					.setParameter(3, Date.valueOf(task.getDeadline()))
					.setParameter(4, task.getDescription())
					.setParameter(5, task.getDifficulty().toString())
					.setParameter(6, task.getPriority().toString())
					.setParameter(7, task.getStatus().toString())
					.setParameter(8, subId)
					.executeUpdate();
			});
			return true;
		}
		return false;
	}
	
	@Override
	public List<Task> findTasksBySubgroupNameAndGroupId(final String subGroupName, final Integer groupId) {
		return
				dao
				.findAll(Task.class, em -> {
					@SuppressWarnings("unchecked")
					List<Object[]> objects = 
							em.createNativeQuery(
									"SELECT "
									+ "t.id, t.deadline, "
									+ "t.description, t.difficulty, "
									+ "t.status, t.priority, s.name FROM tasks AS t "
									+ "LEFT JOIN sub_groups AS s ON s.id = t.subGroup_id "
									+ "LEFT JOIN groups AS g ON g.id = s.group_id "
									+ "WHERE g.id = :groupId AND s.name = :gName")
								.setParameter("groupId", groupId)
								.setParameter("gName", subGroupName)
							.getResultList();
					List<Task> tasks = new ArrayList<>();
					
					//TODO: change this to be more flexible
					for(Object[] ob : objects) {
						tasks
							.add(new Task(
									(Integer) ob[0],
									((Date) ob[1]).toLocalDate(),
									(String) ob[2],
									Difficulty.value((String) ob[3]),
									Status.value((String) ob[4]),
									Priority.value((String) ob[5]),
									(String) ob[6]
									));
					}
					return tasks;
				});
	}

	@Override
	public Integer selectIdOfLatestTaskAdded(String subgroupName) {
		return
			dao.find(Integer.class, em -> {
				return getSingleResult(em
						.createNativeQuery(
								"SELECT t.id FROM tasks AS t INNER JOIN sub_groups AS g ON g.id = t.subGroup_id "
								+ "WHERE g.name = :name ORDER BY t.id DESC LIMIT 1")
							.setParameter("name", subgroupName)
							);
			}).orElse(null);
	}

}
