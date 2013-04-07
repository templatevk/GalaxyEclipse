package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.inject.*;
import org.junit.*;

import static junit.framework.Assert.*;

/**
 *
 */
public class AuthenticatorTest extends AbstractServerTest {
    private IClientAuthenticator authenticator;

    @Before
    public void initDependencies() {
        authenticator = SpringContextHolder.CONTEXT.getBean(IClientAuthenticator.class);
    }

    @Test
    public void testAuthentication() {
        assertTrue(authenticator.authenticate("test", "test").isSuccess());
    }
}
