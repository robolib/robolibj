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

package io.github.robolib.iface;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.hal.AnalogJNI;
import io.github.robolib.hal.HALUtil;
import io.github.robolib.identifier.NumberSource;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.livewindow.LiveWindowSendable;
import io.github.robolib.pid.PIDSource;
import io.github.robolib.util.Timer;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class AnalogInput extends AnalogIO implements PIDSource, LiveWindowSendable, NumberSource {
    
    /**
     * Analog Trigger Type Enum
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public static enum AnalogTriggerType {
        IN_WINDOW,
        STATE,
        RISING_PULSE,
        FALLING_PULSE;
    }
    
    /**
     * Accumulator class 
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
    public class AccumulatorResult {
        public long value;
        public long count;
    }
    
    private static final int ACCUMULATOR_SLOT = 1;
    private static final int[] ACCUMULATOR_CHANNELS = { 0, 1 };

    protected ITable m_table;

    private long m_accumulatorOffset;
    
    private ByteBuffer m_triggerPort;
    private int m_triggerIndex;
    
    
    /**
     * Construct an analog channel.
     *
     * @param channel The AnalogChannel to represent. 0-3 are on-board 4-7 are on the MXP port.
     */
    public AnalogInput(AnalogChannel channel){
        super(channel, Direction.IN);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void free(){
        if(m_triggerPort != null){
            IntBuffer status = getLE4IntBuffer();
            AnalogJNI.cleanAnalogTrigger(m_triggerPort, status);
            HALUtil.checkStatus(status);
        }
        super.free();
        m_accumulatorOffset = 0;
    }

    /**
     * Get a sample straight from this channel. The sample is a 12-bit value
     * representing the 0V to 5V range of the A/D converter. The units are in
     * A/D converter codes. Use GetVoltage() to get the analog value in
     * calibrated units.
     *
     * @return A sample straight from this channel.
     */
    public int getValue(){
        IntBuffer status = getLE4IntBuffer();
        int value = AnalogJNI.getAnalogValue(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Get a sample from the output of the oversample and average engine for
     * this channel. The sample is 12-bit + the bits configured in
     * SetOversampleBits(). The value configured in setAverageBits() will cause
     * this value to be averaged 2^bits number of samples. This is not a
     * sliding window. The sample will not change until 2^(OversampleBits +
     * AverageBits) samples have been acquired from this channel. Use
     * getAverageVoltage() to get the analog value in calibrated units.
     *
     * @return A sample from the oversample and average engine for this channel.
     */
    public int getAverageValue(){
        IntBuffer status = getLE4IntBuffer();
        int value = AnalogJNI.getAnalogAverageValue(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Get a scaled sample straight from this channel. The value is scaled to
     * units of Volts using the calibrated scaling data from getLSBWeight() and
     * getOffset().
     *
     * @return A scaled sample straight from this channel.
     */
    public double getVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double value = AnalogJNI.getAnalogVoltage(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Get a scaled sample from the output of the oversample and average engine
     * for this channel. The value is scaled to units of Volts using the
     * calibrated scaling data from getLSBWeight() and getOffset(). Using
     * oversampling will cause this value to be higher resolution, but it will
     * update more slowly. Using averaging will cause this value to be more
     * stable, but it will update more slowly.
     *
     * @return A scaled sample from the output of the oversample and average
     *       engine for this channel.
     */
    public double getAverageVoltage(){
        IntBuffer status = getLE4IntBuffer();
        double value = AnalogJNI.getAnalogAverageVoltage(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * {@inheritDoc}
     */
    public double get(){
        return getAverageVoltage();
    }

    /**
     * Get the factory scaling least significant bit weight constant. The least
     * significant bit weight constant for the channel that was calibrated in
     * manufacturing and stored in an eeprom.
     *
     * Volts = ((LSB_Weight * 1e-9) * raw) - (Offset * 1e-9)
     *
     * @return Least significant bit weight.
     */
    public long getLSBWeight(){
        IntBuffer status = getLE4IntBuffer();
        long value = AnalogJNI.getAnalogLSBWeight(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Get the factory scaling offset constant. The offset constant for the
     * channel that was calibrated in manufacturing and stored in an eeprom.
     *
     * Volts = ((LSB_Weight * 1e-9) * raw) - (Offset * 1e-9)
     *
     * @return Offset constant.
     */
    public int getOffset(){
        IntBuffer status = getLE4IntBuffer();
        int value = AnalogJNI.getAnalogOffset(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Set the number of averaging bits. This sets the number of averaging bits.
     * The actual number of averaged samples is 2^bits. The averaging is done
     * automatically in the FPGA.
     *
     * @param bits
     *          The number of averaging bits.
     */
    public void setAverageBits(final int bits){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogAverageBits(m_port, bits, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Get the number of averaging bits. This gets the number of averaging bits
     * from the FPGA. The actual number of averaged samples is 2^bits. The
     * averaging is done automatically in the FPGA.
     *
     * @return The number of averaging bits.
     */
    public int getAverageBits(){
        IntBuffer status = getLE4IntBuffer();
        int value = AnalogJNI.getAnalogAverageBits(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Set the number of oversample bits. This sets the number of oversample
     * bits. The actual number of oversampled values is 2^bits. The
     * oversampling is done automatically in the FPGA.
     *
     * @param bits
     *          The number of oversample bits.
     */
    public void setOversampleBits(final int bits){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogOversampleBits(m_port, bits, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Get the number of oversample bits. This gets the number of oversample
     * bits from the FPGA. The actual number of oversampled values is 2^bits.
     * The oversampling is done automatically in the FPGA.
     *
     * @return The number of oversample bits.
     */
    public int getOversampleBits(){
        IntBuffer status = getLE4IntBuffer();
        int value = AnalogJNI.getAnalogOversampleBits(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Initialize the accumulator.
     */
    public void initAccumulator(){
        if(isAccumulatorChannel()){
            throw new ResourceAllocationException(
                    "Accumulators or only available on slot"
                            + ACCUMULATOR_SLOT + " on channels "
                            + ACCUMULATOR_CHANNELS[0] + ","
                            + ACCUMULATOR_CHANNELS[1]);
        }
        m_accumulatorOffset = 0;
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.initAccumulator(m_port, status);
        HALUtil.checkStatus(status);
    }

    /**
     * Set an initial value for the accumulator.
     *
     * This will be added to all values returned to the user.
     *
     * @param initialValue
     *          The value that the accumulator should start from when reset.
     */
    public void setAccumulatorInitialValue(long initialValue){
        m_accumulatorOffset = initialValue;
    }
    
    /**
     * Resets the accumulator to the initial value.
     */
    public void resetAccumulator(){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.resetAccumulator(m_port, status);
        HALUtil.checkStatus(status);
        final double sampleTime = 1.0 / getGlobalSampleRate();
        final double overSamples = 1 << getOversampleBits();
        final double averageSamples = 1 << getAverageBits();
        Timer.delay(sampleTime * overSamples * averageSamples);
    }
    
    /**
     * Set the center value of the accumulator.
     *
     * The center value is subtracted from each A/D value before it is added to
     * the accumulator. This is used for the center value of devices like gyros
     * and accelerometers to take the device offset
     * into account when integrating.
     *
     * This center value is based on the output of the oversampled and averaged
     * source the accumulator channel. Because of this, any non-zero oversample bits will
     * affect the size of the value for this field.
     * @param center the center of the accumulator
     */
    public void setAccumulatorCenter(int center){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAccumulatorCenter(m_port, center, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Set the accumulator's deadband.
     * @param deadband The deadband size in ADC codes (12-bit value)
     */
    public void setAccumulatorDeadband(int deadband){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAccumulatorDeadband(m_port, deadband, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Read the accumulated value.
     *
     * Read the value that has been accumulating. The accumulator
     * is attached after the oversample and average engine.
     *
     * @return The 64-bit value accumulated since the last Reset().
     */
    public long getAccumulatorValue(){
        IntBuffer status = getLE4IntBuffer();
        long value = AnalogJNI.getAccumulatorValue(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Read the number of accumulated values.
     *
     * Read the count of the accumulated values since the accumulator was last
     * Reset().
     *
     * @return The number of times samples from the channel were accumulated.
     */
    public long getAccumulatorCount(){
        IntBuffer status = getLE4IntBuffer();
        long value = AnalogJNI.getAccumulatorCount(m_port, status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    /**
     * Read the accumulated value and the number of accumulated values
     * atomically.
     *
     * This function reads the value and count from the FPGA atomically. This
     * can be used for averaging.
     *
     * @param result AccumulatorResult object to store the results in.
     */
    public void getAccumulatorOutput(AccumulatorResult result){
        if(result == null) throw new IllegalArgumentException("Result cannot be NULL!");
        if(!isAccumulatorChannel()) throw new IllegalArgumentException(m_channel.name() + " is not an accumulator channel.");
        
        ByteBuffer value = ByteBuffer.allocateDirect(8);
        value.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer count = getLE4IntBuffer();
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.getAccumulatorOutput(m_port, value.asLongBuffer(), count, status);
        result.value = value.asLongBuffer().get(0) + m_accumulatorOffset;
        result.count = count.get(0);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Is the channel attached to an accumulator.
     *
     * @return The analog channel is attached to an accumulator.
     */
    public boolean isAccumulatorChannel(){
        int chNum = m_channel.ordinal();
        for(int i : ACCUMULATOR_CHANNELS){
            if(i == chNum) return true;
        }
        
        return false;
    }

    /**
     * Set the sample rate per channel.
     *
     * This is a global setting for all channels.
     * The maximum rate is 500kS/s divided by the number of channels in use.
     * This is 62500 samples/s per channel if all 8 channels are used.
     * @param samples The number of samples per second.
     */
    public static void setGlobalSampleRate(final double samples){
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogSampleRate((float)samples, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Get the current sample rate.
     *
     * This assumes one entry in the scan list. This is a global setting for
     * all channels.
     *
     * @return Sample rate.
     */
    public static double getGlobalSampleRate(){
        IntBuffer status = getLE4IntBuffer();
        double value = AnalogJNI.getAnalogSampleRate(status);
        HALUtil.checkStatus(status);
        return value;
    }
    
    public void initTrigger(){
        IntBuffer status = getLE4IntBuffer();
        IntBuffer index = getLE4IntBuffer();
        m_triggerPort = AnalogJNI.initializeAnalogTrigger(m_portPointer, index, status);
        HALUtil.checkStatus(status);
        m_triggerIndex = index.get(0);
        UsageReporting.report(UsageReporting.ResourceType_AnalogTrigger, m_channel.ordinal());
    }
    
    /**
     * Set the upper and lower limits of the analog trigger. The limits are
     * given in ADC codes. If oversampling is used, the units must be scaled
     * appropriately.
     *
     * @param lower the lower raw limit
     * @param upper the upper raw limit
     */
    public void setLimitsRaw(final int lower, final int upper){
        validateTrigger();
        if(lower > upper)
            throw new IllegalArgumentException("Lower bound is greater than upper");
        
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogTriggerLimitsRaw(m_triggerPort, lower, upper, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Set the upper and lower limits of the analog trigger. The limits are
     * given as floating point voltage values.
     *
     * @param lower the lower voltage limit
     * @param upper the upper voltage limit
     */
    public void setLimitsVoltage(final double lower, final double upper){
        validateTrigger();
        if(lower > upper)
            throw new IllegalArgumentException("Lower bound is greater than upper");
            
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogTriggerLimitsVoltage(m_triggerPort, lower, upper, status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Configure the analog trigger to use the averaged vs. raw values. If the
     * value is true, then the averaged value is selected for the analog
     * trigger, otherwise the immediate value is used.
     *
     * @param useAveraged true to use an averaged value, false otherwise
     */
    public void setAveraged(boolean useAveraged){
        validateTrigger();
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogTriggerAveraged(m_triggerPort, (byte)(useAveraged?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Configure the analog trigger to use a filtered value. The analog trigger
     * will operate with a 3 point average rejection filter. This is designed to
     * help with 360 degree pot applications for the period where the pot
     * crosses through zero.
     *
     * @param useFiltered true to use a filterd value, false otherwise
     */
    public void setFiltered(boolean useFiltered){
        validateTrigger();
        IntBuffer status = getLE4IntBuffer();
        AnalogJNI.setAnalogTriggerFiltered(m_triggerPort, (byte)(useFiltered?1:0), status);
        HALUtil.checkStatus(status);
    }
    
    /**
     * Return the index of the analog trigger. This is the FPGA index of this
     * analog trigger instance.
     *
     * @return The index of the analog trigger.
     */
    public int getTriggerIndex(){
        validateTrigger();
        return m_triggerIndex;
    }
    
    /**
     * Return the InWindow output of the analog trigger. True if the analog
     * input is between the upper and lower limits.
     *
     * @return The InWindow output of the analog trigger.
     */
    public boolean getInWindow(){
        validateTrigger();
        IntBuffer status = getLE4IntBuffer();
        byte value = AnalogJNI.getAnalogTriggerInWindow(m_triggerPort, status);
        HALUtil.checkStatus(status);
        return value != 0;
    }
    
    /**
     * Return the TriggerState output of the analog trigger. True if above upper
     * limit. False if below lower limit. If in Hysteresis, maintain previous
     * state.
     *
     * @return The TriggerState output of the analog trigger.
     */
    public boolean getTriggerState(){
        IntBuffer status = getLE4IntBuffer();
        byte value = AnalogJNI.getAnalogTriggerTriggerState(m_triggerPort, status);
        HALUtil.checkStatus(status);
        return value != 0;
    }
    
    /**
     * Creates an AnalogTriggerOutput object. Gets an output object that can be
     * used for routing. Caller is responsible for deleting the
     * AnalogTriggerOutput object.
     *
     * @param type An enum of the type of output object to create.
     * @return A new AnalogTriggerOutput object.
     */
    public AnalogTriggerOutput createTriggerOutput(final AnalogTriggerType type){
        return new AnalogTriggerOutput(){
            public int getChannelNumber(){
                return (m_triggerIndex << 2) + type.ordinal();
            }
            public byte getModuleNumber(){
                return (byte)(m_triggerIndex >> 2);
            }
            @Override
            public boolean get() {
                IntBuffer status = getLE4IntBuffer();
                byte value = AnalogJNI.getAnalogTriggerOutput(m_triggerPort, type.ordinal(), status);
                HALUtil.checkStatus(status);
                return value != 0;
            }
        };
    }
    
    /**
     * validate that the trigger pointer is not null
     */
    private void validateTrigger(){
        if(m_triggerPort == null)
            throw new IllegalStateException("Trigger not initialized. Call \"initTrigger()\" first.");
    }
    
    /**
     * Get the average voltage for use with PIDController
     *
     * @return the average voltage
     */
    @Override
    public double pidGet(){
        return getAverageVoltage();
    }
    
    /**
     * Live Window code, only does anything if live window is activated.
     */
    @Override
    public String getSmartDashboardType() {
        return "Analog Input";
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
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getAverageVoltage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * Analog Channels don't have to do anything special when entering the
     * LiveWindow.
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {}

    /**
     * Analog Channels don't have to do anything special when exiting the
     * LiveWindow.
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {}
    

}
