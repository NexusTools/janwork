/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.work;

import java.io.IOException;
import java.net.Socket;
import net.nexustools.janet.Client;
import net.nexustools.janet.Packet;
import net.nexustools.janet.PacketTransport;
import net.nexustools.janet.Server.Protocol;
import net.nexustools.runtime.RunQueue;
import net.nexustools.utils.log.Logger;

/**
 *
 * @author kate
 */
public class WorkClient<W extends WorkPacket, P extends Packet, S extends WorkServer<W, P, ? extends WorkClient>> extends Client<P, S> {

    public WorkClient(Socket socket, final WorkServer server) throws IOException {
        super(socket, server);
    }
    public WorkClient(String host, int port, Protocol protocol, RunQueue runQueue, PacketTransport packetRegistry) throws IOException {
        super(host, port, protocol, runQueue, packetRegistry);
        requestWork();
    }
    
    private void requestWork() {
        Logger.quote("Requesting Work", this);
        super.send((P)new RequestWorkPacket());
    }

	@Override
	public void send(P packet) {
		super.send(packet);
		if(packet instanceof WorkResponsePacket)
			requestWork();
	}
    
}
