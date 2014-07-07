/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ClientChannelHandler.java) is part of bifrost.
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

import ru.nloader.bifrost.aio.ChannelHandler;
import ru.nloader.bifrost.aio.PipelinePacketProcessor;
import ru.nloader.bifrost.aio.handlers.ConnectionCompletionHandler;
import ru.nloader.bifrost.aio.handlers.ReadCompletionHandler;
import ru.nloader.bifrost.aio.handlers.WriteCompletionHandler;
import ru.nloader.bifrost.event.handlers.IPacketReceivedEventHandler;
import ru.nloader.bifrost.factories.PacketFactory;
import ru.nloader.bifrost.factories.UndaemonizedThreadFactory;
import ru.nloader.bifrost.misc.Callback;
import ru.nloader.bifrost.packet.Packet;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;

/**
 * Created by asn007 on 03.07.14.
 */
public class ClientChannelHandler extends ChannelHandler {
    public AsynchronousSocketChannel channel;

    private PipelinePacketProcessor packetProcessor;

    public ClientChannelHandler(Callback<Throwable> closeCallback) throws IOException {
        this(AsynchronousChannelGroup.withFixedThreadPool(2, new UndaemonizedThreadFactory()), closeCallback);
    }

    public ClientChannelHandler(AsynchronousChannelGroup group, AsynchronousSocketChannel channel, Callback<Throwable> closeCallback) throws IOException {
        super(group, channel, closeCallback);
        this.channel = channel;
        this.packetProcessor = new PipelinePacketProcessor(this);
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

    }

    public ClientChannelHandler(AsynchronousChannelGroup asynchronousChannelGroup, Callback<Throwable> closeCallback) throws IOException {
        this(asynchronousChannelGroup, AsynchronousSocketChannel.open(asynchronousChannelGroup), closeCallback);
    }


    // used by server logic
    public ClientChannelHandler(AsynchronousSocketChannel channel, ArrayList<IPacketReceivedEventHandler> packetReceivedEventHandlers, Callback<Throwable> closeCallback) throws IOException {
        this(null, channel, closeCallback);
        // copy packet event handlers from server. other events remain handled by server itself
        this.packetReceivedEventHandlers = packetReceivedEventHandlers;
    }

    public void connect(SocketAddress address, Callback<Void> completionCallback) throws IOException {
        channel.connect(address, this, new ConnectionCompletionHandler(this, channel, completionCallback));
    }

    public void write(ByteBuffer buffer, Callback<Void> completionCallback) {
        channel.write(buffer, buffer, new WriteCompletionHandler(this, channel, completionCallback));
    }

    public void read(int length, Callback<ByteBuffer> callback) {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        channel.read(buffer, buffer, new ReadCompletionHandler(this, channel, callback));
    }

    public void readPacketData(Callback<ByteBuffer> callback) {
        read(Integer.BYTES, buffer -> read(buffer.getInt(), callback));
    }

    public void readPacket() {
        readPacketData(packetProcessor);
    }

    public void sendPacket(Packet p) {
        ByteBuffer wrapped = ByteBuffer.wrap(PacketFactory.getInstance().serialize(p));
        write(wrapped, null);
    }

    public AsynchronousSocketChannel getChannel() {
        return channel;
    }


}
