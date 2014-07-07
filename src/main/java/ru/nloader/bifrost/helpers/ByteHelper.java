/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ByteHelper.java) is part of bifrost.
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

package ru.nloader.bifrost.helpers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by asn007 on 30.06.2014.
 */
public class ByteHelper {
    public static byte[] addSizeInteger(byte[] b) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream daos = new DataOutputStream(baos)) {
            daos.writeInt(b.length);
            daos.write(b);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }



    public static byte[] getBytesFromType(char c) {
        return new String(new char[]{c}).getBytes(Charset.forName("UTF-8"));
    }

    public static byte[] getBytesFromType(byte c) {
        return new byte[] { c };
    }

    public static byte[] getBytesFromType(boolean b) {
        return new byte[] {(byte) ((b) ? 1 : 0)};
    }

    public static byte[] getBytesFromType(double b) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putDouble(b);
        buf.flip();
        return buf.array();
    }

    public static byte[] getBytesFromType(float f) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putFloat(f);
        buf.flip();
        return buf.array();
    }

    public static byte[] getBytesFromType(int i) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putInt(i);
        buf.flip();
        return buf.array();
    }

    public static byte[] getBytesFromType(long l) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.putLong(l);
        buf.flip();
        return buf.array();
    }

    public static byte[] getBytesFromType(short s) {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort(s);
        buf.flip();
        return buf.array();
    }

    public static byte[] getBytesFromType(String s) {
        return s.getBytes(Charset.forName("UTF-8"));
    }

    public static byte[] extractFromBuffer(ByteBuffer b, int len) {
        byte[] arr = new byte[len];
        b.get(arr);
        return arr;
    }
}
