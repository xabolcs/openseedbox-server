package com.openseedbox.backend.rtorrent.xmlrpc.mapper;

public abstract class IntegerXMLRPCMapper extends AbstractXMLRPCMapper{
	public IntegerXMLRPCMapper(String methodNameParam) {
		super(methodNameParam);
	}

	@Override
	public void setValueObject(Object valueObject) {
		this.valueObject = (Integer) valueObject;
	}

	public final Integer getValueObject() {
		return (Integer) valueObject;
	}}
