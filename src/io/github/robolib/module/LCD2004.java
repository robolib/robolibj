/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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

package io.github.robolib.module;

import java.util.HashMap;
import java.util.Map;

import io.github.robolib.module.iface.I2C;
import io.github.robolib.util.Timer;

/**
 * LCD CLass for LCM2004
 *
 * @see http://www.wvshare.com/datasheet/LCD_en_PDF/HD44780.pdf
 *
 * @author noriah <vix@noriah.dev>
 */
@SuppressWarnings("unused")
public class LCD2004 extends I2C {

    private static final byte I2C_ADDR = 0x27;

    private static final byte LCD_CLEARDISPLAY = 0x01;
    private static final byte LCD_RETURNHOME = 0x02;
    private static final byte LCD_ENTRYMODESET = 0x04;
    private static final byte LCD_DISPLAYCONTROL = 0x08;
    private static final byte LCD_CURSORSHIFT = 0x10;
    private static final byte LCD_FUNCTIONSET = 0x20;
    private static final byte LCD_SET_CGRAM_ADDR = 0x40;
    private static final byte LCD_SET_DDRAM_ADDR = (byte) 0x80;

    private static final byte LCD_ENTRY_RIGHT = 0x00;
    private static final byte LCD_ENTRY_LEFT = 0x02;
    private static final byte LCD_ENTRY_SHIFT_INCREMENT = 0x01;
    private static final byte LCD_ENTRY_SHIFT_DECREMENT = 0x00;

    private static final byte LCD_DISPLAY_ON = 0x04;
    private static final byte LCD_DISPLAY_OFF = 0x00;
    private static final byte LCD_CURSOR_ON = 0x02;
    private static final byte LCD_CURSOR_OFF = 0x00;
    private static final byte LCD_BLINK_ON = 0x01;
    private static final byte LCD_BLINK_OFF = 0x00;

    private static final byte LCD_DISPLAYMOVE = 0x08;
    private static final byte LCD_CURSORMOVE = 0x00;
    private static final byte LCD_MOVE_RIGHT = 0x04;
    private static final byte LCD_MOVE_LEFT = 0x00;

    private static final byte LCD_8BITMODE = 0x10;
    private static final byte LCD_4BITMODE = 0x00;
    private static final byte LCD_2LINE = 0x08;
    private static final byte LCD_1LINE = 0x00;
    private static final byte LCD_5x10DOTS = 0x04;
    private static final byte LCD_5x8DOTS = 0x00;

    private static final byte FOUR_BITS = 0x02;

    private static final double HOME_CLEAR_EXEC = 0.2;

    private static final byte REGISTER_SELECT_MASK = 0x01;
    private static final byte READ_WRITE_MASK = 0x02;
    private static final byte ENABLE_MASK = 0x04;
    private static final byte BACKLIGHT_MASK = 0x08;
    private static final byte DATA_PIN_4_MASK = 0x10;
    private static final byte DATA_PIN_5_MASK = 0x20;
    private static final byte DATA_PIN_6_MASK = 0x40;
    private static final byte DATA_PIN_7_MASK = (byte) 0x80;

    private static final byte COMMAND = 0x00;
    private static final byte DATA = REGISTER_SELECT_MASK;

    private static final Map<Byte, Byte>DATA_PIN_MASK_MAP = new HashMap<>();
    static{
        DATA_PIN_MASK_MAP.put((byte) 0x01, DATA_PIN_4_MASK);
        DATA_PIN_MASK_MAP.put((byte) 0x02, DATA_PIN_5_MASK);
        DATA_PIN_MASK_MAP.put((byte) 0x04, DATA_PIN_6_MASK);
        DATA_PIN_MASK_MAP.put((byte) 0x08, DATA_PIN_7_MASK);
    }

    private static final byte[] ROW_ADDR = {0x00, 0x40, 0x14, 0x54};

    private byte displayControl = LCD_DISPLAYCONTROL | LCD_DISPLAY_ON;
    private byte displayMode = LCD_ENTRYMODESET | LCD_ENTRY_LEFT | LCD_ENTRY_SHIFT_DECREMENT;
    private byte backlightStsMask = 0x08;

    public LCD2004(Port port){
        super(port, I2C_ADDR);

        Timer.delay(0.041);

        clear();
    }

    public void writeString(String str){
        for(char c : str.toCharArray())
            write(c);
    }


    public void clear(){
        command(LCD_CLEARDISPLAY);
        Timer.delay(0.1);
    }

    public void home(){
        command(LCD_RETURNHOME);
        Timer.delay(0.1);
    }

    public void setCursor(int line, int pos){
        if(line >= 4)
            line = line - 1;

        int address = ROW_ADDR[line] + pos;
        command(LCD_SET_DDRAM_ADDR + address);
    }

    public void noDisplay(){
        displayControl &= ~LCD_DISPLAY_ON;
        command(displayControl);
    }

    public void display(){
        displayControl |= LCD_DISPLAY_ON;
        command(displayControl);
    }

    public void noCursor(){
        displayControl &= ~LCD_CURSOR_ON;
        command(displayControl);
    }

    public void cursor(){
        displayControl |= LCD_CURSOR_ON;
        command(displayControl);
    }

    public void noBlink(){
        displayControl &= ~LCD_BLINK_ON;
        command(displayControl);
    }

    public void blink(){
        displayControl |= LCD_BLINK_ON;
        command(displayControl);
    }

    public void scrollDisplayLeft(){
        command(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | LCD_MOVE_LEFT);
    }

    public void scrollDisplayRight(){
        command(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | LCD_MOVE_LEFT);
    }

    public void leftToRight(){
        displayMode |= LCD_ENTRY_LEFT;
        command(displayMode);
    }

    public void rightToLeft(){
        displayMode &= ~LCD_ENTRY_LEFT;
        command(displayMode);
    }

    public void moveCursorRight(){
        command(LCD_CURSORSHIFT | LCD_CURSORMOVE | LCD_MOVE_RIGHT);
    }

    public void moveCursorLeft(){
        command(LCD_CURSORSHIFT | LCD_CURSORMOVE | LCD_MOVE_LEFT);
    }

    public void autoscroll(){
        displayMode |= LCD_ENTRY_SHIFT_INCREMENT;
        command(displayMode);
    }

    public void noAutoscroll(){
        displayMode &= ~LCD_ENTRY_SHIFT_INCREMENT;
        command(displayMode);
    }

    public void backlight(){
        backlightStsMask = BACKLIGHT_MASK;
        writeBulk(new byte[]{backlightStsMask});
    }

    public void noBacklight(){
        backlightStsMask = 0x00;
        writeBulk(new byte[]{backlightStsMask});
    }

    private void write(int data){
        command(displayControl);
        send(data, DATA);
    }

    private void command(int data){
        send(data, COMMAND);
    }

    private void send(int data, byte mode){
        byte first = get4bits((byte) (data >> 4), mode);
        byte second = get4bits((byte) (data & 0x0f), mode);
        writeBulk(new byte[]{
                (byte) (first | ENABLE_MASK),
                first,
                (byte) (second | ENABLE_MASK),
                second
        });
    }

    private byte get4bits(byte value, byte mode){
        final byte[] pinMapValue = new byte[1];
        DATA_PIN_MASK_MAP.forEach((Byte a, Byte b) -> {
            if((value & a) == a) pinMapValue[0] |= b;
        });

        pinMapValue[0] |= (mode | backlightStsMask);

        return pinMapValue[0];
    }

}
