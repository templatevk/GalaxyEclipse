package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.HibernateUnitOfWork;
import arch.galaxyeclipse.server.data.model.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
*
*/
class ClientAuthenticator implements IClientAuthenticator {
    public ClientAuthenticator() {

    }

    @Override
    public AuthenticationResult authenticate(final String username,
            final String password) {
        Player player = new HibernateUnitOfWork<Player>() {
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