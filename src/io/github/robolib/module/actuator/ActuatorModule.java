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

package io.github.robolib.module.actuator;

import io.github.robolib.module.Module;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public interface ActuatorModule extends Module {

    /**
     * Make this actuator safe.
     *
     * This should disable the module and keep it disabled.
     * There should be no way to recover from a "safe" state.
     *
     * This is here to allow for exceptions in function, to keep
     * the code resetting (switching game mode) from affecting
     * the state, in case an external sensor detects that the module
     * will cause damage to itself or others (or even people)
     *
     * To recover from this state, the code should need to be restarted.
     */
    void makeSafe();

}
