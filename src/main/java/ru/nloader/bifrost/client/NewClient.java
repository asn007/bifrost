/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (NewClient.java) is part of bifrost.
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

package ru.nloader.bifrost.client;

import ru.nloader.bifrost.event.handlers.IClientConnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IClientDisconnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IPacketReceivedEventHandler;
import ru.nloader.bifrost.misc.PacketCodes;
import ru.nloader.bifrost.packet.Packet;
import ru.nloader.bifrost.transport.INetworkTransport;
import ru.nloader.logging.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by asn007 on 03.07.14.
 */
public class NewClient implements INetworkTransport {

    private InetSocketAddress address;
    private ClientChannelHandler worker = null;

    public NewClient(InetSocketAddress address) throws IOException {
        this.address = address;
        this.worker = new ClientChannelHandler(nothing -> Logger.info("System fucked up severely"));
        Logger.info("Attempting to establish connection to server...");
        // initiate the connection, because someone needs to
    }


    public void connect() throws Exception {
        worker.connect(address, nothing -> worker.sendPacket(new Packet(PacketCodes.PACKET_HANDSHAKE)));
    }

    @Override
    public void addPacketHandler(IPacketReceivedEventHandler handler) {
        worker.addPacketReceivedEventHandler(handler);
    }

    @Override
    public void addClientConnectedEventHandler(IClientConnectedEventHandler handler) {
        worker.addClientConnectedEventHandler(handler);
    }

    @Override
    public void addClientDisconnectedEventHandler(IClientDisconnectedEventHandler handler) {
        worker.addClientDisconnectedEventHandler(handler);
    }

    public void sendPacket(Packet p) {
        worker.sendPacket(p);
    }
}
