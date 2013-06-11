package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.GeAbstractTestNGServerTest;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import org.fest.assertions.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class GeAuthenticatorTest extends GeAbstractTestNGServerTest {
    private IGeClientAuthenticator authenticator;

    @BeforeClass
    public void initDependencies() {
        authenticator = GeContextHolder.getBean(IGeClientAuthenticator.class);
    }

    @Test(groups = "fast")
    public void testAuthentication() {
        Assertions.assertThat(authenticator.authenticate(
                TEST_PLAYER_USERNAME,
                TEST_PLAYER_PASSWORD_DECRYPTED).isSuccess()).isTrue();
    }
}
