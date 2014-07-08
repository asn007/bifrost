/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (NIOHelper.java) is part of bifrost.
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

package ru.nloader.bifrost.helpers;

import ru.nloader.bifrost.factories.PacketFactory;
import ru.nloader.bifrost.misc.PacketCodes;
import ru.nloader.bifrost.packet.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by asn007 on 03.07.14.
 */
public class NIOHelper {


    public static ByteBuffer getHandshake() {
        return ByteBuffer.wrap(PacketFactory.getInstance().serialize(new Packet(PacketCodes.PACKET_HANDSHAKE)));
    }

    public static int fillBuffer(ByteBuffer readBuffer, SelectionKey key, SocketChannel socketChannel) throws IOException {
        readBuffer.clear();
        int numRead = -1;
        try {
            numRead = socketChannel.read(readBuffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            socketChannel.close();
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
        }
        readBuffer.flip();
        return numRead;
    }

}
