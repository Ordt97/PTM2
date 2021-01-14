package commands;

import client_side.Parser;
import expressions.Algo;

public class AssignCommand implements Command {
    @Override
    public void doCommand(String[] args) {
        /*
         * args[2] can be "bind" or a math expression
         * */
        if (args[2].equals("bind")) {
            if (Parser.symbolTable.get(args[0]).getV() != Parser.symbolTable.get(args[3]).getV())
                Parser.symbolTable.get(args[0]).setV(Parser.symbolTable.get(args[3]).getV());
            Parser.symbolTable.get(args[3]).addObserver(Parser.symbolTable.get(args[0]));
            Parser.symbolTable.get(args[0]).addObserver(Parser.symbolTable.get(args[3]));
        } else {
            StringBuilder exp = new StringBuilder();
            for (int i = 2; i < args.length; i++)
                exp.append(args[i]);
            double result = Algo.calc(exp.toString());
            if (Parser.symbolTable.get(args[0]) != null && Parser.symbolTable.get(args[0]).getLocation() != null && ConnectCommand.out != null) {
                ConnectCommand.out.println("set " + Parser.symbolTable.get(args[0]).getLocation() + " " + result);
                ConnectCommand.out.flush();
                System.out.println("set " + Parser.symbolTable.get(args[0]).getLocation() + " " + result);
            } else if (Parser.symbolTable.get(args[0]) != null)
                Parser.symbolTable.get(args[0]).setV(result);
        }
    }
}
