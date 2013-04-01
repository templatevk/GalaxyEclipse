package arch.galaxyeclipse.server.data.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class Players {
    private int playerId;

    @Column(name = "player_id")
    @Id
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    private String username;

    @Column(name = "username")
    @Basic
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    @Column(name = "password")
    @Basic
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String nickname;

    @Column(name = "nickname")
    @Basic
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String email;

    @Column(name = "email")
    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private boolean isBanned;

    @Column(name = "is_banned")
    @Basic
    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    private boolean isActivated;

    @Column(name = "is_activated")
    @Basic
    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Players players = (Players) o;

        if (isActivated != players.isActivated) return false;
        if (isBanned != players.isBanned) return false;
        if (playerId != players.playerId) return false;
        if (email != null ? !email.equals(players.email) : players.email != null) return false;
        if (nickname != null ? !nickname.equals(players.nickname) : players.nickname != null) return false;
        if (password != null ? !password.equals(players.password) : players.password != null) return false;
        if (username != null ? !username.equals(players.username) : players.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (isBanned ? 1 : 0);
        result = 31 * result + (isActivated ? 1 : 0);
        return result;
    }
}
