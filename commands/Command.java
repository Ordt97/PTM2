package commands;

import java.io.IOException;

public interface Command {
    int doCommand(String[] args) throws IOException;
}
