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

import io.github.robolib.module.actuator.DoubleSolenoid;
import io.github.robolib.module.actuator.Solenoid;
import io.github.robolib.module.actuator.SolenoidBase;
import io.github.robolib.module.actuator.SolenoidBase.SolenoidChannel;
import io.github.robolib.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public final class SolenoidMapper implements ModuleMapper<SolenoidBase> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getModuleIdentifiers() {
        return new String[] { "solenoid", "singlesolenoid", "doublesolenoid", };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SolenoidBase createModule(String key, JSONObject data) {
        try {
            String type = data.getString("type");
            switch (type) {
                case "solenoid":
                case "singlesolenoid":
                    return new Solenoid(SolenoidChannel.values()[data.getInt("channel")]);
                case "doublesolenoid":
                    return new DoubleSolenoid(SolenoidChannel.values()[data.getInt("forward_channel")],
                            SolenoidChannel.values()[data.getInt("reverse_channel")]);
                default:
                    Logger.get(RobotMap.class)
                            .fatal("Unable to create Solenoid for '" + key + "'! NO type '" + type + "'");
                    break;
            }

        } catch (SecurityException | IllegalArgumentException | JSONException e) {
            Logger.get(RobotMap.class).fatal("Unable to create Solenoid for '" + key + "'!", e);
        }
        return null;
    }

}
