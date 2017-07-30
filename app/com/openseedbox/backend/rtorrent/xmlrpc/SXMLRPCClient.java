package com.openseedbox.backend.rtorrent.xmlrpc;

import de.timroes.axmlrpc.Call;
import de.timroes.axmlrpc.ResponseParser;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.serializer.SerializerHandler;
import play.Logger;

import java.io.IOException;
import java.io.OutputStreamWriter;

import static de.timroes.axmlrpc.XMLRPCClient.FLAGS_DEBUG;

public class SXMLRPCClient {
	protected IXMLRPCClientTransport transport;
	protected int flags;
	private final SerializerHandler serializerHandler;
	protected ResponseParser responseParser;

	public SXMLRPCClient(IXMLRPCClientTransport transport, int flags) {
		this.transport = transport;
		this.flags = flags;
		this.serializerHandler = new SerializerHandler(flags);
		this.responseParser = new ResponseParser();
	}

	/**
	 * Checks whether a specific flag has been set.
	 *
	 * @param flag The flag to check for.
	 * @return Whether the flag has been set.
	 */
	private boolean isFlagSet(int flag) {
		return (this.flags & flag) != 0;
	}

	public Object call(String method, Object... params) throws XMLRPCException {
		OutputStreamWriter stream = new OutputStreamWriter(transport.getOutputStream());
		try {
			stream.write(new Call(serializerHandler, method, params).getXML(isFlagSet(FLAGS_DEBUG)));
			stream.flush();
			stream.close();

			return responseParser.parse(serializerHandler,transport.getInputStream(), isFlagSet(FLAGS_DEBUG));
		} catch (IOException ex) {
			throw new XMLRPCException(ex);
		}
	}

	//getSystemMultiCall
}
