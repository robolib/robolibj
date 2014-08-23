package org.team2583.robolib.util;

/**
 * Common functions for use in the robot code
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class CommonFunctions {
    
    
    /**
     * Get an integer representation of the direction towards an angle
     * @param to angle to go to
     * @param from angle to come from
     * @return integer representation (-1/0/1) of left/none/right
     */
    public static int directionTo(double to, double from){
        if (to == from){
            return 0;
        }else if ((to >= 0 & from >= 0) | (to < 0 & from < 0)){
            return (to - from) < 0 ? -1 : 1;
        }else if (to < 0 & from >= 0){
            return ((from < 90 ? -1 : 1) * (to > -90 ? -1 : 1));
        }else{
            return ((from < -90 ? -1 : 1) * (to > 90 ? -1 : 1));
        }
    }

}