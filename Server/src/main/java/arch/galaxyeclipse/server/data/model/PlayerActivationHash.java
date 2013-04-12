package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "player_activation_hash", schema = "", catalog = "ge")
@Entity
public class PlayerActivationHash {
    private int playerActivationHashId;

    @Column(name = "player_activation_hash_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getPlayerActivationHashId() {
        return playerActivationHashId;
    }

    public void setPlayerActivationHashId(int playerActivationHashId) {
        this.playerActivationHashId = playerActivationHashId;
    }

    private String activationHash;

    @Column(name = "activation_hash", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getActivationHash() {
        return activationHash;
    }

    public void setActivationHash(String activationHash) {
        this.activationHash = activationHash;
    }

    private int playerId;

    @Column(name = "player_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerActivationHash that = (PlayerActivationHash) o;

        if (playerActivationHashId != that.playerActivationHashId) return false;
        if (playerId != that.playerId) return false;
        if (activationHash != null ? !activationHash.equals(that.activationHash) : that.activationHash != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerActivationHashId;
        result = 31 * result + (activationHash != null ? activationHash.hashCode() : 0);
        result = 31 * result + playerId;
        return result;
    }

    private Player player;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_activation_hash_id", referencedColumnName = "player_id", nullable = false, insertable = false, updatable = false)
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
