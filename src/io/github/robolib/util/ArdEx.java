/*
 * Copyright (c) 2015 noriah <vix@noriah.dev>.
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

package io.github.robolib.util;

import io.github.robolib.iface.I2C;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class ArdEx extends I2C {
    
    private static final char ARDEX_CMD_SET_PIN_MODE = 'M';
    private static final char ARDEX_CMD_DIGITAL = 'D';
    private static final char ARDEX_CMD_ANALOG = 'A';
    private static final char ARDEX_CMD_PWM = 'P';
    
    private static final char ARDEX_MODE_OUTPUT = 'O';
    
    private static final char ARDEX_JOB_WRITE = 'W';
    private static final char ARDEX_JOB_READ = 'R';
    
    public ArdEx(Port port, byte address){
        super(port, address);
    }
    
    private short ardexRead(char cmd, int pin, char job, byte val8, byte bytes){
        byte[] a = {(byte)cmd, (byte)pin, (byte)job, val8};
        byte[] result = new byte[2];
        transaction(a, 4, result, 2);
        return (short) ((result[0] << 8) + result[1]);
    }
    
    private void ardexWrite(char cmd, int pin, char job, byte var1, byte var2){
//        transaction(new byte[]{(byte)cmd, (byte)pin, (byte)job, var1, var2}, 5, null, 0);
        writeBulk(new byte[]{(byte)cmd, (byte)0, (byte)pin, (byte)job, var1, var2});
    }
    
    public void setPinAsOutput(int dpin){
        ardexWrite(ARDEX_CMD_SET_PIN_MODE, dpin, ARDEX_MODE_OUTPUT, (byte)0, (byte)0);
    }
    
    public void setPullup(int dpin, boolean lowHigh){
        ardexWrite(ARDEX_CMD_DIGITAL, dpin, ARDEX_JOB_WRITE, ((byte)(lowHigh ? 0x01 : 0x00)), (byte)0);
    }
    
    public void digitalWrite(int dpin, boolean lowHigh){
        ardexWrite(ARDEX_CMD_DIGITAL, dpin, ARDEX_JOB_WRITE, ((byte)(lowHigh ? 0x01 : 0x00)), (byte)0);
    }
    
    public void pwmWrite(int dpin, byte value){
        ardexWrite(ARDEX_CMD_PWM, dpin, ARDEX_JOB_WRITE, value, (byte)0);
    }

    public boolean digitalRead(int dpin){
        return ardexRead(ARDEX_CMD_DIGITAL, dpin, ARDEX_JOB_READ, (byte)0, (byte)1) != 0;
    }
    
    public short analogRead(int apin){
        return ardexRead(ARDEX_CMD_ANALOG, apin, ARDEX_JOB_READ, (byte)0, (byte)2);
    }
    
    public short analogReadVcc(){
        return ardexRead(ARDEX_CMD_ANALOG, 8, ARDEX_JOB_READ, (byte)0, (byte)2);
    }
    
    

}
