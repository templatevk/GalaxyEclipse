package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.UnitOfWork;
import arch.galaxyeclipse.server.data.model.LocationObject;
import arch.galaxyeclipse.server.data.model.ShipConfig;
import arch.galaxyeclipse.server.data.model.ShipState;
import arch.galaxyeclipse.server.util.MathUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ClientAction.ClientActionType;
import arch.galaxyeclipse.shared.thread.TaskRunnablePair;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import static arch.galaxyeclipse.shared.SharedInfo.*;

/**
 *
 */
@Slf4j
class ClientActionHandler extends PacketHandlerDecorator {
    private PlayerInfoHolder playerInfoHolder;
    private ShipConfig shipConfig;
    private ShipState shipState;
    private LocationObject locationObject;

    private MoveHandler moveHandler;
    private RotationHandler rotationHandler;
    private boolean rotating;
    private boolean moving;

    public ClientActionHandler(IChannelAwarePacketHandler decoratedPacketHandler) {
        super(decoratedPacketHandler);

        playerInfoHolder = getServerChannelHandler().getPlayerInfoHolder();
        shipConfig = playerInfoHolder.getShipConfig();
        shipState = playerInfoHolder.getShipState();
        locationObject = playerInfoHolder.getLocationObject();

        moveHandler = new MoveHandler(playerInfoHolder);
        rotationHandler = new RotationHandler(playerInfoHolder);
        rotating = false;
    }

    @Override
    protected boolean handleImp(GeProtocol.Packet packet) {
        switch (packet.getType()) {
            case CLIENT_ACTION:
                GeProtocol.ClientAction clientAction = packet.getClientAction();
                ClientActionType clientActionType = clientAction.getType();

                switch (clientActionType) {
                    case ROTATE_LEFT:
                    case ROTATE_RIGHT:
                        processRotation(clientActionType);
                        break;
                    case MOVE:
                    case STOP:
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
        moving = !moving;
        moveHandler.setMoveType(moveType);
        moveHandler.setMoving(moving);
    }

    private void processRotation(ClientActionType moveType) {
        rotating = !rotating;
        rotationHandler.setRotationType(moveType);
        rotationHandler.setRotating(rotating);
    }

    private static class RotationHandler extends TaskRunnablePair<Runnable> {
        public static final int MAX_ANGLE = 360;

        private @Setter GeProtocol.ClientAction.ClientActionType rotationType;
        private ShipConfig shipConfig;
        private ShipState shipState;
        private LocationObject locationObject;
        private RotatingRunnable rotatingRunnable;
        private PostRotatingRunnable postRotatingRunnable;

        private RotationHandler(PlayerInfoHolder playerInfoHolder) {
            super(CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS, null, true, true);

            shipConfig = playerInfoHolder.getShipConfig();
            shipState = playerInfoHolder.getShipState();
            locationObject = playerInfoHolder.getLocationObject();
            rotatingRunnable = new RotatingRunnable();
            postRotatingRunnable = new PostRotatingRunnable();

            setRotating(false);
        }

        public void setRotating(boolean rotating) {
            if (rotating) {
                setRunnable(rotatingRunnable);
                start();
            } else {
                setRunnable(postRotatingRunnable);
            }
        }

        private class RotatingRunnable implements Runnable {
            @Override
            public void run() {
                float currentRotationSpeed = shipState.getShipStateRotationSpeed();
                float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();
                float maxRotationSpeed = shipConfig.getShipConfigRotationMaxSpeed();

                switch (rotationType) {
                    case ROTATE_RIGHT:
                        currentRotationSpeed -= rotationAcceleration;
                        break;
                    case ROTATE_LEFT:
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

                new UnitOfWork() {
                    @Override
                    protected void doWork(Session session) {
                        session.merge(shipState);
                        session.merge(locationObject);
                    }
                }.execute();
            }
        }

        private class PostRotatingRunnable implements Runnable {
            @Override
            public void run() {
                float currentRotationSpeed = shipState.getShipStateRotationSpeed();
                float rotationAcceleration = shipConfig.getShipConfigRotationAcceleration();

                switch (rotationType) {
                    case ROTATE_RIGHT:
                        currentRotationSpeed += rotationAcceleration;
                        if (currentRotationSpeed > 0) {
                            currentRotationSpeed = 0;
                            stop();
                        }
                        break;
                    case ROTATE_LEFT:
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

                new UnitOfWork() {
                    @Override
                    protected void doWork(Session session) {
                        session.merge(shipState);
                        session.merge(locationObject);
                    }
                }.execute();
            }
        }
    }

    private static class MoveHandler {
        private @Setter GeProtocol.ClientAction.ClientActionType moveType;
        private ShipConfig shipConfig;
        private ShipState shipState;
        private LocationObject locationObject;
        private SpeedTask speedTask;
        private PositionTask positionTask;

        private MoveHandler(PlayerInfoHolder playerInfoHolder) {
            shipConfig = playerInfoHolder.getShipConfig();
            shipState = playerInfoHolder.getShipState();
            locationObject = playerInfoHolder.getLocationObject();

            speedTask = new SpeedTask();
            positionTask = new PositionTask();

            setMoving(false);
        }

        public void setMoving(boolean moving) {
            if (moving) {
                if (!positionTask.isAlive()) {
                    positionTask.start();
                }
                speedTask.start();
            } else {
                speedTask.stop();
            }
        }

        private class SpeedTask extends TaskRunnablePair<Runnable> implements Runnable {
            private SpeedTask() {
                super(CLIENT_ACTION_MOVE_DELAY_MILLISECONDS, null, true, true);
                setRunnable(this);
            }

            @Override
            public void run() {
                float currentMoveSpeed = shipState.getShipStateMoveSpeed();
                float moveAcceleration = shipConfig.getShipConfigMoveAcceleration();
                float maxMoveSpeed = shipConfig.getShipConfigMoveMaxSpeed();

                switch (moveType) {
                    case MOVE:
                        currentMoveSpeed += moveAcceleration;
                        break;
                    case STOP:
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

                new UnitOfWork() {
                    @Override
                    protected void doWork(Session session) {
                        session.merge(shipState);
                    }
                }.execute();
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

                new UnitOfWork() {
                    @Override
                    protected void doWork(Session session) {
                        session.merge(locationObject);
                    }
                }.execute();
            }
        }
    }
}
