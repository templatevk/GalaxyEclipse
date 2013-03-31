package arch.galaxyeclipse.server.authentication;

/**
 * Test authenticator.
 */
public class StubAllowingAuthenticator implements IClientAuthenticator {
	@Override
	public AuthenticationResult authenticate(String username, String password) {
		return new AuthenticationResult(true);
	}
}
