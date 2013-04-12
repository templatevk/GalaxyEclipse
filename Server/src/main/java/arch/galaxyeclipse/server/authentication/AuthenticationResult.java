package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.*;
import lombok.*;

/**
 * Result of the player authentication.
 */
public class AuthenticationResult {
    @Getter
    private boolean success;
    @Getter
    private Player player;

	public AuthenticationResult(boolean isSuccess) {
		this(isSuccess, new Player());
	}

    public AuthenticationResult(boolean isSuccess, Player player) {
        this.success = isSuccess;
        this.player = player;
    }
}
