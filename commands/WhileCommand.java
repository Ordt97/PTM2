package commands;

import client_side.Parser;

import java.io.IOException;

public class WhileCommand implements Command {

    @Override
    public int doCommand(String[] args) throws IOException {
        Command predicateCommand = Parser.commandFactory.createCommand("predicate");
        return predicateCommand.doCommand(args);
    }
}
