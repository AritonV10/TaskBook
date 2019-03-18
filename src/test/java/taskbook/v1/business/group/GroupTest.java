package taskbook.v1.business.group;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import taskbook.v1.business.group.entity.Group;
import taskbook.v1.platform.utility.UniqueBinaryTree;

@RunWith(JUnit4.class)
public class GroupTest {
 
	@Test
	public void testInviteCode() {
		List<Group> groups = Arrays.asList(new Group("C", 7), new Group("F", 2), new Group("A", 5));
		UniqueBinaryTree<Group> binary = 
				new UniqueBinaryTree<>(groups.get(0), (a, b) -> a.getName().compareTo(b.getName()));
		binary.display(group -> System.out.println(group.getName()));
		System.out.println("HEY");
	} 
}
  