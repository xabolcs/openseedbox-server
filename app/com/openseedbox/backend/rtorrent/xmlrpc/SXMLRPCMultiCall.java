package com.openseedbox.backend.rtorrent.xmlrpc;

import com.openseedbox.backend.rtorrent.xmlrpc.mapper.IXMLRPCMapper;
import de.timroes.axmlrpc.XMLRPCException;
import org.apache.commons.lang.StringUtils;
import play.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SXMLRPCMultiCall<T extends IMultiCallFriendly> {

	private SXMLRPCClient client;
	private Supplier<T> supplier;

	public SXMLRPCMultiCall(SXMLRPCClient client, Supplier<T> supplier) {
		this.client = client;
		this.supplier = supplier;
	}

	public List<T> getMultiCall() throws XMLRPCException {
		T elem = supplier.get();
		IXMLRPCMapper[] params = elem.getMultiCallParams();
		String[] callParams = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			callParams[i] = params[i].getMethodName();
		}
		Object response = client.call(elem.getMultiCallMethod(), (Object[]) callParams);
		if (response == null || !(response instanceof Object[])) {
			throw new XMLRPCException("Response is not list of Objects");
		}

		Object[] responseArray = (Object[]) response;
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < responseArray.length; i++) {
			Object[] responseRow = (Object[]) responseArray[i];
			if (params.length != responseRow.length + 2) {
				throw new XMLRPCException("Got response row with width " + responseRow.length + " instead of " + params.length);
			}
			T newElem = supplier.get();
			IXMLRPCMapper[] newElemMappers = newElem.getMultiCallParams();
			for (int j = 0; j < responseRow.length; j++) {
				newElemMappers[j+2].setValueObject(responseRow[j]);
			}
			result.add(newElem);
		}

		return result;
	}
}
