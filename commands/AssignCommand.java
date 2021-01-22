package commands;

import client_side.Parser;
import expressions.ExpressionBuilder;
import model.SimulatorModel;

public class AssignCommand implements Command {
    @Override
    public void doCommand(String[] args) {
        SimulatorModel simulatorModel = new SimulatorModel();
        /*
         * args[2] can be "bind" or a math expression
         * */
        if (args[2].equals("bind")) {
            if (Parser.symbolTable.get(args[0]).getValue() != Parser.symbolTable.get(args[3]).getValue())
                Parser.symbolTable.get(args[0]).setValue(Parser.symbolTable.get(args[3]).getValue());
            Parser.symbolTable.get(args[3]).addObserver(Parser.symbolTable.get(args[0]));
            Parser.symbolTable.get(args[0]).addObserver(Parser.symbolTable.get(args[3]));
        } else {
            StringBuilder exp = new StringBuilder();
            for (int i = 2; i < args.length; i++)
                exp.append(args[i]);
            double result = ExpressionBuilder.calc(exp.toString());
            if (Parser.symbolTable.get(args[0]) != null && Parser.symbolTable.get(args[0]).getLocation() != null) {
                String[] command = {"set " + Parser.symbolTable.get(args[0]).getLocation() + " " + result};
                simulatorModel.send(command);
            } else if (Parser.symbolTable.get(args[0]) != null)
                Parser.symbolTable.get(args[0]).setValue(result);
            else System.out.println("Unrecognized command");
        }
    }
}
