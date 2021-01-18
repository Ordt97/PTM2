package commands;

import expressions.Algo;

public class SleepCommand implements Command {
    @Override
    public void doCommand(String[] args) {
        try {
            Thread.sleep((long) Algo.calc(args[1]));
        } catch (InterruptedException e) {
            System.out.println("Someone interrupted us to sleep");
        }
    }
}
