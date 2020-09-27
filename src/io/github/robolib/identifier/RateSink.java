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

package io.github.robolib.identifier;

import java.util.function.DoubleSupplier;

import io.github.robolib.command.Scheduler;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public interface RateSink {

    void setRate(double rate);

    public default void bindRate(RateSource source) {
        Scheduler.addBind(() -> setRate(source.getRate()));
    }

    public default void bindRate(DoubleSupplier source) {
        Scheduler.addBind(() -> setRate(source.getAsDouble()));
    }

}
