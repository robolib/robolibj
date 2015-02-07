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

package io.github.robolib.module.sensor;

import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.module.iface.I2C;
import io.github.robolib.nettable.ITable;
import io.github.robolib.util.Timer;

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
public class HMC5883L extends I2C implements LiveWindowSendable {
    
    /** Device I2C Address */
    public static final byte HMC_I2C_ADDR = (byte)0x1e;

    /** Axes scale value */
    public static final byte HMC_SCALE = (byte)0.92;

    /** Configuration Register A */
    
    /**
     * Measurement Configuration Bits.
     * These bits define the measurement flow of the device,
     * specifically whether or not to incorporate an applied
     * bias into the measurement.
     * 
     * @author noriah Reuland <vix@noriah.dev>
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
    
    /**
     * Data Output Rate Bits
     * These bits set the rate at which data is written to
     * all three data output registers.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum OutputRate {
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
        
        OutputRate(int val){
            value = (byte)val;
        }
    }

    /**
     * Select number of samples averaged (1 to 8) per measurement output.
     * 00 = 1(Default); 01 = 2; 10 = 4; 11 = 8
     * 
     * @author noriah Reuland <vix@noriah.dev>
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
    
    /**
     * Gain Configuration Bits
     * These bits configure the gain for the device.
     * The gain configuration is common for all channels.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum Gain {
        /** +/- 0.88 Ga / 1370 Gain LSb/Gauss */
        k1370(0x00),
        /** +/- 1.30 Ga / 1090 Gain LSb/Gauss (Default) */
        k1090(0x20),
        /** +/- 1.90 Ga / 820 Gain LSb/Gauss */
        k820(0x40),
        /** +/- 2.50 Ga / 660 Gain LSb/Gauss */
        k660(0x60),
        /** +/- 4.00 Ga / 440 Gain LSb/Gauss */
        k440(0x80),
        /** +/- 4.70 Ga / 390 Gain LSb/Gauss */
        k390(0xa0),
        /** +/- 5.60 Ga / 330 Gain LSb/Gauss */
        k330(0xc0),
        /** +/- 8.10 Ga / 230 Gain LSb/Gauss */
        k230(0xe0);
        
        public byte value;
        
