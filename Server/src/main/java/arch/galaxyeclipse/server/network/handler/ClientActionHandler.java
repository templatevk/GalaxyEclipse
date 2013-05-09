package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.JedisSerializers.LocationObjectPacketSerializer;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.data.RedisUnitOfWork;
import arch.galaxyeclipse.server.data.model.LocationObject;
import arch.galaxyeclipse.server.data.model.ShipConfig;
import arch.galaxyeclipse.server.data.model.ShipState;
import arch.galaxyeclipse.shared.common.MathUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ClientActionPacket.ClientActionType;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.thread.TaskRunnablePair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.jedis.JedisConnection;

import static arch.galaxyeclipse.shared.SharedInfo.*;

/**
 *
 */
@Slf4j
class ClientActionHandler extends PacketHandlerDecorator {
    private PlayerInfoHolder playerInfoHolder;

    private MoveHandler moveHandler;
    private RotationHandler rotationHandler;

    public ClientActionHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();

        moveHandler = new MoveHandler(playerInfoHolder);
        rotationHandler = new RotationHandler(playerInfoHolder);
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CLIENT_ACTION:
                ClientActionPacket clientAction = packet.getClientAction();
                ClientActionType clientActionType = clientAction.getType();

                switch (clientActionType) {
                    case ROTATE_LEFT_DOWN:
                    case ROTATE_LEFT_UP:
                    case ROTATE_RIGHT_DOWN:
                    case ROTATE_RIGHT_UP:
                        processRotation(clientActionType);
                        break;
                    case MOVE_UP:
                    case MOVE_DOWN:
                    case STOP_UP:
                    case STOP_DOWN:
                        processMoving(clientActionType);
                        processMoving(clientActionType);
                        break;
                    case OBJECT_CLICK:
                        break;
                    case LOOT_PICK:
                        break;
                    case ATTACK:
                        break;
                    case ROCKET_SHOOT:
                        break;
                }

