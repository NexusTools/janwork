/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.io.net.work;

import net.nexustools.io.net.ResponsePacket;

/**
 *
 * @author kate
 */
public abstract class WorkResponsePacket<W extends WorkPacket, C extends WorkClient<W, ?, ?>, S extends WorkServer<W, ?, ?>> extends ResponsePacket<W, C, S> {

    protected abstract void handleResponse(C client, S server, W work);

    @Override
    protected final void handleClientResponse(C client, S server, W request) {
        handleResponse(client, server, request);
    }

    @Override
    protected final void handleServerResponse(C client, W request) {
        throw new UnsupportedOperationException("Server should not be sending work responses.");
    }
    
}
