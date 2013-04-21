package arch.galaxyeclipse.client.data;

import lombok.*;

/**
 *
 */
@Data
public class ShipStateInfoHolder {
    private int moveSpeed;
    private int rotationSpeed;
    private int hp;
    private int armorDurability;
    private int rotationAngle;
    private float positionX;
    private float positionY;
}
