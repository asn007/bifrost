/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (MapPayload.java) is part of bifrost.
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

package ru.nloader.bifrost.payload;


import ru.nloader.bifrost.factories.PayloadFactory;
import ru.nloader.bifrost.helpers.ByteHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asn007 on 30.06.2014.
 */
public class MapPayload extends AbstractPayload<HashMap<String, AbstractPayload>> {

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.MAP;
    }

    public MapPayload(HashMap<String, AbstractPayload> value) {
        super(value);
    }

    @Override
    public byte[] getBytes() {
        int mapSize = value.size();
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream daos = new DataOutputStream(baos)) {
            daos.writeInt(mapSize);
            for(Map.Entry<String, AbstractPayload> entry: value.entrySet()) {
                // write struct of int: strlen + byte[] actual string (UTF-8)
                daos.write(ByteHelper.addSizeInteger(ByteHelper.getBytesFromType(entry.getKey())));
                // serialize and write map value
                daos.write(PayloadFactory.getInstance().serialize(entry.getValue()));
                // just a zero byte indicating that there's no more data to read in that key-value pair
                daos.write((byte)0);
            }
            return baos.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public boolean isSizeIntegerRequired() {
        return true;
    }

    @Override
    public boolean equals(Object anotherObj) {
        if(anotherObj == null) return false;
        if(!getClass().equals(anotherObj.getClass())) return false;
        return this.hashCode() == anotherObj.hashCode() || getValue().equals(((MapPayload) anotherObj).getValue());
    }
}
