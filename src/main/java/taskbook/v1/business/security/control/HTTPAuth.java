package taskbook.v1.business.security.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AutoApplySession
@ApplicationScoped
public class HTTPAuth implements HttpAuthenticationMechanism{

	@Inject
	private IdentityStore identityStore;
	
	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {
		Credential cred = httpMessageContext.getAuthParameters().getCredential();
		if(cred != null) {
			CredentialValidationResult result = this.identityStore.validate(cred);
			if(result.getStatus() == Status.VALID) {
				return httpMessageContext
				.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
			}
		}
		return httpMessageContext.doNothing();
	}

}
