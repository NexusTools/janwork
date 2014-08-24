/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.nexustools.net.web.modules;

import net.nexustools.net.web.WebRequest;
import net.nexustools.net.web.WebResponse;
import net.nexustools.net.web.WebServer;

/**
 *
 * @author kate
 */
public class CGIModule implements WebModule {
	
	private final String cgiScript;
	private final String cgiProcess;
	private final String documentRoot;
	public CGIModule(String documentRoot, String cgiScript, String cgiProcess) {
		this.documentRoot = documentRoot;
		this.cgiProcess = cgiProcess;
		this.cgiScript = cgiScript;
	}

	public WebResponse handle(WebServer server, WebRequest request) throws Throwable {
		return server.cgiResponse(documentRoot, cgiScript, cgiProcess, request);
	}
    
}