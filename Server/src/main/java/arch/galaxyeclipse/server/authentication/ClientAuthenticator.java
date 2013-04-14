package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.types.*;
import org.apache.commons.codec.digest.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

import java.util.*;

/**
*
*/
class ClientAuthenticator implements IClientAuthenticator {
    public ClientAuthenticator() {

    }

    @Override
    public AuthenticationResult authenticate(final String username,
            final String password) {
        Player player = new UnitOfWork<Player>() {
            @Override
            protected void doWork(Session session) {
               Player player = (Player)session.createCriteria(Player.class)
                       .add(Restrictions.eq("activated", true))
                       .add(Restrictions.eq("banned", false))
                       .add(Restrictions.eq("username", username))
                       .add(Restrictions.eq("password", DigestUtils.md5Hex(password)))
                       .uniqueResult();

                if (player != null) {
                    setResult(player);
                }
            }
        }.execute();

        if (player != null && player.isActivated() && !player.isBanned()) {
            return new AuthenticationResult(true, player);
        }
        return new AuthenticationResult(false);
    }
}