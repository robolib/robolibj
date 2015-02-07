package edu.wpi.first.wpilibj.networktables.stream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * An object that will provide socket connections when a client connects to the server on the given port (for standard java VM)
 * 
 * @author mwills
 *
 */
public class SocketServerStreamProvider {

    private ServerSocket server = null;
    public SocketServerStreamProvider(int port) throws IOException{
            server = new ServerSocket(port);
    }

    /**
     * 
     * @return a new SimpleIOStream normally from a server
     * @throws IOException
     */
    public SimpleIOStream accept() throws IOException {
            Socket socket = server.accept();
            return new SocketStream(socket);
    }
    
    /**
     * Close the source of the IOStreams. {@link #accept()} should not be called after this is called
     * @throws IOException
     */
    public void close() throws IOException {
            if(server!=null)
                    server.close();
    }
}