                return true;
        }
        return false;
    }

    @Override
    public void onChannelClosed() {
        rotationHandler.stop();
    }

    private void processMoving(ClientActionType moveType) {
        moveHandler.setMoveType(moveType);
    }

    private void processRotation(ClientActionType moveType) {
        rotationHandler.setRotationType(moveType);
    }

    private static class RotationHandler extends TaskRunnablePair<Runnable> {
        public static final int MAX_ANGLE = 360;

        private ClientActionType rotationType;
        private ShipConfig shipConfig;
        private ShipState shipState;
        private LocationObject locationObject;
        private RotatingRunnable rotatingRunnable;
        private PostRotatingRunnable postRotatingRunnable;
        private PlayerInfoHolder playerInfoHolder;

        private RotationHandler(PlayerInfoHolder playerInfoHolder) {
            super(CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS, null, true, true);
            this.playerInfoHolder = playerInfoHolder;

            shipConfig = playerInfoHolder.getShipConfig();
            shipState = playerInfoHolder.getShipState();
            locationObject = playerInfoHolder.getLocationObject();
            rotatingRunnable = new RotatingRunnable();
            postRotatingRunnable = new PostRotatingRunnable();
        }

        public void setRotationType(ClientActionType rotationType) {
            this.rotationType = rotationType;

            switch (rotationType) {
                case ROTATE_LEFT_DOWN:
                case ROTATE_RIGHT_DOWN:
                    setRunnable(rotatingRunnable);
                    start();
                    break;
                case ROTATE_LEFT_UP:
                case ROTATE_RIGHT_UP:
                    setRunnable(postRotatingRunnable);
                    break;
            }
        }

        private void update(final float rotationAngle) {
            new RedisUnitOfWork() {
                @Override
                protected void doWork(JedisConnection connection) {
                    LocationObjectPacketSerializer lopSerializer = new LocationObjectPacketSerializer();
                    byte[] lopHashKey = playerInfoHolder.getLocationObjectPacketHashKey();

                    byte[] lopBytes = connection.hGet(lopHashKey, lopHashKey);
                    LocationObjectPacket lop = lopSerializer.deserialize(lopBytes);
                    LocationObjectPacket newLop = LocationObjectPacket.newBuilder()
                            .mergeFrom(lop).setRotationAngle(rotationAngle).build();

                    connection.hSet(lopHashKey, lopHashKey, lopSerializer.serialize(newLop));
                }
            }.execute();
        }

        private class RotatingRunnable implements Runnable {
            @Override
            public void run() {
                float currentRotationSpeed = shipState.getShipStateRotationSpeed();
                float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();
                float maxRotationSpeed = shipConfig.getShipConfigRotationMaxSpeed();

                switch (rotationType) {
                    case ROTATE_RIGHT_DOWN:
                        currentRotationSpeed -= rotationAcceleration;
                        break;
                    case ROTATE_LEFT_DOWN:
                        currentRotationSpeed += rotationAcceleration;
                        break;
                }
                if (Math.abs(currentRotationSpeed) > maxRotationSpeed) {
                    currentRotationSpeed = Math.signum(currentRotationSpeed) * maxRotationSpeed;
                }
                shipState.setShipStateRotationSpeed(currentRotationSpeed);

                float currentRotationAngle = locationObject.getRotationAngle();
                currentRotationAngle += currentRotationSpeed;
                if (currentRotationAngle > MAX_ANGLE) {
                    currentRotationAngle -= MAX_ANGLE;
                } else if (currentRotationAngle < 0) {
                    currentRotationAngle = MAX_ANGLE + currentRotationAngle;
                }
                locationObject.setRotationAngle(currentRotationAngle);

                if (ClientActionHandler.log.isDebugEnabled()) {
                    ClientActionHandler.log.debug(rotationType.toString());
                    ClientActionHandler.log.debug("Rotation speed " + currentRotationSpeed);
                    ClientActionHandler.log.debug("Rotation angle " + currentRotationAngle);
                }
                update(currentRotationAngle);
            }
        }

        private class PostRotatingRunnable implements Runnable {
            @Override
            public void run() {
                float currentRotationSpeed = shipState.getShipStateRotationSpeed();
                float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();

                switch (rotationType) {
                    case ROTATE_RIGHT_UP:
                        currentRotationSpeed += rotationAcceleration;
                        if (currentRotationSpeed > 0) {
                            currentRotationSpeed = 0;
                            stop();
                        }
                        break;
                    case ROTATE_LEFT_UP:
                        currentRotationSpeed -= rotationAcceleration;
                        if (currentRotationSpeed < 0) {
                            currentRotationSpeed = 0;
                            stop();
                        }
                        break;
                }
                shipState.setShipStateRotationSpeed(currentRotationSpeed);

                float currentRotationAngle = locationObject.getRotationAngle();
                currentRotationAngle += currentRotationSpeed;
                if (currentRotationAngle > MAX_ANGLE) {
                    currentRotationAngle -= MAX_ANGLE;
                } else if (currentRotationAngle < 0) {
                    currentRotationAngle = MAX_ANGLE + currentRotationAngle;
                }
                locationObject.setRotationAngle(currentRotationAngle);

                if (ClientActionHandler.log.isDebugEnabled()) {
                    ClientActionHandler.log.debug(rotationType.toString());
                    ClientActionHandler.log.debug("Rotation speed " + currentRotationSpeed);
                    ClientActionHandler.log.debug("Rotation angle " + currentRotationAngle);
                }
                update(currentRotationAngle);
            }
        }
    }

    private static class MoveHandler {
        private ClientActionType moveType;
        private ShipConfig shipConfig;
        private ShipState shipState;
        private LocationObject locationObject;
        private SpeedTask speedTask;
        private PositionTask positionTask;
        private PlayerInfoHolder playerInfoHolder;

        private MoveHandler(PlayerInfoHolder playerInfoHolder) {
            this.playerInfoHolder = playerInfoHolder;
            shipConfig = playerInfoHolder.getShipConfig();
            shipState = playerInfoHolder.getShipState();
            locationObject = playerInfoHolder.getLocationObject();

            speedTask = new SpeedTask();
            positionTask = new PositionTask();
        }

        public void setMoveType(ClientActionType moveType) {
            this.moveType = moveType;

            switch (moveType) {
                case MOVE_DOWN:
                case STOP_DOWN:
                    if (!positionTask.isAlive()) {
                        positionTask.start();
                    }
                    speedTask.start();
                    break;
                case MOVE_UP:
                case STOP_UP:
                    speedTask.stop();
                    break;
            }
        }

        private class SpeedTask extends TaskRunnablePair<Runnable> implements Runnable {
            private SpeedTask() {
                super(CLIENT_ACTION_SPEED_DELAY_MILLISECONDS, null, true, true);
                setRunnable(this);
            }

            @Override
            public void run() {
                float currentMoveSpeed = shipState.getShipStateMoveSpeed();
                float moveAcceleration = shipConfig.getShipConfigMoveAcceleration();
                float maxMoveSpeed = shipConfig.getShipConfigMoveMaxSpeed();

                switch (moveType) {
                    case MOVE_DOWN:
                        currentMoveSpeed += moveAcceleration;
                        break;
                    case STOP_DOWN:
                        currentMoveSpeed -= moveAcceleration;
                        if (currentMoveSpeed <= 0) {
                            currentMoveSpeed = 0;
                            speedTask.stop();
                            positionTask.stop();
                        }
                        break;
                }

                if (currentMoveSpeed > maxMoveSpeed) {
                    currentMoveSpeed = maxMoveSpeed;
                }

                shipState.setShipStateMoveSpeed(currentMoveSpeed);

                if (ClientActionHandler.log.isDebugEnabled()) {
                    ClientActionHandler.log.debug(moveType.toString());
                    ClientActionHandler.log.debug("Move speed " + currentMoveSpeed);
                }
            }
        }

        private class PositionTask extends TaskRunnablePair<Runnable> implements Runnable {
            private PositionTask() {
                super(CLIENT_ACTION_MOVE_DELAY_MILLISECONDS, null, true, true);
                setRunnable(this);
            }

            @Override
            public void run() {
                float currentMoveSpeed = shipState.getShipStateMoveSpeed();
                float rotationAngle = locationObject.getRotationAngle();
                float positionX = locationObject.getPositionX();
                float positionY = locationObject.getPositionY();
                float xDiff = currentMoveSpeed * MathUtils.sinDeg(rotationAngle);
                float yDiff = currentMoveSpeed * MathUtils.cosDeg(rotationAngle);
                positionX += xDiff * SHIP_MOVE_SPEED_TO_LOCATION_COORDS_COEF;
                // -= here because of client rendering
                positionY -= yDiff * SHIP_MOVE_SPEED_TO_LOCATION_COORDS_COEF;

                locationObject.setPositionX(positionX);
                locationObject.setPositionY(positionY);

                if (ClientActionHandler.log.isDebugEnabled()) {
                    ClientActionHandler.log.debug(moveType.toString());
                    ClientActionHandler.log.debug("Position x " + positionX);
                    ClientActionHandler.log.debug("Position y " + positionY);
                }

                final float newPositionX = positionX;
                final float newPositionY = positionY;
                new RedisUnitOfWork() {
                    @Override
                    protected void doWork(JedisConnection connection) {
                        LocationObjectPacketSerializer lopSerializer = new LocationObjectPacketSerializer();
                        byte[] lopHashKey = playerInfoHolder.getLocationObjectPacketHashKey();

                        byte[] lopBytes = connection.hGet(lopHashKey, lopHashKey);
                        LocationObjectPacket lop = lopSerializer.deserialize(lopBytes);
                        LocationObjectPacket newLop = LocationObjectPacket.newBuilder()
                                .mergeFrom(lop)
                                .setPositionX(newPositionX)
                                .setPositionY(newPositionY).build();

                        connection.hSet(lopHashKey, lopHashKey, lopSerializer.serialize(newLop));
                    }
                }.execute();
            }
        }
    }
}
