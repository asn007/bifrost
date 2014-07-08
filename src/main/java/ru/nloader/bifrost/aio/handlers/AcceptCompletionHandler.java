/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (AcceptCompletionHandler.java) is part of bifrost.
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

package ru.nloader.bifrost.aio.handlers;

import ru.nloader.bifrost.aio.ChannelHandler;
import ru.nloader.bifrost.aio.ExtendedCompletionHandler;
import ru.nloader.bifrost.client.ClientChannelHandler;
import ru.nloader.bifrost.event.ClientConnectedEvent;
import ru.nloader.bifrost.event.handlers.IClientConnectedEventHandler;
import ru.nloader.bifrost.misc.Callback;
import ru.nloader.bifrost.server.ServerChannelHandler;
import ru.nloader.logging.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by asn007 on 03.07.14.
 */
public class AcceptCompletionHandler extends ExtendedCompletionHandler<AsynchronousSocketChannel, ServerChannelHandler, AsynchronousSocketChannel> {

    //private AsynchronousServerSocketChannel server;

    public AcceptCompletionHandler(ChannelHandler worker, Callback<AsynchronousSocketChannel> callback) {
        this(worker, null, callback);
    }

    public AcceptCompletionHandler(ChannelHandler worker, AsynchronousSocketChannel channel, Callback<AsynchronousSocketChannel> callback) {
        super(worker,channel, callback);
        //this.server = ((ServerChannelWorker)worker).getServer();
    }

    @Override
    public void completed(AsynchronousSocketChannel result, ServerChannelHandler attachment) {
        attachment.getServer().accept(attachment, this);
        try {
            ClientChannelHandler newWorker = new ClientChannelHandler(result, attachment.getPacketReceivedEventHandlers(), null);
            //
            //
            //
            // attachment.registerClient(newWorker);
            for(IClientConnectedEventHandler handler: attachment.getClientConnectedEventHandlers()) {
                try {
                    handler.onClientConnected(new ClientConnectedEvent(newWorker));
                } catch(Exception e) {
                    Logger.warn("Exception happened in outer client connection handlers!");
                    Logger.logException(e);
                }
            }
            invokeCallback(result);
            newWorker.readPacket(); // switch to read mode, as it's the client who should initiate connection by sending handshakes
        } catch (IOException e) {
            Logger.warn("Failed to accept connection from client. Disconnecting...");
            failed(null, null);
            Logger.logException(e);
        }
        /*try {
            result.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            result.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            result.setOption(StandardSocketOptions.TCP_NODELAY, true);
            ReadCompletionHandler rh = new ReadCompletionHandler(worker, result, nothing -> Logger.info("Handshake completed"));
            //result.write(ByteBuffer.wrap(PacketFactory.getInstance().serialize(new Packet(0))), null, new WriteCompletionHandler(worker, result, nothing -> Logger.info("Handshake completed")));
        } catch (IOException e) {
            Logger.logException(e);
            try {
                worker.invokeCloseCallback(e);
            } catch (BifrostException ignored) {
                Logger.logException(ignored);
            }
            return;
        }*/
    }
}
