/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ServerChannelHandler.java) is part of bifrost.
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

package ru.nloader.bifrost.server;

import ru.nloader.bifrost.aio.ChannelHandler;
import ru.nloader.bifrost.aio.handlers.AcceptCompletionHandler;
import ru.nloader.bifrost.client.ClientChannelHandler;
import ru.nloader.bifrost.event.handlers.IClientConnectedEventHandler;
import ru.nloader.bifrost.factories.UndaemonizedThreadFactory;
import ru.nloader.bifrost.misc.Callback;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;

/**
 * Created by asn007 on 03.07.14.
 */
public class ServerChannelHandler extends ChannelHandler {

    private AsynchronousServerSocketChannel server;
    private Callback<AsynchronousSocketChannel> callback;


    private ArrayList<ClientChannelHandler> connectedClients = new ArrayList<>();

    public ServerChannelHandler(Callback<AsynchronousSocketChannel> callback, Callback<Throwable> closeCallback) throws IOException {
        this(callback, AsynchronousChannelGroup.withFixedThreadPool(4, new UndaemonizedThreadFactory()), closeCallback);
    }

    public ServerChannelHandler(Callback<AsynchronousSocketChannel> callback, AsynchronousChannelGroup group, Callback<Throwable> closeCallback) throws IOException {
        this(callback, group, AsynchronousServerSocketChannel.open(group), closeCallback);
    }

    private ServerChannelHandler(Callback<AsynchronousSocketChannel> callback, AsynchronousChannelGroup group, AsynchronousServerSocketChannel server, Callback<Throwable> closeCallback) throws IOException {
        super(group, server, closeCallback);
        this.server = server;
        this.callback = callback;
        server.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
        //server.setOption(StandardSocketOptions.SO_RCVBUF, BufferHelper.MEDIUM_BUFFER_SIZE);
    }

    public ServerChannelHandler(AsynchronousChannelGroup group, AsynchronousSocketChannel channel, Callback<Throwable> closeCallback) {
        super(group, channel, closeCallback);
    }

    public void bind(int port) throws IOException {
        server.bind(new InetSocketAddress(port));
        server.accept(this, new AcceptCompletionHandler(this, callback));
    }

    public ArrayList<IClientConnectedEventHandler> getClientConnectedEventHandlers() {
        return this.connectedEventHandlers;
    }

    public void registerClient(ClientChannelHandler worker) {
        connectedClients.add(worker);
    }

    public AsynchronousChannelGroup getProcessingGroup() {
        return group;
    }

    public AsynchronousServerSocketChannel getServer() {
        return server;
    }
}
