package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.repository.jpa.*;
import arch.galaxyeclipse.shared.context.*;
import org.apache.commons.codec.digest.*;

/**
 *
 */
class ClientAuthenticator implements IClientAuthenticator {
    private arch.galaxyeclipse.server.data.repository.jpa.IPlayersRepository IPlayersRepository;

    public ClientAuthenticator() {
        IPlayersRepository = ContextHolder.INSTANCE.getBean(IPlayersRepository.class);
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
