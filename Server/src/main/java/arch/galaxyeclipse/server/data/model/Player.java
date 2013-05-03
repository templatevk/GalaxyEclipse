package arch.galaxyeclipse.server.data.model;

import arch.galaxyeclipse.shared.util.*;

import javax.persistence.*;
import java.util.*;

/**
 *
 */
@Table(name = "player", schema = "", catalog = "ge")
@Entity
@NamedQuery(name = "player.startupInfo", query =
        "select p from Player p inner join fetch p.shipState ss " +
        "inner join fetch p.locationObject lo inner join fetch lo.location l " +
        "left outer join fetch p.inventoryItems ii left outer join fetch ii.item " +
        "inner join fetch p.shipConfig sc inner join fetch sc.shipType st " +
        "inner join fetch sc.engine e left outer join fetch sc.shipConfigBonusSlots scbs " +
        "left outer join scbs.item left outer join fetch sc.shipConfigWeaponSlots scws " +
        "left outer join fetch scws.item where p.playerId = :playerId")
public class Player {
    private int playerId;

    @Column(name = "player_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    private String username;

    @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 64, precision = 0)
    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String nickname;

    @Column(name = "nickname", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String email;

    @Column(name = "email", nullable = false, insertable = true, updatable = true, length = 64, precision = 0)
    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private boolean banned;

    @Column(name = "banned", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    @Basic
    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    private boolean activated;

    @Column(name = "activated", nullable = true, insertable = true, updatable = true, length = 1, precision = 0)
    @Basic
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    private int shipStateId;

    @Column(name = "ship_state_id", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateId() {
        return shipStateId;
    }

    public void setShipStateId(int shipStateId) {
        this.shipStateId = shipStateId;
    }

    private int shipConfigId;

    @Column(name = "ship_config_id", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigId() {
        return shipConfigId;
    }

    public void setShipConfigId(int shipConfigId) {
        this.shipConfigId = shipConfigId;
    }

    private int locationObjectId;

    @Column(name = "location_object_id", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationObjectId() {
        return locationObjectId;
    }

    public void setLocationObjectId(int locationObjectId) {
        this.locationObjectId = locationObjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (playerId != player.playerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return playerId;
    }

    private Set<InventoryItem> inventoryItems;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    public Set<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(Set<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    private Set<PlayerActivationHash> playerActivationHashes;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    public Set<PlayerActivationHash> getPlayerActivationHashes() {
        return playerActivationHashes;
    }

    public void setPlayerActivationHashes(Set<PlayerActivationHash> playerActivationHashes) {
        this.playerActivationHashes = playerActivationHashes;
    }

    private ShipState shipState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_state_id", referencedColumnName = "ship_state_id", nullable = true, insertable = false, updatable = false)
    public ShipState getShipState() {
        return shipState;
    }

    public void setShipState(ShipState shipState) {
        this.shipState = shipState;
    }

    private ShipConfig shipConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_config_id", referencedColumnName = "ship_config_id", nullable = true, insertable = false, updatable = false)
    public ShipConfig getShipConfig() {
        return shipConfig;
    }

    public void setShipConfig(ShipConfig shipConfig) {
        this.shipConfig = shipConfig;
    }

    private LocationObject locationObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_object_id", referencedColumnName = "location_object_id", nullable = true, insertable = false, updatable = false)
    public LocationObject getLocationObject() {
        return locationObject;
    }

    public void setLocationObject(LocationObject locationObject) {
        this.locationObject = locationObject;
    }
}
