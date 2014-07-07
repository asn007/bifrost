/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PacketFactory.java) is part of bifrost.
 *
 *     bifrost is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     bifrost is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.nloader.bifrost.factories;

import ru.nloader.bifrost.exceptions.EncodingException;
import ru.nloader.bifrost.helpers.ByteHelper;
import ru.nloader.bifrost.packet.Packet;
import ru.nloader.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by asn007 on 01.07.14.
 */
public class PacketFactory {
    private static PacketFactory _instance;

    public static PacketFactory getInstance() {
        if(_instance == null) _instance = new PacketFactory();
        return _instance;
    }

    public Packet deserialize(byte[] arr) {
        ByteBuffer buf = ByteBuffer.wrap(arr);
        try {
            return new Packet(buf.getInt(), buf);
        } catch (EncodingException e) {
            Logger.logException(e);
            return null;
        }
    }

    public Packet deserialize(ByteBuffer buf) {
        try {
            return new Packet(buf.getInt(), buf);
        } catch (EncodingException e) {
            Logger.logException(e);
            return null;
        }
    }

    public byte[] serialize(Packet packet) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream daos = new DataOutputStream(baos)) {
            daos.write(ByteHelper.addSizeInteger(packet.getBytes()));
            return baos.toByteArray();
        } catch(Exception e) {
            Logger.logException(e);
        }
        return new byte[0];
    }
}
