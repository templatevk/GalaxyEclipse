package arch.galaxyeclipse.server.authentication;

/**
 * Responsible for authenticating the client.
 */
public interface IGeClientAuthenticator {
	GeAuthenticationResult authenticate(String username, String password);
}
