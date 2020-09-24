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

package io.github.robolib.util.mapper;

import io.github.robolib.module.PDP.PowerChannel;
import io.github.robolib.module.actuator.CANJaguar;

import org.json.JSONObject;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public class CANJaguarMapper implements ModuleMapper<CANJaguar> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CANJaguar createModule(String key, JSONObject data) {
        int channel = data.getInt("device_channel");
        String description = data.getString("description");
        int pChannel = data.getInt("power_channel");

        return new CANJaguar(
                channel, description,
                PowerChannel.values()[pChannel]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getModuleIdentifiers() {
        return new String[]{
                "canjaguar",
                "can_jaguar",
                "canjag",
                "can_jag"
        };
    }

}
