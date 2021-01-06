package commands;

import server_side.SimulatorSocket;

import java.io.IOException;

public class SetCommand implements Command {
    @Override
    public int doCommand(String[] args) throws IOException {
        String command = "set " + args[0] + " " + args[1];
        if(args[1].equals("-")) {
            command += args[2];
        }

        SimulatorSocket.getInstance().sendString(command);
        return 0;
    }
}
