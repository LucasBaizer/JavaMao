package com.mao.lang;

import java.util.function.Function;

public class Method {
	private String name;
	private Function<Obtainable[], Object> func;

	public Method(String name, Function<Obtainable[], Object> func) {
		this.name = name;
		this.func = func;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object call(Obtainable[] param) {
		return func.apply(param);
	}
}