        Gain(int val){
            value = (byte)val;
        }
    }

    /** Mode Register */
    
    /**
     * Mode Select Bits.
     * These bits select the operation mode of this device.
     * 
     * @author noriah Reuland <vix@noriah.dev>
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
    public static final byte HMC_LOCK_BIT = (byte)1;
    /** Ready Bit */
    public static final byte HMC_READY_BIT = (byte)0;


    /** Registers */
     
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
    
    /**
     * Enum representation of each of the three Axes.
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum Axis{
        X(0),
        Z(2),
        Y(4);
        public byte value;
        Axis(int val){
            value = (byte)val;
        }
    }

    private MeasurementMode m_mMode = MeasurementMode.kNORMAL;
    private OutputRate m_rate = OutputRate.k15Hz;
    private Averaging m_averaging = Averaging.k1SAMPLES;
    private Gain m_gain = Gain.k1090;
    private OperatingMode m_oMode = OperatingMode.kCONTINUOUS;
    
    private boolean m_highSpeed = false;
    
    private ITable m_table;
    
    /**
     * Create a new HMC5883L compass on I2C Port.
     * @param port
     */
    public HMC5883L(Port port){
        super(port, HMC_I2C_ADDR);
        writeRegisterA();
        writeRegisterB();
        writeModeRegister();
        Timer.delay(0.7);
    }
    
    /**
     * Get the heading around the X axis in radians.
     * This is not the direction the X axis is pointing,
     * but the value of rotation around the X axis.
     * @return the heading around the X axis.
     */
    public double getHeadingXRad(){
        double mags[] = getAllMagnitudes();
        double head = Math.atan2(mags[1], mags[2]);
        return head < 0.0 ? head + Math.PI * 2 : head;
    }
    
    /**
     * Get the heading around the X axis in degrees.
     * This is not the direction the X axis is pointing,
     * but the value of rotation around the X axis.
     * @return the heading around the X axis.
     */
    public double getHeadingXDeg(){
        return Math.toDegrees(getHeadingXRad());
    }
    
    /**
     * Get magnitude for the X Axis.
     * @return the X axis magnitude value.
     */
    public double getXMagnitude(){
        return getMagnitude(Axis.X);
    }
    
    /**
     * Get the heading around the Y axis in radians.
     * This is not the direction the Y axis is pointing,
     * but the value of rotation around the Y axis.
     * @return the heading around the Y axis.
     */
    public double getHeadingYRad(){
        double mags[] = getAllMagnitudes();
        double head = Math.atan2(mags[0], mags[2]);
        return head < 0.0 ? head + Math.PI * 2 : head;
    }
    
    /**
     * Get the heading around the Y axis in degrees.
     * This is not the direction the Y axis is pointing,
     * but the value of rotation around the Y axis.
     * @return the heading around the Y axis.
     */
    public double getHeadingYDeg(){
        return Math.toDegrees(getHeadingYRad());
    }
    
    /**
     * Get magnitude for the Y Axis.
     * @return the Y axis magnitude value.
     */
    public double getYMagnitude(){
        return getMagnitude(Axis.Y);
    }
    
    /**
     * Get the heading around the Z axis in radians.
     * This is not the direction the Z axis is pointing,
     * but the value of rotation around the Z axis.
     * @return the heading around the Z axis.
     */
    public double getHeadingZRad(){
        double mags[] = getAllMagnitudes();
        double head = Math.atan2(mags[1], mags[0]);
        return head < 0.0 ? head + Math.PI * 2 : head;
    }
    
    /**
     * Get the heading around the Z axis in degrees.
     * This is not the direction the Z axis is pointing,
     * but the value of rotation around the Z axis.
     * @return the heading around the Z axis.
     */
    public double getHeadingZDeg(){
        return Math.toDegrees(getHeadingZRad());
    }
    
    /**
     * Get magnitude for the Z Axis.
     * @return the Z axis magnitude value.
     */
    public double getZMagnitude(){
        return getMagnitude(Axis.Z);
    }
    
    /**
     * Get magnitude for givien {@link Axis}
     * @see Axis
     * @param axis the {@link Axis} to get
     * @return the given axis magnitude value.
     */
    public double getMagnitude(Axis axis){
        byte[] regs = new byte[6];
        read(HMC_REG_X_MSB, regs, 6);
        byte addr = axis.value;
        return magnitudeFromBytes(regs[addr], regs[addr+1]);
    }
    
    /**
     * Get all magnitude values. Headings are in the order of X, Y, Z.
     * @return all magnitude values.
     */
    public double[] getAllMagnitudes(){
        byte[] regs = new byte[6];
        read(HMC_REG_X_MSB, regs, 6);
        return new double[]{
                magnitudeFromBytes(regs[0], regs[1]),
                magnitudeFromBytes(regs[4], regs[5]),
                magnitudeFromBytes(regs[2], regs[3])
                };
    }
    
    /**
     * Data output register lock bit status.
     * <p>This bit is set when: <ol><li>some but not all for of the six
     * data output registers have been read</li><li>Mode register has
     * been read</li></ol></p><p>When this bit is set, the six data output
     * registers are locked and any new data will not be placed in these
     * register until one of these conditions are met: <ol><li>all six
     * bytes have been read</li><li>the mode register is changed</li>
     * <li>the measurement configuration (CRA) is changed</li><li>power
     * is reset.</li></ol></p>
     * @return the status of the lock bit.
     */
    public boolean getLock(){
        return readBit(HMC_REG_STATUS, HMC_LOCK_BIT);
    }
    
    /**
     * Get the status of the Ready Bit.
     * Set when data is written to all six data registers.
     * Cleared when device initiates a write to the data output
     * registers and after one or more of the data output registers
     * are written to. When RDY bit is clear it shall remain cleared
     * for a 250 μs. DRDY pin can be used as an alternative to
     * the status register for monitoring the device for
     * measurement data.
     * @return the status of the ready bit
     */
    public boolean getReady(){
        return readBit(HMC_REG_STATUS, HMC_READY_BIT);
    }
    
    /**
     * Turn two bytes into a magnitude
     * @param first the MSB byte
     * @param second the LSB byte
     * @return magnitude from bytes
     */
    private double magnitudeFromBytes(byte first, byte second){
        return ((first << 8) | second);
    }
    
    /**
     * Write the A configuration register
     */
    private void writeRegisterA(){
        byte val = 0;
        val |= m_mMode.value;
        val |= m_rate.value;
        val |= m_averaging.value;
        write(HMC_REG_CFG_A, val);
    }
    
    /**
     * Write the B configuration register
     */
    private void writeRegisterB(){
        byte val = 0;
        val |= m_gain.value;
        write(HMC_REG_CFG_B, val);
    }
    
    /**
     * Write the Mode configuration register
     */
    private void writeModeRegister(){
        byte val = 0;
        val |= (m_highSpeed ? 1 : 0) << 7;
        val |= m_oMode.value;
        write(HMC_REG_CFG_MODE, val);
    }
    
    /**
     * Set the {@link MeasurementMode} to use
     * @see MeasurementMode
     * @param mode the {@link MeasurementMode} to use
     */
    public void setMeasurementMode(MeasurementMode mode){
        m_mMode = mode;
        writeRegisterA();
    }
    
    /**
     * Get the {@link MeasurementMode} being used.
     * @see MeasurementMode
     * @return the {@link MeasurementMode} being used.
     */
    public MeasurementMode getMeasurementMode(){
        return m_mMode;
    }
    
    /**
     * Set the {@link OutputRate} to used.
     * @see OutputRate
     * @param rate the {@link OutputRate} to used.
     */
    public void setOutputRate(OutputRate rate){
        m_rate = rate;
        writeRegisterA();
    }
    
    /**
     * Get the {@link OutputRate} being used.
     * @see OutputRate
     * @return the {@link OutputRate} being used.
     */
    public OutputRate getOutputRate(){
        return m_rate;
    }
    
    /**
     * Set the {@link Averaging} mode to use.
     * @see Averaging
     * @param avg the {@link Averaging} mode to use.
     */
    public void setSamplesPerAverage(Averaging avg){
        m_averaging = avg;
        writeRegisterA();
    }
    
    /**
     * Get the {@link Averaging} mode being used.
     * @see Averaging
     * @return the {@link Averaging} mode being used.
     */
    public Averaging getSamplesPerAverage(){
        return m_averaging;
    }
    
    /**
     * Set the {@link Gain} to use for measurement.
     * @see Gain
     * @param gain the {@link Gain} to use.
     */
    public void setGain(Gain gain){
        m_gain = gain;
        writeRegisterB();
    }
    
    /**
     * Get the {@link Gain} used for measurements.
     * @see Gain
     * @return the {@link Gain} used for measurements.
     */
    public Gain getGain(){
        return m_gain;
    }
    
    /**
     * Set the {@link OperatingMode} to run in.
     * @see OperatingMode
     * @param mode the {@link OperatingMode} to run in.
     */
    public void setOperatingMode(OperatingMode mode){
        m_oMode = mode;
        writeModeRegister();
    }
    
    /**
     * Get the {@link OperatingMode} being run.
     * @see OperatingMode
     * @return the {@link OperatingMode} being run.
     */
    public OperatingMode getOperatingMode(){
        return m_oMode;
    }
    
    /**
     * Set whether or not to use High Speed I2C mode (3400kHz)
     * @param highSpeed use high speed or not
     */
    public void setUsingHighSpeed(boolean highSpeed){
        m_highSpeed = highSpeed;
        writeModeRegister();
    }
    
    /**
     * Get whether or not we are using High Speed I2C mode (3400kHz)
     * @return using high speed or not
     */
    public boolean getUsingHighSpeed(){
        return m_highSpeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable() {
        if(m_table != null){
            m_table.putNumber("X Heading", getHeadingXDeg());
            m_table.putNumber("Y Heading", getHeadingYDeg());
            m_table.putNumber("Z Heading", getHeadingZDeg());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "3-Axis Compass";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {}
}
