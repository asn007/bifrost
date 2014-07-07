/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (WriteCompletionHandler.java) is part of bifrost.
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

package ru.nloader.bifrost.aio.handlers;

import ru.nloader.bifrost.aio.ChannelHandler;
import ru.nloader.bifrost.aio.ExtendedCompletionHandler;
import ru.nloader.bifrost.client.ClientChannelHandler;
import ru.nloader.bifrost.misc.Callback;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by asn007 on 03.07.14.
 */
public class WriteCompletionHandler extends ExtendedCompletionHandler<Integer, ByteBuffer, Void> {

    public WriteCompletionHandler(ChannelHandler worker, AsynchronousSocketChannel channel, Callback<Void> callback) {
        super(worker, channel, callback);
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(attachment.hasRemaining()) {
            channel.write(attachment, attachment, this);
            return;
        }
        ((ClientChannelHandler)worker).readPacket(); // as our communication goes in read-write manner, expect response from client (or server)
        invokeCallback(null);
    }
}
