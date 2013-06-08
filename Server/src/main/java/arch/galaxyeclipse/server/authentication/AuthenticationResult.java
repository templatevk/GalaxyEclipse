package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.Player;
import lombok.Data;

/**
 * Result of the player authentication.
 */
@Data
public class AuthenticationResult {
    private boolean success;
    private Player player;

	public AuthenticationResult(boolean isSuccess) {
		this(isSuccess, new Player());
	}

    public AuthenticationResult(boolean success, Player player) {
        this.success = success;
        this.player = player;
    }
}
