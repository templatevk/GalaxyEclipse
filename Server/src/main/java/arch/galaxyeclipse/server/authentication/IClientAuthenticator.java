package arch.galaxyeclipse.server.authentication;

/**
 * Responsible for authenticating the client.
 */
public interface IClientAuthenticator {
	AuthenticationResult authenticate(String username, String password);
}
