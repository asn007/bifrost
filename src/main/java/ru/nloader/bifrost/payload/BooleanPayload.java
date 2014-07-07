/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (BooleanPayload.java) is part of bifrost.
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

import ru.nloader.bifrost.helpers.ByteHelper;

/**
 * Created by asn007 on 30.06.2014.
 */
public class BooleanPayload extends AbstractPayload<Boolean> {

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.BOOLEAN;
    }

    public BooleanPayload(Boolean value) {
        super(value);
    }

    @Override
    public byte[] getBytes() {
        return ByteHelper.getBytesFromType(value);
    }

    @Override
    public boolean isSizeIntegerRequired() {
        return false;
    }

    @Override
    public boolean equals(Object anotherObj) {
        if(anotherObj == null) return false;
        if(!getClass().equals(anotherObj.getClass())) return false;
        return this.hashCode() == anotherObj.hashCode() || getValue().equals(((BooleanPayload) anotherObj).getValue());
    }
}
