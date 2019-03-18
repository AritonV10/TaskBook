package taskbook.v1.business.security.control;

import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import taskbook.v1.business.user.control.UserStore;
import taskbook.v1.business.user.entity.User;
import taskbook.v1.platform.utility.Sets;


@ApplicationScoped
public class IdentityStoreImpl implements IdentityStore{
	
	@Inject
	private UserStore userStore;
	
	@Override
	public CredentialValidationResult validate(Credential credential) {
		if(credential instanceof UsernamePasswordCredential) {
			String email = ((UsernamePasswordCredential) credential).getCaller();
			String pw = ((UsernamePasswordCredential) credential).getPasswordAsString();
			Optional<User> user = userStore.getCredentialsForLogin(email);
			if(user.isPresent()) {
				if(userStore.login(user.get().getPassword(), pw) && user.get().isBanned() != true) {
					return new CredentialValidationResult(
							email, 
							getRoles(user.get()));
				} else {
					return CredentialValidationResult.INVALID_RESULT;
				}
			} else {
				return CredentialValidationResult.INVALID_RESULT;
			}
		}
		return CredentialValidationResult.NOT_VALIDATED_RESULT;	
	}
	public Set<String> getRoles(User user) {
		return Sets.asSet(user.getRank().toString());
	}
	
}
