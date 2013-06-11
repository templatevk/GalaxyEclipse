package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.GeAbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.model.GePlayer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.testng.annotations.Test;

import java.util.Calendar;

/**
 *
 */
@Slf4j
public class GePlayerQueryTest extends GeAbstractTestNGServerTest {

    @Test
    public void startupInfoQueryTest() {
        long start = Calendar.getInstance().getTimeInMillis();

        GePlayer p2 = new GeHibernateUnitOfWork<GePlayer>() {
            @Override
            protected void doWork(Session session) {
                setResult((GePlayer) session.getNamedQuery("player.startupInfo")
                        .setParameter("playerId", 1)
                        .uniqueResult());
            }
        }.execute();

        System.out.println(Calendar.getInstance().getTimeInMillis() - start);

        System.out.println("ship state " + p2.getShipState().getShipStateId());
        System.out.println("ship state - location object " +
                p2.getLocationObject().getLocationId());
        System.out.println("ship state - location object - location " +
                p2.getLocationObject().getLocation().getLocationId());
        if (p2.getInventoryItems().iterator().hasNext()) {
            System.out.println("inventory items - item " +
                    p2.getInventoryItems().iterator().next().getItem().getItemId());
        }
        System.out.println("ship config " + p2.getShipConfig().getShipConfigId());
        System.out.println("ship config - ship type " +
                p2.getShipConfig().getShipType().getShipTypeId());
        System.out.println("ship config - engine " + p2.getShipConfig().getEngine().getItemId());
    }
}
