/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PacketSerializationTest.java) is part of bifrost.
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
import static org.junit.Assert.assertEquals;
import ru.nloader.bifrost.factories.PacketFactory;
import ru.nloader.bifrost.packet.Packet;
import ru.nloader.bifrost.payload.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



/**
 * Created by asn007 on 02.07.14.
 */
public class PacketSerializationTest {

    public Random random = new Random();

    @Test
    public void testSimplePacketSerialization() {
        Packet packet = new Packet(random.nextInt());
        byte[] arr = PacketFactory.getInstance().serialize(packet);
        ByteBuffer buf = ByteBuffer.wrap(arr);
        buf.getInt(); // skip size integer
        Packet responsePacket = PacketFactory.getInstance().deserialize(buf);
        assertEquals("Packet ids must be equal", packet.getID(), responsePacket.getID());
    }

    @Test
    public void testComplicatedPacketSerialization() {
        Packet packet = new Packet(random.nextInt());
        byte randomByte = (byte)random.nextInt();
        double randomDouble = random.nextDouble();
        float randomFloat = random.nextFloat();
        int randomInt = random.nextInt();
        long randomLong = random.nextLong();
        short randomShort = (short)random.nextInt();
        String testString = "thisIsAStringДляТеста";
        ArrayList<AbstractPayload> arrayList = new ArrayList<>();
        HashMap<String, AbstractPayload> hashMap = new HashMap<>();
        arrayList.add(new BytePayload(randomByte));
        arrayList.add(new DoublePayload(randomDouble));
        arrayList.add(new FloatPayload(randomFloat));
        arrayList.add(new IntegerPayload(randomInt));
        arrayList.add(new LongPayload(randomLong));
        arrayList.add(new ShortPayload(randomShort));
        arrayList.add(new StringPayload(testString));
        hashMap.put("byte", new BytePayload(randomByte));
        hashMap.put("double", new DoublePayload(randomDouble));
        hashMap.put("float", new FloatPayload(randomFloat));
        hashMap.put("int", new IntegerPayload(randomInt));
        hashMap.put("long", new LongPayload(randomLong));
        hashMap.put("short", new ShortPayload(randomShort));
        hashMap.put("string", new StringPayload(testString));

        arrayList.add(new MapPayload(hashMap));

        packet.addKey("byte", new BytePayload(randomByte));
        packet.addKey("double", new DoublePayload(randomDouble));
        packet.addKey("float", new FloatPayload(randomFloat));
        packet.addKey("int", new IntegerPayload(randomInt));
        packet.addKey("long", new LongPayload(randomLong));
        packet.addKey("short", new ShortPayload(randomShort));
        packet.addKey("string", new StringPayload(testString));
        packet.addKey("array", new ArrayListPayload(arrayList));

        byte[] arr = PacketFactory.getInstance().serialize(packet);
        ByteBuffer buf = ByteBuffer.wrap(arr);

        buf.getInt();

        assertEquals("Packets must be the same", packet, PacketFactory.getInstance().deserialize(buf));
    }

}
