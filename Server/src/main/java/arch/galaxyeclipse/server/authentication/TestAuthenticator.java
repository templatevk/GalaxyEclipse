package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.repository.*;
import arch.galaxyeclipse.shared.inject.*;

/**
 * The class performs user authentication simply invoking equals on username and password fields.
 * Should not be used in production.
 */
class TestAuthenticator implements IClientAuthenticator {
    private PlayersRepository playersRepository;

    public TestAuthenticator() {
        playersRepository = SpringContextHolder.CONTEXT.getBean(PlayersRepository.class);
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        // Query the player by the username and password
        Players player = playersRepository.findByUsernameAndPassword(username, password);

        if (player != null) {
            return new AuthenticationResult(true);
        }
        return new AuthenticationResult(false);
    }
}
