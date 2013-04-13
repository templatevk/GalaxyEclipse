package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.*;

import static arch.galaxyeclipse.server.data.EntityUtils.*;

/**
 *
 */
@Table(name = "player", schema = "", catalog = "ge")
@Entity
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (activated != player.activated) return false;
        if (banned != player.banned) return false;
        if (playerId != player.playerId) return false;
        if (email != null ? !email.equals(player.email) : player.email != null) return false;
        if (nickname != null ? !nickname.equals(player.nickname) : player.nickname != null) return false;
        if (password != null ? !password.equals(player.password) : player.password != null) return false;
        if (username != null ? !username.equals(player.username) : player.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (banned ? 1 : 0);
        result = 31 * result + (activated ? 1 : 0);
        return result;
    }

    private Set<InventoryItem> inventoryItems;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    public Set<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(Set<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    private PlayerActivationHash playerActivationHash;

    @OneToOne(mappedBy = "player", fetch = FetchType.LAZY)
    public PlayerActivationHash getPlayerActivationHash() {
        return playerActivationHash;
    }

    public void setPlayerActivationHash(PlayerActivationHash playerActivationHash) {
        this.playerActivationHash = playerActivationHash;
    }

    private Set<ShipConfig> shipConfigs;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    public Set<ShipConfig> getShipConfigs() {
        return shipConfigs;
    }

    @Transient
    public ShipConfig getShipConfig() {
        return getFirst(shipConfigs);
    }

    public void setShipConfigs(Set<ShipConfig> shipConfigs) {
        this.shipConfigs = shipConfigs;
    }

    private Set<ShipState> shipStates;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    public Set<ShipState> getShipStates() {
        return shipStates;
    }

    @Transient
    public ShipState getShipState() {
        return getFirst(shipStates);
    }

    public void setShipStates(Set<ShipState> shipStates) {
        this.shipStates = shipStates;
    }
}
