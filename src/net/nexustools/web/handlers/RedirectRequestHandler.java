/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.web.handlers;

import net.nexustools.data.buffer.basic.ByteArrayBuffer;
import net.nexustools.utils.Pair;
import net.nexustools.web.WebRequest;
import net.nexustools.web.WebResponse;
import net.nexustools.web.WebServer;

/**
 *
 * @author katelyn
 */
public class RedirectRequestHandler implements WebRequestHandler {
	
	final String redirectBase;
	public RedirectRequestHandler(String redirectBase) {
		if(redirectBase.endsWith("/"))
			redirectBase = (String) redirectBase.subSequence(0, redirectBase.length()-1);
		this.redirectBase = redirectBase;
	}

	public WebResponse handle(WebServer server, WebRequest request) throws Throwable {
		return createResponse(redirectBase + request.path(), true, server, request);
	}

	public static WebResponse createResponse(String location, boolean perminent, WebServer server, WebRequest request) {
		ByteArrayBuffer arrayBuffer = new ByteArrayBuffer();
		return server.createResponse(perminent ? 301 : 302, "text/html", arrayBuffer.take(), request, new Pair<String, String>("Location", location));
	}

	public static WebResponse createResponse(String location, WebServer server, WebRequest request) {
		return createResponse(location, false, server, request);
	}
	
}
