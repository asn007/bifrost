/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (AbstractPacket.java) is part of bifrost.
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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asn007 on 01.07.14.
 */
public abstract class AbstractPacket implements IPacket {
    protected int _id;
    protected HashMap<String, AbstractPayload> payloadMap = new HashMap<>();

    public AbstractPacket(int id) {
        this._id = id;
    }

    public AbstractPacket(int id, byte[] encodedPayload) throws EncodingException {
        this(id, ByteBuffer.wrap(encodedPayload));

    }

    public AbstractPacket(int id, ByteBuffer encodedPayloadBuffer) throws EncodingException {
        this(id);
        try {
            this.payloadMap = PayloadFactory.getInstance().deserialize(encodedPayloadBuffer, MapPayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException(e);
        }
    }

    public AbstractPacket(int id, HashMap<String, AbstractPayload> payloadHashMap) {
        this(id);
        this.payloadMap = payloadHashMap;
    }

    @Override
    public int getID() {
        return _id;
    }

    @Override
    public abstract byte[] getBytes();

    @Override
    public AbstractPayload getValue(String key) {
        return payloadMap.get(key);
    }

    public <T extends AbstractPayload> T getValue(String key, Class<T> type) {
        AbstractPayload result = getValue(key);
        return (result != null) ? type.cast(result) : null;
    }

    public HashMap<String, ? extends AbstractPayload> getPayloadMap() {
        return payloadMap;
    }

    public void setPayloadMap(HashMap<String, AbstractPayload> newPayloadMap) {
        this.payloadMap = newPayloadMap;
    }

    @Override
    public void addKey(String key, AbstractPayload value) {
        payloadMap.put(key, value);
    }

    public void addAll(Map<? extends String, ? extends AbstractPayload> toPut) {
        payloadMap.putAll(toPut);
    }

    public boolean equals(Object anotherObj) {
        if(anotherObj == null) return false;
        if(!getClass().equals(anotherObj.getClass())) return false;
        if(this.hashCode() == anotherObj.hashCode()) return true;
        AbstractPacket anotherPacket = (AbstractPacket)anotherObj;
        return _id == anotherPacket.getID() && payloadMap.equals(anotherPacket.getPayloadMap());
    }
}
