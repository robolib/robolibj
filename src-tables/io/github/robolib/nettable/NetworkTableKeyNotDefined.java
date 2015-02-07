package io.github.robolib.nettable;

import java.util.NoSuchElementException;

/**
 * An exception throw when the lookup a a key-value fails in a {@link NetworkTable}
 * @deprecated to provide backwards compatability for new api
 */
public class NetworkTableKeyNotDefined extends NoSuchElementException {

    private static final long serialVersionUID = 4535215854918583959L;

    /**
	 * @param key the key that was not defined in the table
	 */
	public NetworkTableKeyNotDefined(String key) {
		super("Unkown Table Key: "+key);
	}

}
