package commands;

import client_side.Parser;

import java.io.IOException;

public class VarCommand implements Command {

    @Override
    public int doCommand(String[] args) throws IOException {
        if (args.length == 1) {
            String varName = args[0];
            Parser.symbolTable.put(varName, null);
        } else if (args[1].equals("=")) {
            Parser.commandFactory.createCommand("=").doCommand(args);
        }

        return 0;
    }
}
