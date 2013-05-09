package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.AbstractTestNGServerTest;
import arch.galaxyeclipse.shared.context.ContextHolder;
import org.fest.assertions.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
