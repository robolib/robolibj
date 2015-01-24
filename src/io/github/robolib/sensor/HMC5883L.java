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

package io.github.robolib.sensor;

import io.github.robolib.iface.I2C;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class HMC5883L extends I2C {
    
    /** Device I2C Address */
    public static final byte HMC_I2C_ADDR = (byte)0x1e;

    /** Axes scale value */
    public static final byte HMC_SCALE = (byte)0.92;

    /** Configuration Register A */

    /** Measurement Mode bits start */
    public static final byte HMC_MMODE_BIT = (byte)1;
    /** Measurement Mode bits length */
    public static final byte HMC_MMODE_LEN = (byte)2;
    
    /**
     * Enum for setting the Measurement Mode
     */
    public static enum MeasurementMode {
        /** Normal Measurement Config (Default) */
        kNORMAL(0x00),
        /** Positive bias configuration */
        kPOSBIAS(0x01),
        /** Negative bias configuration */
        kNEGBIAS(0x02),
        /** Reserved */
        MODE_RES(0x03);
        
        public byte value;
        
        MeasurementMode(int val){
            value = (byte)val;
        }
    }
    
    /*public static final byte HMC_MMODE_NORM = (byte)0x00;
    public static final byte HMC_MMODE_POSB = (byte)0x01;
    public static final byte HMC_MMODE_NEGB = (byte)0x02;
    public static final byte HMC_MMODE_RES = (byte)0x03;*/

    /** Rate bits start */
    public static final byte HMC_RATE_BIT = (byte)4;
    /** Rate bits length */
    public static final byte HMC_RATE_LEN = (byte)3;
    
    /**
     * Enum for setting the Data Output Rate.
     */
    public static enum Rate {
        /** 0.75Hz */
        k075Hz(0x00),
        /** 1.50Hz */
        k1P5Hz(0x04),
        /** 3.00Hz */
        k3Hz(0x08),
        /** 7.50Hz */
        k7P5Hz(0x0c),
        /** 15.0Hz (Default) */
        k15Hz(0x10),
        /** 30.0Hz */
        k30Hz(0x14),
        /** 75.0Hz */
        k75Hz(0x18),
        /** Reserved */
        RATE_RES(0x1c);
        
        public byte value;
        
        Rate(int val){
            value = (byte)val;
        }
    }
    
    /*public static final byte HMC_RATE_075 = (byte)0x00;
    public static final byte HMC_RATE_1P5 = (byte)0x01;
    public static final byte HMC_RATE_3P0 = (byte)0x02;
    public static final byte HMC_RATE_7P5 = (byte)0x03;
    public static final byte HMC_RATE_15P = (byte)0x04;
    public static final byte HMC_RATE_30P = (byte)0x05;
    public static final byte HMC_RATE_75P = (byte)0x06;
    public static final byte HMC_RATE_RES = (byte)0x07;*/

    /** Averaging bits start */
    public static final byte HMC_AVRG_BIT = (byte)6;
    /** Averaging bits length */
    public static final byte HMC_AVRG_LEN = (byte)2;
    
    /**
     * Enum for setting the number of samples to average.
     */
    public static enum Averaging {
        /** 1 Sample (Default) */
        k1SAMPLES(0x00),
        /** 2 Samples */
        k2SAMPLES(0x20),
        /** 4 Samples */
        k3SAMPLES(0x40),
        /** 8 Samples */
        k4SAMPLES(0x60);
        
        public byte value;
        
        Averaging(int val){
            value = (byte)val;
        }
    }
    
    /*public static final byte HMC_AVRG_1 = (byte)0x00;
    public static final byte HMC_AVRG_2 = (byte)0x01;
    public static final byte HMC_AVRG_4 = (byte)0x02;
    public static final byte HMC_AVRG_8 = (byte)0x03;*/


    /** Configuration Register B */

    /** Gain bits start */
    public static final byte HMC_GAIN_BIT = (byte)7;
    /** Gain bits length */
    public static final byte HMC_GAIN_LEN = (byte)3;
    
    public static enum Gain {
        GAIN_1370,
    }
    /** +/- 0.88 Ga / 1370 Gain LSb/Gauss */
    public static final byte HMC_GAIN_1370 = (byte)0x00;
    /** +/- 1.30 Ga / 1090 Gain LSb/Gauss (Default) */
    public static final byte HMC_GAIN_1090 = (byte)0x01;
    /** +/- 1.90 Ga / 820 Gain LSb/Gauss */
    public static final byte HMC_GAIN_820 = (byte)0x02;
    /** +/- 2.50 Ga / 660 Gain LSb/Gauss */
    public static final byte HMC_GAIN_660 = (byte)0x03;
    /** +/- 4.00 Ga / 440 Gain LSb/Gauss */
    public static final byte HMC_GAIN_440 = (byte)0x04;
    /** +/- 4.70 Ga / 390 Gain LSb/Gauss */
    public static final byte HMC_GAIN_390 = (byte)0x05;
    /** +/- 5.60 Ga / 330 Gain LSb/Gauss */
    public static final byte HMC_GAIN_330 = (byte)0x06;
    /** +/- 8.10 Ga / 230 Gain LSb/Gauss */
    public static final byte HMC_GAIN_230 = (byte)0x07;


    /** Mode Register */

    /** Mode bits start */
    public static final byte HMC_MODE_BIT = (byte)1;
    /** Mode bits length */
    public static final byte HMC_MODE_LEN = (byte)2;
    /**
     * Enum for setting the Operating Mode
     */
    
    public static enum OperatingMode {
        /** Continuous-Measurement Mode */
        kCONTINUOUS,
        /** Single-Measurement Mode (Default) */
        kSINGLE,
        /** Idle Mode (Power Saving) */
        kIDLE,
        /** Reserved */
        MODE_RES;
    }
    
    /*public static final byte HMC_MODE_CONTINUOUS = (byte)0x00;
    public static final byte HMC_MODE_SINGLE = (byte)0x01;
    public static final byte HMC_MODE_IDLE = (byte)0x02;*/

    /**
     * Status Register
     */

    /** Data output register lock */
    public static final byte HMC_LOCK_BIT = (byte)2;
    /** Ready Bit */
    public static final byte HMC_RDY_BIT = (byte)1;


    /**
     * Read Registers
     */
     
    /** Configuration Register A */
    public static final byte HMC_REG_CFG_A = (byte)0x00;
    /** Configuration Register B */
    public static final byte HMC_REG_CFG_B = (byte)0x01;
    /** Mode Register */
    public static final byte HMC_REG_CFG_MODE = (byte)0x02;
    /** Data Output X MSB Register */
    public static final byte HMC_REG_X_MSB = (byte)0x03;
    /** Data Output X LSB Register */
    public static final byte HMC_REG_X_LSB = (byte)0x04;
    /** Data Output Z MSB Register */
    public static final byte HMC_REG_Z_MSB = (byte)0x05;
    /** Data Output Z LSB Register */
    public static final byte HMC_REG_Z_LSB = (byte)0x06;
    /** Data Output Y MSB Register */
    public static final byte HMC_REG_Y_MSB = (byte)0x07;
    /** Data Output Y LSB Register */
    public static final byte HMC_REG_Y_LSB = (byte)0x08;
    /** Status Register */
    public static final byte HMC_REG_STATUS = (byte)0x09;
    /** Identification Register A */
    public static final byte HMC_REG_IDA = (byte)0x0A;
    /** Identification Register B */
    public static final byte HMC_REG_IDB = (byte)0x0B;
    /** Identification Register C */
    public static final byte HMC_REG_IDC = (byte)0x0C;

    private MeasurementMode m_mMode;
    private Rate m_rate;
    private Averaging m_averaging;
    private Gain m_gain;
    private OperatingMode m_oMode;
    
    public HMC5883L(Port port){
        super(port, HMC_I2C_ADDR);
        
        
    }
    
    public void setMeasurementMode(MeasurementMode mode){
        m_mMode = mode;
    }
    
    public MeasurementMode getMeasurementMode(){
        return m_mMode;
    }
    
    public void setOutputRate(Rate rate){
        m_rate = rate;
    }
    
    public Rate getOutputRate(){
        return m_rate;
    }
    
    public void setSamplesPerAverage(Averaging avg){
        m_averaging = avg;
    }
    
    public Averaging getSamplesPerAverage(){
        return m_averaging;
    }
    
    public void setGain(Gain gain){
        m_gain = gain;
    }
    
    public Gain getGain(){
        return m_gain;
    }
    
    public void setOperatingMode(OperatingMode mode){
        m_oMode = mode;
    }
    
    public OperatingMode getOperatingMode(){
        return m_oMode;
    }
    
    
    
}
