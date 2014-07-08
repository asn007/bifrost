/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (INetworkTransport.java) is part of bifrost.
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

package ru.nloader.bifrost.transport;

import ru.nloader.bifrost.event.handlers.IClientConnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IClientDisconnectedEventHandler;
import ru.nloader.bifrost.event.handlers.IPacketReceivedEventHandler;

/**
 * Created by asn007 on 03.07.14.
 */
public interface INetworkTransport {

    public void addPacketHandler(IPacketReceivedEventHandler handler);
    public void addClientConnectedEventHandler(IClientConnectedEventHandler handler);
    public void addClientDisconnectedEventHandler(IClientDisconnectedEventHandler handler);


}
