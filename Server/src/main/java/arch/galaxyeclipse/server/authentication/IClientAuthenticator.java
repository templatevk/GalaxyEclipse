package arch.galaxyeclipse.server.authentication;

public interface IClientAuthenticator {
	AuthenticationResult authenticate(String username, String password);
}
