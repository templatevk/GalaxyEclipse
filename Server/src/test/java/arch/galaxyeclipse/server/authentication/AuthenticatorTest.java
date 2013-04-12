package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.context.*;
import org.junit.*;

import static junit.framework.Assert.*;

/**
 *
 */
public class AuthenticatorTest extends AbstractJUnitServerTest {
    private IClientAuthenticator authenticator;

    @Before
    public void initDependencies() {
        authenticator = ContextHolder.INSTANCE.getBean(IClientAuthenticator.class);
    }

    @Test
    public void testAuthentication() {
        assertTrue(authenticator.authenticate(
                TEST_PLAYER_USERNAME,
                TEST_PLAYER_PASSWORD_DECRYPTED).isSuccess());
    }
}
