package commands;

import expressions.Algo;
import client_side.Parser;

public class ReturnCommand implements Command {

    @Override
    public void doCommand(String[] args) {
        StringBuilder exp = new StringBuilder();
        for (String arg : args) {
            exp.append(arg);
        }
        Parser.returnValue = (int)Algo.calc(exp.toString());
    }
}
