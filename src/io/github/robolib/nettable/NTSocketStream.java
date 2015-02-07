package io.github.robolib.nettable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * An SimpleIOStream that wraps an {@link InputStream} and an {@link OutputStream}
 */
public class NTSocketStream {

    private final InputStream m_is;
    private final OutputStream m_os;
    private final Socket m_socket;

    /**
     * Create a new SimpleIOStream
     * @param is
     * @param os
     */
    public NTSocketStream(String host, int port) throws IOException {
        this(new Socket(host, port));
    }
    public NTSocketStream(Socket socket) throws IOException {
        m_is = socket.getInputStream();
        m_os = socket.getOutputStream();
        m_socket = socket;
    }

    /**
     * @return the input stream for this IOStream
     */
    public InputStream getInputStream() {
        return m_is;
    }

    /**
     * @return the output stream for this IOStream
     */
    public OutputStream getOutputStream() {
        return m_os;
    }

    /**
     * completely close the stream
     */
    public void close() {
        try{
            m_is.close();
        } catch(IOException e){}
        try{
            m_os.close();
        } catch(IOException e){}
        try{
            m_socket.close();
        } catch(IOException e){}
    }

}
