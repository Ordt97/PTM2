package commands;

public class WhileCommand extends ConditionCommand {

    @Override
    public void doCommand(String[] args) {
        PredicateCommand tmp = (PredicateCommand) commands.get(0).getC();
        commands.get(0).calculate();
        while (tmp.getResult() != 0) {
            for (int i = 1; i < commands.size(); i++) {
                commands.get(i).calculate();
            }
            commands.get(0).calculate();
        }
    }
}
