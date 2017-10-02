package com.mao.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Method {
	private String name;
	private int parameters;
	private Class<?>[] parameterTypes;
	private Function<Object[], Object> func;

	public Method(String name, int params, Class<?>[] paramTypes, Function<Object[], Object> func) {
		this.name = name;
		this.func = func;
		this.parameters = params;
		if (params > 0) {
			if (params != paramTypes.length) {
				throw new CompilerError("Method params amount not equal to paramTypes amount");
			}

			this.parameterTypes = paramTypes;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object call(Obtainable[] params) {
		int i = 0;
		List<Object> obtained = new ArrayList<>();
		for (Obtainable param : params) {
			Object p = param.obtain();
			if (!parameterTypes[i].isAssignableFrom(p.getClass())) {
				throw new RuntimeException("Method " + name + " takes a " + parameterTypes[i].getSimpleName()
						+ " as its " + i + " parameter; a " + p.getClass().getSimpleName() + " was given");
			}
			obtained.add(p);
		}
		return func.apply(obtained.toArray(new Object[obtained.size()]));
	}

	public int getParameters() {
		return parameters;
	}

	public void setParameters(int parameters) {
		this.parameters = parameters;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
}
