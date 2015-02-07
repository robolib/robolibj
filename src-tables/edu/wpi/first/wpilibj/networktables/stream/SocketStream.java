package edu.wpi.first.wpilibj.networktables.stream;

import java.io.IOException;
import java.net.Socket;

/**
 * A socket connection on a standard Java VM
 * 
 * @author mwills
 *
 */
public class SocketStream extends SimpleIOStream{

	private final Socket socket;
	public SocketStream(String host, int port) throws IOException {
		this(new Socket(host, port));
	}
	public SocketStream(Socket socket) throws IOException {
		super(socket.getInputStream(), socket.getOutputStream());
		this.socket = socket;
	}

	public void close() {
		super.close();
		try{
			socket.close();
		} catch(IOException e){}
	}

}
