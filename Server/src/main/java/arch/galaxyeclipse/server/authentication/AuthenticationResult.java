package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.*;

/**
 * Result of the player authentication.
 */
public class AuthenticationResult {
    private boolean success;
    private Players player;

	public AuthenticationResult(boolean isSuccess) {
		this(isSuccess, new Players());
	}

    public AuthenticationResult(boolean isSuccess, Players player) {
        this.success = isSuccess;
        this.player = player;
    }

    public boolean isSuccess() {
        return success;
    }

    public Players getPlayer() {
        return player;
    }
}
