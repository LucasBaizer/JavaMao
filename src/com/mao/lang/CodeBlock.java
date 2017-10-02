package com.mao.lang;

import java.util.ArrayList;
import java.util.List;

public abstract class CodeBlock extends Code {
	private ArrayList<Code> children = new ArrayList<>();
	private String block;

	public CodeBlock(String block) {
		this.block = block;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public void addChild(Code child) {
		children.add(child);
	}

	public List<Code> getChildren() {
		return children;
	}

	protected void parseChildren() {
		parseBlock(block);
	}

	private void parseBlock(String block) {
		int lineStart = 0;
		boolean exitNext = false;
		while (lineStart != -1) {
			if (block.indexOf("\n", lineStart) < 0) {
				exitNext = true;
			}
			String line = exitNext ? block.substring(lineStart).trim()
					: block.substring(lineStart, block.indexOf("\n", lineStart)).trim();
			if (!line.isEmpty()) {
				if (line.endsWith("{")) {
					int openingIndex = block.indexOf("{", lineStart + 1);
					int closingIndex = ParseUtilities.findClosingBracket(block.toCharArray(), openingIndex);

					String subBlock = block.substring(lineStart, closingIndex + 1).trim();

					if (subBlock.startsWith("if") || subBlock.startsWith("for")) {
						String params = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
						String body = subBlock.substring(subBlock.indexOf("{") + 1, subBlock.length() - 1).trim();
						addChild(subBlock.startsWith("if") ? new IfStatement(this, params, body)
								: new ForLoop(this, params, body));
					} else if (subBlock.startsWith("function")) {
						String name = line.substring(line.indexOf(" "), line.indexOf("(")).trim();
						String params = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
						String body = subBlock.substring(subBlock.indexOf("{") + 1, subBlock.length() - 1).trim();
						addChild(new DefinedFunction(this, name, params, body));
					} else {
						throw new CompilerError("Unexpected block");
					}

					lineStart = closingIndex + 1;
				} else {
					String[] split = line.split(" ", 2);
					String command = split[0].trim();
					if (command.equals("say")) {
						addChild(new SayCommand(parseObtainable(split[1].trim())));
					} else if (command.equals("exit")) {
						addChild(new ExitCommand());
					} else if (command.equals("penalize")) {
						addChild(new PenalizeCommand(parseObtainable(split[1].trim())));
					} else if (command.equals("var")) {
						addChild(new VarCommand(this, split[1].trim(), false));
					} else if (command.equals("const")) {
						addChild(new VarCommand(this, split[1].trim(), true));
					} else if (command.equals("break")) {
						addChild(new BreakCommand());
					} else if (command.equals("return")) {
						addChild(new ReturnCommand(this, split.length == 1 ? null : split[1].trim()));
					} else if (command.contains("(")) {
						addChild(new CallMethodCommand((MethodReference) parseObtainable(line)));
					} else if (line.contains("=")) {
						Variable var = getVariable(command);
						if (var == null) {
							throw new CompilerError("Variable " + command + " is undefined");
						}
						addChild(new SetVarCommand(var, parseObtainable(line.split("=")[1].trim())));
					} else {
						throw new CompilerError("Unknown command: " + command);
					}

					lineStart = block.indexOf("\n", lineStart + 1) + 1;
				}
			} else {
				lineStart++;
				if (lineStart >= block.length()) {
					break;
				}
			}
			if (exitNext) {
				break;
			}
		}

	}
}
