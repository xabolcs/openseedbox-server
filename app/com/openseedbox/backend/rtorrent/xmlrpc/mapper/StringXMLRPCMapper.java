package com.openseedbox.backend.rtorrent.xmlrpc.mapper;

public abstract class StringXMLRPCMapper extends AbstractXMLRPCMapper {

	public StringXMLRPCMapper(String methodNameParam) {
		super(methodNameParam);
	}

	@Override
	public void setValueObject(Object valueObject) {
		this.valueObject = (String) valueObject;
	}

	public final String getValueObject() {
		return (String) valueObject;
	}
}
