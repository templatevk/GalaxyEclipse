package arch.galaxyeclipse.server.authentication;

/**
 * Result of the player authentication.
 */
public class AuthenticationResult {
	// Submited credentials exist
	public final boolean isSuccess;

	public AuthenticationResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
