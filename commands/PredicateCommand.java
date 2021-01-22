package commands;

import expressions.ConditionBuilder;

public class PredicateCommand implements Command {

    double result;

    public double getResult() {
        return result;
    }

    @Override
    public void doCommand(String[] args) {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            s.append(args[i]);
        }
        result = ConditionBuilder.calc(s.toString());
    }
}
