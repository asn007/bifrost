/*
 * Copyright (C) 2014 asn007 aka Andrey Sinitsyn <andrey.sin98@gmail.com>
 *
 * This file (PipelinePacketProcessor.java) is part of bifrost.
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

import ru.nloader.bifrost.client.ClientChannelHandler;
import ru.nloader.bifrost.event.PacketReceivedEvent;
import ru.nloader.bifrost.event.handlers.IPacketReceivedEventHandler;
import ru.nloader.bifrost.factories.PacketFactory;
import ru.nloader.bifrost.misc.Callback;
import ru.nloader.bifrost.misc.PacketCodes;
import ru.nloader.bifrost.packet.Packet;
import ru.nloader.logging.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by asn007 on 04.07.14.
 */
public class PipelinePacketProcessor implements Callback<ByteBuffer> {

    private final ClientChannelHandler worker;
    private boolean handsShaken = false;

    public PipelinePacketProcessor(ClientChannelHandler worker) {
        this.worker = worker;
    }

    @Override
    public void callback(ByteBuffer result) throws IOException {
        Packet parsed = PacketFactory.getInstance().deserialize(result);
        if(parsed.getID() == PacketCodes.PACKET_HANDSHAKE) {
            if(!handsShaken) {
                handsShaken = true;
                worker.sendPacket(new Packet(PacketCodes.PACKET_HANDSHAKE));
            }
        }
        else if(parsed.getID() == PacketCodes.PACKET_DISCONNECT) {
            worker.close();
        }
        else {
            for(IPacketReceivedEventHandler handler: worker.getPacketReceivedEventHandlers()) {
                try {
                    handler.onPacketReceived(new PacketReceivedEvent(worker, parsed));
                } catch(Exception logged) {
                    Logger.warn("Exception happened somewhere in packet handlers!");
                    Logger.logException(logged);
                }
            }
        }
    }
}
