/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ArrayListPayload.java) is part of bifrost.
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
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by asn007 on 30.06.2014.
 */
public class ArrayListPayload extends AbstractPayload<ArrayList<? extends AbstractPayload>> {

    @Override
    public PayloadType getPayloadType() {
        return PayloadType.ARRAY_LIST;
    }

    public ArrayListPayload(ArrayList<? extends AbstractPayload> value) {
        super(value);
    }

    @Override
    public boolean equals(Object anotherObj) {
        if(anotherObj == null) return false;
        if(!getClass().equals(anotherObj.getClass())) return false;
        return this.hashCode() == anotherObj.hashCode() || getValue().equals(((ArrayListPayload) anotherObj).getValue());
    }

    @Override
    public byte[] getBytes() {
        int elementCount = value.size();
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write(ByteHelper.getBytesFromType(elementCount));
            for(AbstractPayload payload: value)
                baos.write(PayloadFactory.getInstance().serialize(payload));
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public boolean isSizeIntegerRequired() {
        return true;
    }
}
