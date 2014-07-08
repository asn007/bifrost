/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PacketCodes.java) is part of bifrost.
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

package ru.nloader.bifrost.misc;

/**
 * Created by asn007 on 01.07.14.
 */
public class PacketCodes {
    public static final int PACKET_HANDSHAKE = 0;
    public static final int PACKET_DISCONNECT = 1;
    public static final int PACKET_GET_SETTINGS = 2;
    public static final int PACKET_GET_STYLE = 3;
    // these packets are not used for now.
    public static final int PACKET_GET_PLUGINS = 4;
    public static final int PACKET_VERIFY_PLUGIN = 5;
    // end unused packets
    public static final int PACKET_GET_SERVERS = 6;
    public static final int PACKET_VERIFY_CLIENT = 7;
    public static final int PACKET_VERIFY_JVM = 8;
    public static final int PACKET_AUTHENTICATE = 9;
    public static final int PACKET_URGENT_MESSAGE = 10;

}
