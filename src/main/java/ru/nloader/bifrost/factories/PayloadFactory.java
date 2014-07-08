/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PayloadFactory.java) is part of bifrost.
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
 *     along with bifrost.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.nloader.bifrost.factories;

import ru.nloader.bifrost.helpers.ByteHelper;
import ru.nloader.bifrost.helpers.PrimitiveHelper;
import ru.nloader.bifrost.payload.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by asn007 on 30.06.2014.
 */
public class PayloadFactory {

    private static PayloadFactory _instance;

    public static PayloadFactory getInstance() {
        if(_instance == null) _instance = new PayloadFactory();
        return _instance;
    }

    public <T extends AbstractPayload> T deserialize(ByteBuffer actualBuffer, Class<T> type, PayloadType ptype) throws UnsupportedEncodingException {
        int shift = 0;
        int totallyRead = 0;
        AbstractPayload result = null;
        int numToRead = 0;
        int elementCount = 0;
        int oldBufferPosition = actualBuffer.position();
        ByteBuffer converter;
        switch(ptype) {
            case BYTE:
                result = new BytePayload(actualBuffer.get());
                break;
            case BYTE_ARRAY:
                // GIMME C# GENERICS IN JAVA

                result = new ByteArrayPayload(PrimitiveHelper.toObjects(ByteHelper.extractFromBuffer(actualBuffer, actualBuffer.getInt())));
                break;
            case MAP:
                HashMap<String, AbstractPayload> resultingMap = new HashMap<>();
                numToRead = actualBuffer.getInt();
                totallyRead = 0;
                elementCount = actualBuffer.getInt();
                totallyRead += 4;
                oldBufferPosition = actualBuffer.position();
                for(int i = 0; i < elementCount; i++) {
                    String key = extractString(actualBuffer);
                    PayloadType ptt = PayloadType.valueOf(actualBuffer.get());
                    resultingMap.put(key, deserialize(actualBuffer, ptt.tagType, ptt));
                    actualBuffer.get(); // skip nullbyte (that protocol reference is for @Mik1313 aka nullbyte C:)
                }
                result = new MapPayload(resultingMap);
                break;
            case DOUBLE:
                result = new DoublePayload(actualBuffer.getDouble());
                break;
            case FLOAT:
                result = new FloatPayload(actualBuffer.getFloat());
                break;
            case INTEGER:
                result = new IntegerPayload(actualBuffer.getInt());
                break;
            case ARRAY_LIST:
                // TODO: Verification maybe?
                ArrayList<AbstractPayload> something = new ArrayList<>();
                numToRead = actualBuffer.getInt();
                totallyRead = 0;
                elementCount = actualBuffer.getInt();
                totallyRead += 4;
                oldBufferPosition = actualBuffer.position();
                for(int i = 0; i < elementCount; i++) {
                    PayloadType ptt = PayloadType.valueOf(actualBuffer.get());
                    something.add(deserialize(actualBuffer, ptt.tagType, ptt));
                }
                result = new ArrayListPayload(something);
                break;
            case LONG:
                result = new LongPayload(actualBuffer.getLong());
                break;
            case SHORT:
                result = new ShortPayload(actualBuffer.getShort());
                break;
            case STRING:
                result = new StringPayload(extractString(actualBuffer));
                break;
            case BOOLEAN:
                result = new BooleanPayload(actualBuffer.get() == (byte)1);
                break;
        }
        return type.cast(result);
    }

    public <T extends AbstractPayload> T deserialize(ByteBuffer actualBuffer, Class<T> type) throws UnsupportedEncodingException {
        return deserialize(actualBuffer, type, PayloadType.valueOf(actualBuffer.get()));
    }


    public byte[] serialize(AbstractPayload payload) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] actualPayload = payload.getBytes();
            baos.write(payload.getPayloadType().typeID);
            baos.write(payload.isSizeIntegerRequired() ? ByteHelper.addSizeInteger(actualPayload) : actualPayload);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payload.getBytes();
    }

    public String extractString(ByteBuffer actualBuffer) throws UnsupportedEncodingException {
        return new String(ByteHelper.extractFromBuffer(actualBuffer, actualBuffer.getInt()), "UTF-8");
    }
}
