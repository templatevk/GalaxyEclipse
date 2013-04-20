package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.context.*;
import org.fest.assertions.*;
import org.testng.annotations.*;

/**
 *
 */
public class AuthenticatorTest extends AbstractTestNGServerTest {
    private IClientAuthenticator authenticator;

    @BeforeClass
    public void initDependencies() {
        authenticator = ContextHolder.getBean(IClientAuthenticator.class);
    }

    @Test(groups = "fast")
    public void testAuthentication() {
        Assertions.assertThat(authenticator.authenticate(
                TEST_PLAYER_USERNAME,
                TEST_PLAYER_PASSWORD_DECRYPTED).isSuccess()).isTrue();
    }
}
