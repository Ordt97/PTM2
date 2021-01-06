package commands;

import expressions.Algo;

public class ReturnCommand implements Command {

    @Override
    public int doCommand(String[] args) {
        StringBuilder exp = new StringBuilder();
        for (String arg : args) {
            exp.append(arg);
        }

        return (int)Algo.calc(exp.toString());
    }
}
