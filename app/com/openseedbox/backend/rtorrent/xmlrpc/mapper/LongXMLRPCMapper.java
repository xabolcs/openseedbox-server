package com.openseedbox.backend.rtorrent.xmlrpc.mapper;

public abstract class LongXMLRPCMapper extends AbstractXMLRPCMapper{
	public LongXMLRPCMapper(String methodNameParam) {
		super(methodNameParam);
	}

	@Override
	public void setValueObject(Object valueObject) {
		this.valueObject = (Long) valueObject;
	}

	public final Long getValueObject() {
		return (Long) valueObject;
	}}
