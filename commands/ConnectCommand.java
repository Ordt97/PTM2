package commands;

import server_side.SimulatorSocket;

import java.io.IOException;

public class ConnectCommand implements Command {

    @Override
    public int doCommand(String[] args) throws IOException {
        String ip = args[0];
        String port = args[1];
        SimulatorSocket.init(ip, Integer.parseInt(port));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
