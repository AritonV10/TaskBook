package taskbook.v1.platform.utility;

import org.junit.Test;

public class AlgorithmTest {
	
	@Test
	public void test() {
		int min = 3;
		int max = 5;
		System.out.println(((min - 1)*((min - 1) + 1)/2) - max*(max + 1)/2);
	}
}
