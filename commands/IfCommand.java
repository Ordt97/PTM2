package commands;

import java.util.ArrayList;

public class IfCommand extends ConditionCommand {

    public ArrayList<SetCommand> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<SetCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void doCommand(String[] args) {
        PredicateCommand predicateCommand = (PredicateCommand) commands.get(0).getCommand();
        commands.get(0).calculate();
        if (predicateCommand.getResult() != 0) {
            for (int i = 1; i < commands.size(); i++) {
                commands.get(i).calculate();
            }
            commands.get(0).calculate();
        }
    }
}