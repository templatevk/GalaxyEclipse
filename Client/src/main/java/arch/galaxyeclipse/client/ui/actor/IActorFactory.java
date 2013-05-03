package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;

/**
 * Responsible for retreiving pictures from the file system and creating Actors.
 */
public interface IActorFactory {
    IGeActor createLocationObjectActor(LocationObject locationObject);

    IGeActor createBackgroundActor(int locationId);
}
