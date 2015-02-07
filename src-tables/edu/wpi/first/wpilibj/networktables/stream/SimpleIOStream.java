package edu.wpi.first.wpilibj.networktables.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An SimpleIOStream that wraps an {@link InputStream} and an {@link OutputStream}
 * @author Mitchell
 *
 */
public class SimpleIOStream {

	private final InputStream is;
	private final OutputStream os;

	/**
	 * Create a new SimpleIOStream
	 * @param is
	 * @param os
	 */
	public SimpleIOStream(final InputStream is, final OutputStream os){
		this.is = is;
		this.os = os;
	}

	/**
     * @return the input stream for this IOStream
     */
	public InputStream getInputStream() {
		return is;
	}

	/**
     * @return the output stream for this IOStream
     */
	public OutputStream getOutputStream() {
		return os;
	}

	/**
     * completely close the stream
     */
	public void close() {
		try{
			is.close();
		} catch(IOException e){
			//just ignore the error and close the output stream
		}
		try{
			os.close();
		} catch(IOException e){
			//just ignore the error and assume everything is now closed
		}
	}

}
