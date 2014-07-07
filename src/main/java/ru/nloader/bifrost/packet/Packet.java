/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (Packet.java) is part of bifrost.
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

package ru.nloader.bifrost.packet;

import ru.nloader.bifrost.exceptions.EncodingException;
import ru.nloader.bifrost.factories.PayloadFactory;
import ru.nloader.bifrost.payload.AbstractPayload;
import ru.nloader.bifrost.payload.MapPayload;
import ru.nloader.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by asn007 on 01.07.14.
 */
public class Packet extends AbstractPacket {
    public Packet(int id) {
        super(id);
    }

    public Packet(int id, byte[] encodedPayload) throws EncodingException {
        super(id, encodedPayload);

    }

    public Packet(int id, ByteBuffer encodedPayloadBuffer) throws EncodingException {
        super(id, encodedPayloadBuffer);
    }

    public Packet(int id, HashMap<String, AbstractPayload> payloadHashMap) {
        super(id, payloadHashMap);
    }

    @Override
    public byte[] getBytes() {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream daos = new DataOutputStream(baos)) {
            daos.writeInt(this._id);
            daos.write(PayloadFactory.getInstance().serialize(new MapPayload(payloadMap)));
            return baos.toByteArray();
        } catch (Exception e) {
            Logger.logException(e);
            return new byte[0];
        }
    }
}
