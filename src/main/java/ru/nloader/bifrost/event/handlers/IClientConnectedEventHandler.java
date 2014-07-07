/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (IClientConnectedEventHandler.java) is part of bifrost.
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

package ru.nloader.bifrost.event.handlers;

import ru.nloader.bifrost.event.ClientConnectedEvent;

/**
 * Created by asn007 on 04.07.14.
 */
@FunctionalInterface
public interface IClientConnectedEventHandler {
    public void onClientConnected(ClientConnectedEvent evt) throws Exception;
}
