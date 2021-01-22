package commands;

import expressions.ExpressionBuilder;

public class SleepCommand implements Command {

    @Override
    public void doCommand(String[] args) {
        try {
            Thread.sleep((long) ExpressionBuilder.calc(args[1]));
        } catch (InterruptedException e) {
            System.out.println("Someone interrupt us to sleep");
        }
    }
}
