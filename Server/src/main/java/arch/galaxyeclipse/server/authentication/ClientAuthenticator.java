package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
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
        Player player = new DataWorker<Player>() {
            @Override
            protected void doWork(Session session) {
               List<Player> playerList = session.createCriteria(Player.class)
                       .add(Restrictions.eq("activated", true))
                       .add(Restrictions.eq("banned", false))
                       .add(Restrictions.eq("username", username))
                       .add(Restrictions.eq("password", DigestUtils.md5Hex(password)))
                       .setFetchMode("shipStates", FetchMode.JOIN)
                       .setFetchMode("shipConfigs", FetchMode.JOIN)
                       .setFetchMode("shipStates.locationObject", FetchMode.JOIN)
               .list();

                if (!playerList.isEmpty()) {
                    setResult(playerList.get(0));
                }
            }
        }.execute();

        if (player != null && player.isActivated() && !player.isBanned()) {
            return new AuthenticationResult(true, player);
        }
        return new AuthenticationResult(false);
    }
}