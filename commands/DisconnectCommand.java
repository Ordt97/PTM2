package commands;

import client_side.Parser;
import server_side.FlightSimulatorClientHandler;
import server_side.SimulatorSocket;

public class DisconnectCommand implements Command {
    @Override
    public int doCommand(String[] array) {
        try {
            SimulatorSocket.getInstance().stop();
            FlightSimulatorClientHandler.stop = true;
            Parser.closeSocket();
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
