package com.mao.lang;

public class MethodReference implements Obtainable {
	private Method method;
	private Obtainable[] parameters;

	public MethodReference(Method method, Obtainable[] params) {
		this.method = method;
		this.setParameters(params);
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Obtainable[] getParameters() {
		return parameters;
	}

	public void setParameters(Obtainable[] parameters) {
		this.parameters = parameters;
	}

	@Override
	public Object obtain() {
		return method.call(parameters);
	}
}
