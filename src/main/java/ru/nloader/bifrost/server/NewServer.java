/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (NewServer.java) is part of bifrost.
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

package ru.nloader.bifrost.server;

import ru.nloader.bifrost.event.handlers.IClientConnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IClientDisconnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IPacketReceivedEventHandler;
import ru.nloader.bifrost.misc.Callback;
import ru.nloader.bifrost.transport.INetworkTransport;

import java.io.IOException;
import java.nio.channels.*;

/**
 * Created by asn007 on 02.07.14.
 */
public class NewServer implements INetworkTransport {
    private ServerChannelHandler worker;
    public NewServer(int port, Callback<AsynchronousSocketChannel> connectionCallback, Callback<Throwable> teardownCallback) throws IOException {
        worker = new ServerChannelHandler(connectionCallback, teardownCallback);
        worker.bind(port);
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
}
