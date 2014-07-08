/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ExtendedCompletionHandler.java) is part of bifrost.
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

package ru.nloader.bifrost.aio;

import ru.nloader.bifrost.misc.Callback;
import ru.nloader.logging.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by asn007 on 03.07.14.
 */
public abstract class ExtendedCompletionHandler<R, A, C> implements CompletionHandler<R, A> {

    protected AsynchronousSocketChannel channel;
    protected ChannelHandler worker;
    protected Callback<C> callback;

    public ExtendedCompletionHandler(ChannelHandler worker, AsynchronousSocketChannel channel, Callback<C> callback) {
        this.worker = worker;
        this.callback = callback;
        this.channel = channel;
    }



    @Override
    public void failed(Throwable t, A attachment) {
        try {
            worker.invokeCloseCallback(t);
        } catch (IOException e) {
            Logger.logException(e);
        }
    }

    public void invokeCallback(C result) {
        try {
            if(callback != null)
                callback.callback(result);
        } catch(IOException bex) {
            Logger.logException(bex);
            failed(bex, null);
        }
    }
}
