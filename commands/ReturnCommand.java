package commands;

import expressions.ExpressionBuilder;
import client_side.Parser;

public class ReturnCommand implements Command {

    @Override
    public void doCommand(String[] args) {
        StringBuilder exp = new StringBuilder();
        for (String arg : args) {
            exp.append(arg);
        }
        Parser.returnValue = (int) ExpressionBuilder.calc(exp.toString());
    }
}
