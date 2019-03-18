package taskbook.v1.business.algorithm;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import taskbook.v1.business.algorithm.control.Algorithm;
import taskbook.v1.business.group.entity.Group;

public class AlgorithmTest {

	private Algorithm algorithm;
	
	@Before
	public void setup() {
		this.algorithm = new Algorithm();
	}
	 
	@SuppressWarnings("deprecation")
	@Test
	public void testInsertionSort() {
		List<Group> groups
			= Arrays.asList(new Group("C", 4), new Group("A", 9), new Group("B", 5));
		algorithm.insertionSort(groups, Group::compareToName);
		assertEquals(new String[]{"A", "B", "C"}, 
				new String[] {groups.get(0).getName(), groups.get(1).getName(), groups.get(2).getName()});
		
	} 
}
