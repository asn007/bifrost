/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PayloadType.java) is part of bifrost.
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asn007 on 30.06.2014.
 */
public enum PayloadType {
    BYTE (1, BytePayload.class),
    BYTE_ARRAY (2, ByteArrayPayload.class),
    MAP (3, MapPayload.class),
    DOUBLE (4, DoublePayload.class),
    FLOAT (5, FloatPayload.class),
    INTEGER (6, IntegerPayload.class),
    ARRAY_LIST (7, ArrayListPayload.class),
    LONG (8, LongPayload.class),
    SHORT (9, ShortPayload.class),
    STRING (10, StringPayload.class),
    BOOLEAN(11, BooleanPayload.class);


    /**
     * Static Initialization.
     */
    static {
        try {
            HashMap<Byte, PayloadType> temp = new HashMap<>();
            for (PayloadType type : values()) {
                temp.put(type.typeID, type);
            }
            typeMap = temp;
        } catch(Throwable t) {
            System.out.println(t.toString());
            throw t;
        }
    }

    /**
     * Stores an internal mapping between IDs and types.
     */
    protected static final Map<Byte, PayloadType> typeMap;

    /**
     * Defines the tag representation type.
     */
    public final Class<? extends AbstractPayload> tagType;

    /**
     * Stores the tag identifier.
     */
    public final byte typeID;

    /**
     * Constructs a new TagType.
     * @param typeID
     * @param type
     */
    private PayloadType (int typeID, Class<? extends AbstractPayload> type) {
        this.typeID = ((byte) typeID);
        this.tagType = type;
    }

    /**
     * Returns the correct type associated with the specified type ID.
     * @param typeID
     * @return
     */
    public static PayloadType valueOf (byte typeID) {
        return typeMap.get (typeID);
    }
}
