package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.*;
import lombok.*;

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

    public AuthenticationResult(boolean isSuccess, Player player) {
        this.success = isSuccess;
        this.player = player;
    }
}
