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

package io.github.robolib.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import io.github.robolib.nettable.ITable;

/**
 * Common functions for use in the robot code.
 *
 * @author noriah <vix@noriah.dev>
 */
public class Common {

    public static String getTeamNumber() {
        Runtime run = Runtime.getRuntime();
        Process proc;
        try {
            proc = run.exec("hostname");
            BufferedInputStream in = new BufferedInputStream(proc.getInputStream());
            byte[] b = new byte[256];
            in.read(b, 0, 256);
            return new String(b).trim().replace("roboRIO-", "");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Get an integer representation of the direction towards an angle.
     *
     * @param to   angle to go to
     * @param from angle to come from
     * @return integer representation (-1/0/1) of left/none/right
     */
    public static int directionTo(double to, double from) {
        if (to == from) {
            return 0;
        } else if ((to >= 0 & from >= 0) | (to < 0 & from < 0)) {
            return (to - from) < 0 ? -1 : 1;
        } else if (to < 0 & from >= 0) {
            return ((from < 90 ? -1 : 1) * (to > -90 ? -1 : 1));
        } else {
            return ((from < -90 ? -1 : 1) * (to > 90 ? -1 : 1));
        }
    }

    /**
     * Get an IntBuffer of size 4 with Little Endian Byte Order
     *
     * @return an IntBuffer of size 4 with Little Endian byte order
     */
    public static IntBuffer allocateInt() {
        ByteBuffer status = ByteBuffer.allocateDirect(4);
        status.order(ByteOrder.LITTLE_ENDIAN);
        return status.asIntBuffer();
    }

    public static ITable getTable(ITable table, String path) {
        String[] a = path.split("/");
        for (String s : a) {
            table = table.getSubTable(s);
        }
        return table;
    }

}
