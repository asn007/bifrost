/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PayloadFactoryTest.java) is part of bifrost.
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

package ru.nloader.bifrost.junit;

import org.junit.Test;
import ru.nloader.bifrost.factories.PayloadFactory;
import ru.nloader.bifrost.payload.*;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;

import static org.junit.Assert.assertEquals;


/**
 * Created by asn007 on 02.07.14.
 */
public class PayloadFactoryTest {

    public Random random = new Random();

    @Test
    public void testArrayListPayloadSerialization() {
        ArrayList<AbstractPayload> initialArrayList = new ArrayList<>();
        initialArrayList.add(new StringPayload("testString"));
        initialArrayList.add(new BooleanPayload(true));
        ArrayListPayload testPayload = new ArrayListPayload(initialArrayList);
        byte[] arr = PayloadFactory.getInstance().serialize(testPayload);
        ArrayListPayload deserialized = null;
        try {
            deserialized = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), ArrayListPayload.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert deserialized != null;
        ArrayList<? extends AbstractPayload> result = deserialized.getValue();
        assertEquals("Lengths of lists must match", initialArrayList.size(), result.size());
        for(int i = 0; i < initialArrayList.size(); i++) {
            assertEquals("List contents must be the same", initialArrayList.get(i).getValue(), result.get(i).getValue());
        }
    }

    @Test
    public void testBooleanPayloadSerialization() {
        boolean test = random.nextBoolean();
        BooleanPayload payload = new BooleanPayload(test);
        byte[] arr = PayloadFactory.getInstance().serialize(payload);
        try {
            assertEquals("Boolean values must match", PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), BooleanPayload.class).getValue(), test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testByteArrayPayloadSerialization() {
        // TODO: Write this test after i fix bytearray payload
    }

    @Test
    public void testDoublePayloadSerialization() {
        double d = random.nextDouble();
        DoublePayload dP = new DoublePayload(d);
        byte[] arr = PayloadFactory.getInstance().serialize(dP);
        double result = 0;
        try {
            result = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), DoublePayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(d, result, 0D);
    }

    @Test
    public void testFloatPayloadSerialization() {
        float d = random.nextFloat();
        FloatPayload dP = new FloatPayload(d);
        byte[] arr = PayloadFactory.getInstance().serialize(dP);
        float result = 0;
        try {
            result = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), FloatPayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(d, result, 0D);
    }

    @Test
    public void testIntegerPayloadSerialization() {
        int d = random.nextInt();
        IntegerPayload dP = new IntegerPayload(d);
        byte[] arr = PayloadFactory.getInstance().serialize(dP);
        int result = 0;
        try {
            result = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), IntegerPayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals("Integer values must match", d, result);
    }

    @Test
    public void testLongPayloadSerialization() {
        long d = random.nextLong();
        LongPayload dP = new LongPayload(d);
        byte[] arr = PayloadFactory.getInstance().serialize(dP);
        long result = 0;
        try {
            result = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), LongPayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals("Long values must match", d, result);
    }

    @Test
    public void testMapPayloadSerialization() {
        HashMap<String, AbstractPayload> testMap = new HashMap<>();
        testMap.put("key1", new StringPayload("value1"));
        testMap.put("key2", new BooleanPayload(random.nextBoolean()));
        testMap.put("key3", new IntegerPayload(random.nextInt()));
        byte[] result = PayloadFactory.getInstance().serialize(new MapPayload(testMap));
        HashMap<String, AbstractPayload> resultMap = null;
        try {
            resultMap = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(result), MapPayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Iterator<Map.Entry<String, AbstractPayload>> map1 = testMap.entrySet().iterator();
        Iterator<Map.Entry<String, AbstractPayload>> map2 = resultMap.entrySet().iterator();
        assertEquals("Lengths of maps must match",  testMap.size(), resultMap.size());
        while(map1.hasNext()) {
            Map.Entry<String, AbstractPayload> testingEntry1 = (Map.Entry<String, AbstractPayload>)map1.next();
            Map.Entry<String, AbstractPayload> testingEntry2 = (Map.Entry<String, AbstractPayload>)map2.next();
            assertEquals("Keys must match", testingEntry1.getKey(), testingEntry2.getKey());
            assertEquals("Values must match", testingEntry1.getValue().getValue(), testingEntry2.getValue().getValue());
        }
    }

    @Test
    public void testShortPayloadSerialization() {
        short d = (short)random.nextInt();
        ShortPayload dP = new ShortPayload(d);
        byte[] arr = PayloadFactory.getInstance().serialize(dP);
        short result = 0;
        try {
            result = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), ShortPayload.class).getValue();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals("Short values must match", d, result);
    }

    @Test
    public void testStringPayloadSerialization() {
        String s = "testStringДляПроверки";
        byte[] arr = PayloadFactory.getInstance().serialize(new StringPayload(s));
        String result = "";
        try {
            result = PayloadFactory.getInstance().deserialize(ByteBuffer.wrap(arr), StringPayload.class).getValue();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals("Strings must match", s, result);
    }
}
