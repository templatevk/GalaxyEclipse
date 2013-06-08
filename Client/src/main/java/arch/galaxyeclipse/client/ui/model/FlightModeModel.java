package arch.galaxyeclipse.client.ui.model;

import arch.galaxyeclipse.client.ui.actor.IGeActor;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 */
@Data
public class FlightModeModel {
    private static final int ACRORS_SIZE = 0;

    private Set<IGeActor> gameActors;
    private IGeActor background;

    public FlightModeModel() {
        this(ACRORS_SIZE);
    }

    public FlightModeModel(int actorsCapacity) {
        this(actorsCapacity, null);
    }

    public FlightModeModel(int actorsCapacity, IGeActor background) {
        gameActors = new LinkedHashSet<>(actorsCapacity);
        this.background = background;
    }
}