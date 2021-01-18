package commands;

import client_side.Parser;

public class PrintCommand implements Command {
    @Override
    public void doCommand(String[] args) {
        for (int i = 1; i < args.length; i++) {
            if (Parser.symbolTable.containsKey(args[i]))
                System.out.print(args[i] + Parser.symbolTable.get(args[i]).getValue());
            else
                System.out.print(args[i]);
        }
        System.out.println();
    }
}
