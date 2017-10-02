package com.mao.lang;

public class Variable implements Obtainable {
	private String name;
	private Object value;
	private boolean constant;

	public Variable(String name) {
		this(name, null);
	}

	public Variable(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object obtain) {
		this.value = obtain;
	}

	public boolean is(Variable other) {
		return is(other.getClass());
	}

	public boolean is(Class<?> other) {
		return value.getClass() == other;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public Object obtain() {
		return value;
	}

	public boolean isConstant() {
		return constant;
	}

	public Variable setConstant(boolean constant) {
		this.constant = constant;
		return this;
	}
}
