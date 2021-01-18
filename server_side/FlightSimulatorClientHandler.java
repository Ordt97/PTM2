package server_side;

import java.io.InputStream;
import java.io.OutputStream;

public class FlightSimulatorClientHandler implements ClientHandler {
    public static volatile boolean stop = false;
    MyClientHandler ch;

    public FlightSimulatorClientHandler(MyClientHandler ch) {
        this.ch = ch;
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) {
        while (!stop) {

            ch.handleClient(in, out);
        }
    }
}
