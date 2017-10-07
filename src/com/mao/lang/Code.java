package com.mao.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Code {
	private CodeBlock parent;
	private HashMap<String, Variable> variables = new HashMap<>();
	private HashMap<String, Method> methods = new HashMap<>();

	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	public <T> Variable getVariable(String name) {
		Variable var = (Variable) variables.get(name);
		if (var == null && parent != null) {
			return parent.getVariable(name);
		}
		return var;
	}

	public void addMethod(Method function) {
		methods.put(function.getName(), function);
	}

	public Method getMethod(String name) {
		Method func = methods.get(name);
		if (func == null && parent != null) {
			return parent.getMethod(name);
		}
		return func;
	}

	protected Obtainable parseObtainable(String ref) {
		ref = ref.trim();
		if (ref.isEmpty()) {
			return null;
		}
		if (ref.equals("null")) {
			return new NullObtainable();
		}
		if (ref.equalsIgnoreCase("true") || ref.equalsIgnoreCase("false")) {
			return new DefaultObtainable(Boolean.parseBoolean(ref.toLowerCase()));
		}
		if (ref.startsWith("\"") && ref.endsWith("\"")) {
			return new DefaultObtainable(ref.replace("\"", ""));
		}
		try {
			return new DefaultObtainable(Double.parseDouble(ref));
		} catch (NumberFormatException e) {
			// ... who cares?
		}

		Variable var = getVariable(ref);
		if (var == null) {
			if (ref.contains("(") && ref.contains(")")) {
				String methodName = ref.substring(0, ref.indexOf("("));
				Method method = getMethod(methodName);

				if (method == null) {
					throw new CompilerError("The method " + methodName + " is undefined");
				} else {
					String params = ref.substring(ref.indexOf("(") + 1, ref.lastIndexOf(")"));

					int lastComma = 0;
					List<Obtainable> obtained = new ArrayList<>();
					for (int i = 0; i < params.length(); i++) {
						char character = params.charAt(i);
						if (character == '(') {
							i = ParseUtilities.findClosingParenthesis(params.toCharArray(), i);
						} else if (character == ',') {
							obtained.add(parseObtainable(params.substring(lastComma, i).trim()));
							lastComma = i + 1;
						}
					}

					obtained.add(parseObtainable(params.substring(lastComma, params.length()).trim()));

					if (method.getParameters() != obtained.size()) {
						throw new CompilerError("Method " + methodName + " takes " + method.getParameters()
								+ " parameters; " + obtained.size() + " were given");
					}

					return new MethodReference(method, obtained.toArray(new Obtainable[obtained.size()]));
				}
			}
			throw new CompilerError("The variable " + ref + " is undefined");
		} else {
			return var;
		}
	}

	public CodeBlock getParent() {
		return parent;
	}

	public Code setParent(CodeBlock parent) {
		this.parent = parent;
		return this;
	}

	public abstract ExecutionResult execute();
}
