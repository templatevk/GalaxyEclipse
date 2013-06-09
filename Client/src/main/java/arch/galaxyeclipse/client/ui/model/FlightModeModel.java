package arch.galaxyeclipse.client.ui.model;

import arch.galaxyeclipse.client.ui.actor.IActorFactory;
import arch.galaxyeclipse.client.ui.actor.IGeActor;
import arch.galaxyeclipse.client.ui.actor.LocationObjectActor;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 *
 */
public class FlightModeModel {

    private Map<Integer, LocationObjectActor> actorsCache;
    private IActorFactory actorFactory;
    private @Getter @Setter NavigableSet<LocationObjectActor> sortedActors;
    private @Getter @Setter IGeActor background;

    public FlightModeModel() {
        this(null);
    }

    public FlightModeModel(IGeActor background) {
        this.background = background;
        actorsCache = new HashMap<>();
        sortedActors = new TreeSet<>();
        actorFactory = ContextHolder.getBean(IActorFactory.class);
    }

    public void refreshActors(List<LocationObjectPacket> lopList) {
        List<Integer> newActorIds = new ArrayList<>(lopList.size());
        for (LocationObjectPacket lop : lopList) {
            int objectId = lop.getObjectId();
            LocationObjectActor actor = actorsCache.get(objectId);

            if (actor == null) {
                actor = actorFactory.createLocationObjectActor(lop);
                actorsCache.put(objectId, actor);
            } else {
                actor.setLop(lop);
            }
            newActorIds.add(objectId);
        }
        actorsCache.keySet().retainAll(newActorIds);

        sortedActors = new TreeSet<>(actorsCache.values());
    }
}