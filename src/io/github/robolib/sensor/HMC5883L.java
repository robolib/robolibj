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
 * Honeywell HMC5883L I2C 3-axis compass class
 * 
 * <p>The Honeywell HMC5883L magnetoresistive sensor circuit is a trio of sensors and application specific support circuits to
 * measure magnetic fields. With power supply applied, the sensor converts any incident magnetic field in the sensitive axis
 * directions to a differential voltage output. The magnetoresistive sensors are made of a nickel-iron (Permalloy) thin-film and
 * patterned as a resistive strip element. In the presence of a magnetic field, a change in the bridge resistive elements
 * causes a corresponding change in voltage across the bridge outputs.</p>
 * <p>These resistive elements are aligned together to have a common sensitive axis (indicated by arrows in the pinout
 * diagram) that will provide positive voltage change with magnetic fields increasing in the sensitive direction. Because the
 * output is only proportional to the magnetic field component along its axis, additional sensor bridges are placed at
 * orthogonal directions to permit accurate measurement of magnetic field in any orientation.</p>
 * 
 * <p>To check the HMC5883L for proper operation, a self test feature in incorporated in which the sensor is internally excited
 * with a nominal magnetic field (in either positive or negative bias configuration). This field is then measured and reported.
 * This function is enabled and the polarity is set by bits MS[n] in the configuration register A. An internal current source
 * generates DC current (about 10 mA) from the VDD supply. This DC current is applied to the offset straps of the magnetoresistive
 * sensor, which creates an artificial magnetic field bias on the sensor. The difference of this measurement and the
 * measurement of the ambient field will be put in the data output register for each of the three axes. By using this built-in
 * function, the manufacturer can quickly verify the sensor’s full functionality after the assembly without additional test setup.
 * The self test results can also be used to estimate/compensate the sensor’s sensitivity drift due to temperature.</p>
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
     * Measurement Configuration Bits.
     * These bits define the measurement flow of the device,
     * specifically whether or not to incorporate an applied
     * bias into the measurement.
     */
    public static enum MeasurementMode {
        /**
         * Normal Measurement Config (Default)
         * In normal measurement configuration the device follows
         * normal measurement flow. The positive and negative pins
         * of the resistive load are left floating and high impedance.
         */
        kNORMAL(0x00),
        /**
         * Positive bias configuration for X, Y, and Z axes
         * In this configuration, a positive current is forced across
         * the resistive load for all three axes.
         */
        kPOSBIAS(0x01),
        /**
         * Negative bias configuration for X, Y and Z axes
         * In this configuration, a negative current is forced across
         * the resistive load for all three axes.
         */
        kNEGBIAS(0x02),
        /** Reserved */
        MODE_RES(0x03);
        
        public byte value;
        
        MeasurementMode(int val){
            value = (byte)val;
        }
    }

    /** Rate bits start */
    public static final byte HMC_RATE_BIT = (byte)4;
    /** Rate bits length */
    public static final byte HMC_RATE_LEN = (byte)3;
    
    /**
     * Data Output Rate Bits
     * These bits set the rate at which data is written to
     * all three data output registers.
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

    /** Averaging bits start */
    public static final byte HMC_AVRG_BIT = (byte)6;
    /** Averaging bits length */
    public static final byte HMC_AVRG_LEN = (byte)2;
    
    /**
     * Select number of samples averaged (1 to 8) per measurement output.
     * 00 = 1(Default); 01 = 2; 10 = 4; 11 = 8
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

    /** Configuration Register B */

    /** Gain bits start */
    public static final byte HMC_GAIN_BIT = (byte)7;
    /** Gain bits length */
    public static final byte HMC_GAIN_LEN = (byte)3;
    
    /**
     * Gain Configuration Bits
     * These bits configure the gain for the device.
     * The gain configuration is common for all channels.
     */
    public static enum Gain {
        /** +/- 0.88 Ga / 1370 Gain LSb/Gauss */
        GAIN_1370(0x00),
        /** +/- 1.30 Ga / 1090 Gain LSb/Gauss (Default) */
        GAIN_1090(0x20),
        /** +/- 1.90 Ga / 820 Gain LSb/Gauss */
        GAIN_820(0x40),
        /** +/- 2.50 Ga / 660 Gain LSb/Gauss */
        GAIN_660(0x60),
        /** +/- 4.00 Ga / 440 Gain LSb/Gauss */
        GAIN_440(0x80),
        /** +/- 4.70 Ga / 390 Gain LSb/Gauss */
        GAIN_390(0xa0),
        /** +/- 5.60 Ga / 330 Gain LSb/Gauss */
        GAIN_330(0xc0),
        /** +/- 8.10 Ga / 230 Gain LSb/Gauss */
        GAIN_230(0xe0);
        
        public byte value;
        
        Gain(int val){
            value = (byte)val;
        }
    }

    /** Mode Register */

    /** Mode bits start */
    public static final byte HMC_MODE_BIT = (byte)1;
    /** Mode bits length */
    public static final byte HMC_MODE_LEN = (byte)2;
    
    /**
     * Mode Select Bits.
     * These bits select the operation mode of this device.
     */    
    public static enum OperatingMode {
        /**
         * Continuous-Measurement Mode
         * In continuous-measurement mode, the device continuously
         * performs measurements and places the result in the data
         * register. RDY goes high when new data is placed in all
         * three registers. After a power-on or a write to the mode
         * or configuration register, the first measurement set is
         * available from all three data output registers after a
         * period of 2/fDO and subsequent measurements are available
         * at a frequency of fDO, where fDO is the frequency of data
         * output.
         */
        kCONTINUOUS(0x00),
        /** 
         * Single-Measurement Mode (Default)
         * When single-measurement mode is selected, device performs
         * a single measurement, sets RDY high and returned to idle
         * mode. Mode register returns to idle mode bit values. The
         * measurement remains in the data output register and RDY
         * remains high until the data output register is read or
         * another measurement is performed. 
         */
        kSINGLE(0x01),
        /**
         * Idle Mode (Power Saving)
         * Device is placed in idle mode.
         */
        kIDLE(0x02),
        /** Reserved */
        MODE_RES(0x03);
        
        public byte value;
        
        OperatingMode(int val){
            value = (byte)val;
        }
    }

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
