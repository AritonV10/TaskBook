package taskbook.v1.platform.utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import taskbook.v1.business.group.entity.Group;

@RunWith(JUnit4.class)
public class BinaryTreeTest {

	@Test
	public void addTest() {
		BinaryTree<Group> group = new UniqueBinaryTree<Group>((a, b) -> a.getName().compareTo(b.getName()));
		group.add(new Group("g1", null));
		group.add(new Group("g2", null));
		assertEquals((Integer) 2, group.size());
		assertEquals("g2", group.get(new Group("g2", null)).getName());
	}
	
	@Test
	public void toList() {
		BinaryTree<Group> group = new UniqueBinaryTree<Group>((a, b) -> a.getName().compareTo(b.getName()));
		group.add(new Group("g1", null));
		group.add(new Group("g2", null));
		group
			.toList()
			.forEach(e -> System.out.println(e.getName()));
	}
} 
