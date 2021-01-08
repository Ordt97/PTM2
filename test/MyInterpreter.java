package test;

import client_side.Parser;

public class MyInterpreter {

	public static  int interpret(String[] lines){
		Parser parser = Parser.getInstance();
		return parser.parseScript(lines);
	}
}
