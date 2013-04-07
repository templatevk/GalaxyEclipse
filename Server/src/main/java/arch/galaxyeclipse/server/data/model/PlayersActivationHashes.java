package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "players_activation_hashes", schema = "", catalog = "ge")
@Entity
public class PlayersActivationHashes {
    private int playersActivationHashesId;

    @Column(name = "players_activation_hashes_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getPlayersActivationHashesId() {
        return playersActivationHashesId;
    }

    public void setPlayersActivationHashesId(int playersActivationHashesId) {
        this.playersActivationHashesId = playersActivationHashesId;
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

        PlayersActivationHashes that = (PlayersActivationHashes) o;

        if (playerId != that.playerId) return false;
        if (playersActivationHashesId != that.playersActivationHashesId) return false;
        if (activationHash != null ? !activationHash.equals(that.activationHash) : that.activationHash != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playersActivationHashesId;
        result = 31 * result + (activationHash != null ? activationHash.hashCode() : 0);
        result = 31 * result + playerId;
        return result;
    }
}
