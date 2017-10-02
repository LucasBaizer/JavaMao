package com.mao.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Method {
	private String name;
	private int parameters;
	private Function<Object[], Object> func;

	public Method(String name, int params, Function<Object[], Object> func) {
		this.name = name;
		this.func = func;
		this.parameters = params;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object call(Obtainable[] params) {
		List<Object> obtained = new ArrayList<>();
		for (Obtainable param : params) {
			obtained.add(param.obtain());
		}
		return func.apply(obtained.toArray(new Object[obtained.size()]));
	}

	public int getParameters() {
		return parameters;
	}

	public void setParameters(int parameters) {
		this.parameters = parameters;
	}
}
