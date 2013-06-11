package arch.galaxyeclipse.server.authentication;

import arch.galaxyeclipse.server.data.model.GePlayer;
import lombok.Data;

/**
 * Result of the player authentication.
 */
@Data
public class GeAuthenticationResult {

    private boolean success;
    private GePlayer player;

    public GeAuthenticationResult(boolean isSuccess) {
        this(isSuccess, new GePlayer());
    }

    public GeAuthenticationResult(boolean success, GePlayer player) {
        this.success = success;
        this.player = player;
    }
}
