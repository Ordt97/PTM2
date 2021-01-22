package commands;

import java.util.ArrayList;

public class ConditionCommand implements Command {

    protected ArrayList<SetCommand> commands;

    @Override
    public void doCommand(String[] array) { }

    public ArrayList<SetCommand> getCommands() {
        return commands;
    }
    public void setCommands(ArrayList<SetCommand> commands) {
        this.commands = commands;
    }
}