/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 */

package org.team2583.robolib.util;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;

/**
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class ConstantRunner {
    
    private final String FILE_LOCATION;
    
    
    //NOTE!!!!
    //We need to write in 8192Byte Chunks
    
    
    public ConstantRunner(String file){
        FILE_LOCATION = file;
        
        
    }
    
    
    public static void readConstantsFromFile() {
        DataInputStream dataStream;
        FileConnection dataFile;
        //byte[] buffer = new byte[];
        String content = "";
    }

}