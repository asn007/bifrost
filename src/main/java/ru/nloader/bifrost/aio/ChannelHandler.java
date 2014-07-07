/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (ChannelHandler.java) is part of bifrost.
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

package ru.nloader.bifrost.aio;

import ru.nloader.bifrost.event.handlers.AbstractGlobalEventHandler;
import ru.nloader.bifrost.event.handlers.IClientConnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IClientDisconnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IPacketReceivedEventHandler;
import ru.nloader.bifrost.misc.Callback;
import ru.nloader.logging.Logger;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;

/**
 * Created by asn007 on 03.07.14.
 */
public abstract class ChannelHandler implements AutoCloseable {

    protected AsynchronousChannelGroup group;
    protected NetworkChannel channel;
    private Callback<Throwable> closeCallback;

    protected ArrayList<IClientConnectedEventHandler> connectedEventHandlers = new ArrayList<>();
    protected ArrayList<IClientDisconnectedEventHandler> disconnectedEventHandlers = new ArrayList<>();
    protected ArrayList<IPacketReceivedEventHandler> packetReceivedEventHandlers = new ArrayList<>();

    public ChannelHandler(AsynchronousChannelGroup group, NetworkChannel channel, Callback<Throwable> closeCallback) {
        this.group = group;
        this.channel = channel;
        this.closeCallback = closeCallback;
    }


    @Override
    public void close() throws IOException {
        try {
            channel.close();
            if(group != null)
                group.shutdownNow();
            invokeCloseCallback(null);
        } catch (IOException e) {
            Logger.warn("Error while shutting down channel worker!");
            Logger.logException(e);
            invokeCloseCallback(e);
        }
    }


    public void invokeCloseCallback(Throwable t) throws IOException {
        if(this.closeCallback == null)
            return;
        Callback<Throwable> prtctd = this.closeCallback;
        this.closeCallback = null;
        prtctd.callback(t);
    }

    public ArrayList<IClientConnectedEventHandler> getClientConnectedEventHandlers() {
        return this.connectedEventHandlers;
    }

    public ArrayList<IClientDisconnectedEventHandler> getClientDisconnectedEventHandlers() {
        return this.disconnectedEventHandlers;
    }

    public ArrayList<IPacketReceivedEventHandler> getPacketReceivedEventHandlers() {
        return this.packetReceivedEventHandlers;
    }

    public void addGlobalEventHandler(AbstractGlobalEventHandler handler) {
        packetReceivedEventHandlers.add(handler);
        connectedEventHandlers.add(handler);
        disconnectedEventHandlers.add(handler);
    }

    public void addClientConnectedEventHandler(IClientConnectedEventHandler e) {
        this.connectedEventHandlers.add(e);
    }

    public void addClientDisconnectedEventHandler(IClientDisconnectedEventHandler e) {
        this.disconnectedEventHandlers.add(e);
    }

    public void addPacketReceivedEventHandler(IPacketReceivedEventHandler e) {
        this.packetReceivedEventHandlers.add(e);
    }



}
