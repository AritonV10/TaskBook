package taskbook.v1.business.user.control;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import taskbook.v1.business.security.control.PasswordStore;
import taskbook.v1.business.user.entity.Salt;

@RunWith(JUnit4.class)
public class PasswordStoreTest {
	
	
	private PasswordStore passwordStore;
	
	public static final String HASHED_PW = "f34496305470eb1d23d57c253b849fb4840781a127e8eb13aa56b8bb65ee5d1e";
	public static final String PW = "mick";
	
	@Before
	public void setUp() throws NoSuchAlgorithmException {
		this.passwordStore = new PasswordStore();
	}

	@Test @Ignore
	public void test() {
		
	}
	@Test
	public void isSame() throws NoSuchAlgorithmException {
		String hashed = passwordStore.hashPassword(PW, Salt.SALT);
		assertEquals(true, passwordStore.isMatch(hashed, PW));
	}
	
	@Test
	public void hash() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		assertEquals(HASHED_PW, passwordStore.hashPassword(PW, Salt.NO_SALT));
	}
}
