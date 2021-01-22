package commands;

import expressions.Expression;

public class SetCommand implements Expression {

    private Command command;
    private String[] s;

    public Command getCommand() {
        return command;
    }

    public SetCommand(Command command) {
        this.command = command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setCommandStrings(String[] s) {
        this.s = s;
    }

    @Override
    public double calculate() {
        command.doCommand(s);
        return 0;
    }
}
