package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.GeHibernateUnitOfWork;
import arch.galaxyeclipse.server.data.model.GePlayer;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 */
class GeClientAuthenticator implements IGeClientAuthenticator {

    public GeClientAuthenticator() {

    }

    @Override
    public GeAuthenticationResult authenticate(final String username,
            final String password) {
        GePlayer player = new GeHibernateUnitOfWork<GePlayer>() {
            @Override
            protected void doWork(Session session) {
                GePlayer player = (GePlayer) session.createCriteria(GePlayer.class)
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
            return new GeAuthenticationResult(true, player);
        }
        return new GeAuthenticationResult(false);
    }
}