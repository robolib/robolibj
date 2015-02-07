package edu.wpi.first.wpilibj.networktables.connection;

import java.io.IOException;

/**
 * An exception throw when a NetworkTableNode receives a bad message
 * 
 * @author Mitchell
 *
 */
public class BadMessageException extends IOException {
	/**
     * 
     */
    private static final long serialVersionUID = 1988660783996361645L;

    /**
	 * Create a new exception
	 * @param message a message
	 */
	public BadMessageException(final String message) {
		super(message);
	}
}
