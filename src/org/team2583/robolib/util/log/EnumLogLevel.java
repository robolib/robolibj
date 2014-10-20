
package org.team2583.robolib.util.log;

/**
 * A Logging class.
 * 
 * <p>Each Class that wishes to log must call the {@code Logger.get} method with itself
 * as the only argument, or with an optional string value. This will return a new Logger
 * object.</p>
 * 
 * <p>To log something, the Calling class must call the desired level method
 * {@code info, debug, warn, error, fatal} with the message to be logged. If the
 * calling the err or fatal classes, a Throwable object must be passed as a second
 * argument. Calling the {@code error} method will only print the message and
 * error stack trace. However, calling the {@code fatal} method will print the
 * stack trace, and kill the program.</p>
 * 
 * @see ILogger
 * @see Logger
 * @see LogOutput
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 */
public enum EnumLogLevel {
   
    INFO("[INFO]"),
    DEBUG("[DEBUG]"),
    WARN("[WARNING]"),
    ERROR("[ERROR]"),
    SEVERE("[SEVERE]"),
    FATAL("[FATALITY]");
    
    public final String m_name;
        
    private EnumLogLevel(String name){
        m_name = name;
    }

}
