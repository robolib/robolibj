package edu.wpi.first.wpilibj.networktables.server;

/**
 * Represents the state of a connection to the server
 * 
 * @author Mitchell
 *
 */
public class ConnectionState {
	/**
	 * represents that the server has received the connection from the client but has not yet received the client hello
	 */
	public static final ConnectionState GOT_CONNECTION_FROM_CLIENT = new ConnectionState("GOT_CONNECTION_FROM_CLIENT");
	/**
	 * represents that the client is in a connected non-error state
	 */
	public static final ConnectionState CONNECTED_TO_CLIENT = new ConnectionState("CONNECTED_TO_CLIENT");
	/**
	 * represents that the client has disconnected from the server
	 */
	public static final ConnectionState CLIENT_DISCONNECTED = new ConnectionState("CLIENT_DISCONNECTED");
	
	/**
	 * Represents that the client is in an error state
	 * 
	 * @author Mitchell
	 *
	 */
	public static class Error extends ConnectionState{
		private final Exception e;
		/**
		 * Create a new error state
		 * @param e
		 */
		public Error(final Exception e){
			super("SERVER_ERROR");
			this.e = e;
		}
		public String toString(){
			return "SERVER_ERROR: "+e.getClass()+": "+e.getMessage();
		}
		/**
		 * @return the exception that caused the client connection to enter an error state
		 */
		public Exception getException(){
			return e;
		}
	}
	
	private String name;
	protected ConnectionState(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
}
