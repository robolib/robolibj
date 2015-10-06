/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

import io.github.robolib.module.iface.DigitalIO.DigitalChannel;
import io.github.robolib.module.sensor.LimitSwitch;
import io.github.robolib.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LimitSwitchMapper implements ModuleMapper<LimitSwitch> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LimitSwitch createModule(String key, JSONObject data) {
        try {
            return new LimitSwitch(
                    DigitalChannel.values()[data.getInt("channel")],
                    LimitSwitch.SwitchType.valueOf(data.getString("circuit_type").toUpperCase()));
        } catch (IllegalArgumentException | SecurityException | JSONException e) {
            Logger.get(RobotMap.class).fatal("Unable to create LimitSwitch for key '" + key + "'", e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getModuleIdentifiers() {
        return new String[]{
                "limit_switch",
                "limitswitch",
                "lswitch",
                "switch"
        };
    }

}
