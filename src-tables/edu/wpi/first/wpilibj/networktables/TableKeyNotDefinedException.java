package edu.wpi.first.wpilibj.networktables;

/**
 * An exception throw when the lookup a a key-value fails in a {@link ITable}
 */
@SuppressWarnings("deprecation")
public class TableKeyNotDefinedException extends NetworkTableKeyNotDefined {

	/**
     * 
     */
    private static final long serialVersionUID = 2833703256047837936L;

    /**
	 * @param key the key that was not defined in the table
	 */
    public TableKeyNotDefinedException(String key) {
		super(key);
	}

}
