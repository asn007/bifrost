/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ReadCompletionHandler.java) is part of bifrost.
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
import ru.nloader.bifrost.exceptions.EOFException;
import ru.nloader.bifrost.misc.Callback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by asn007 on 03.07.14.
 */
public class ReadCompletionHandler extends ExtendedCompletionHandler<Integer, ByteBuffer, ByteBuffer> {

    public ReadCompletionHandler(ChannelHandler worker, AsynchronousSocketChannel channel, Callback<ByteBuffer> callback) {
        super(worker, channel, callback);
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if(result == -1) {
            failed(new EOFException(new IOException("Connection terminated by the other side")), null);
            return;
        }
        if(attachment.hasRemaining()) {
            channel.read(attachment, attachment, this);
        }
        attachment.flip();
        invokeCallback(attachment);
    }
}
