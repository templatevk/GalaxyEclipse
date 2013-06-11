package arch.galaxyeclipse.client.ui.actor;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;

/**
 * Responsible for retreiving pictures from the file system and creating Actors.
 */
public interface IGeActorFactory {

    GeLocationObjectActor createLocationObjectActor(GeLocationObjectPacket locationObjectPacket);

    GeBackgroundActor createBackgroundActor(int locationId);
}
