package com.openseedbox.backend.rtorrent.xmlrpc.mapper;

public abstract class AbstractXMLRPCMapper implements IXMLRPCMapper {
	protected String methodName;
	protected Object valueObject;
	protected String methodNameParam = null;

	public AbstractXMLRPCMapper() {
	}

	public AbstractXMLRPCMapper(String methodNameParam) {

		this.methodNameParam = methodNameParam;
	}

	@Override
	public final String getMethodName() {
		if (methodName == null) {
			throw new NullPointerException("methodName should be set!");
		}
		if (methodNameParam == null) {
			return methodName;
		}
		return methodName + "=" + methodNameParam;
	}


	public static class DummyStringMapper extends AbstractXMLRPCMapper {
		public DummyStringMapper(String name) {
			methodName = name;
		}

		public DummyStringMapper(String name, String methodNameParam) {
			methodName = name;
			this.methodNameParam = methodNameParam;
		}

		@Override
		public void setValueObject(Object valueObject) {

		}
	}
}
