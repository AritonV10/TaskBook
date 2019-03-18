package taskbook.v1.business.security.control;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import taskbook.v1.business.user.entity.Salt;

@ApplicationScoped
public class PasswordStore {
	
	public boolean isMatch(String hashedPassword, String toCheck) {
		try {
			String hashed = hashPassword(toCheck, Salt.NO_SALT);
			return hashedPassword.substring(0, hashed.length()).equals(hashed);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String hashPassword(final String password, final Salt salt) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		StringBuilder builder = new StringBuilder();
		messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
		byte[] bytes = messageDigest.digest();
		for(byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return salt.equals(Salt.SALT) ? salt(builder) : builder.toString();
	}
	
	private String salt(StringBuilder password) {
		byte[] bytes = new byte[10];
		Random random = new Random();
		random.nextBytes(bytes);
		for(int i = 0; i < 5; ++i) {
			password.append(String.format("%02x", bytes[random.nextInt(8)]));
		}
		return password.toString();
	}
}
