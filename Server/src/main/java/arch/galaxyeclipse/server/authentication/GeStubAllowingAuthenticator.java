package arch.galaxyeclipse.server.authentication;

/**
 * Test authenticator.
 */
class GeStubAllowingAuthenticator implements IGeClientAuthenticator {

    @Override
    public GeAuthenticationResult authenticate(String username, String password) {
        return new GeAuthenticationResult(true);
    }
}
