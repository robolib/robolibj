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

import java.io.BufferedInputStream;
import java.io.IOException;

import io.github.robolib.communication.NetworkCommunications;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class TeamInfo {
    
    public static enum Alliance {
        RED,
        BLUE,
        NONE;
    }
    
    public static enum StationID {
        RED1,
        RED2,
        RED3,
        BLUE1,
        BLUE2,
        BLUE3,
        NONE;
    }
    
    public static String getTeamNumber(){
        Runtime run = Runtime.getRuntime();
        Process proc;
        try {
            proc = run.exec("hostname");
            BufferedInputStream in = new BufferedInputStream(proc.getInputStream());
            byte [] b = new byte[256];
            in.read(b, 0, 256);
            return new String(b).trim().replace("roboRIO-", "");
        } catch(IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    
    public static Alliance getAlliance(){
        int sID = NetworkCommunications.HALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return Alliance.NONE;
        }
        return Alliance.values()[sID / 3];
    }
    
    public static StationID getStation(){
        int sID = NetworkCommunications.HALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return StationID.NONE;
        }
        return StationID.values()[sID];
    }

}
