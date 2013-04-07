package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.repository.*;
import arch.galaxyeclipse.shared.inject.*;
import org.apache.commons.codec.digest.*;

/**
 *
 */
class ClientAuthenticator implements IClientAuthenticator {
    private IPlayersRepository IPlayersRepository;

    public ClientAuthenticator() {
        IPlayersRepository = SpringContextHolder.CONTEXT.getBean(IPlayersRepository.class);
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        // Query the player by the username and password
        Players player = IPlayersRepository.findByUsernameAndPassword(
                username, DigestUtils.md5Hex(password));

        if (player != null && player.isActivated() && !player.isBanned()) {
            return new AuthenticationResult(true);
        }
        return new AuthenticationResult(false);
    }
}
