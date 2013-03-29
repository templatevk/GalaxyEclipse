package arch.galaxyeclipse.server.authentication;

import org.springframework.stereotype.*;

@Component
public class StubAllowingAuthenticator implements IClientAuthenticator {
	@Override
	public AuthenticationResult authenticate(String username, String password) {
		return new AuthenticationResult(true);
	}
}
