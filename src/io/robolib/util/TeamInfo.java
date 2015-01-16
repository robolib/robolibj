/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package io.robolib.util;

import io.robolib.communication.FRCNetworkCommunicationsLibrary;

/**
 * 
 * @author Austin Reuland <amreuland@gmail.com>
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
    
    public static Alliance getAlliance(){
        int sID = FRCNetworkCommunicationsLibrary.NativeHALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return Alliance.NONE;
        }
        return Alliance.values()[sID / 3];
    }
    
    public static StationID getStation(){
        int sID = FRCNetworkCommunicationsLibrary.NativeHALGetAllianceStation();
        if(!MathUtils.inBounds(sID, 0, 5)){
            return StationID.NONE;
        }
        return StationID.values()[sID];
    }

}
