package net.gmx.nosefish.fishylib.logger;

import net.canarymod.logger.CanaryLevel;

/**
 * Workaround for CanrayLib issue #97 
 * @author Stefan Steinheimer (nosefish)
 *
 */
public class Logman extends net.canarymod.logger.Logman {

	public Logman(String name) {
		super(name);
	}


    /**
     * Logs a debug message.
     * @param message
     */
    public void logDebug(String message) {
        log(CanaryLevel.FINE, message);
    }
}
