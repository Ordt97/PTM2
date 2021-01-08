package server_side;

import client_side.Parser;
import java.io.*;

public class FlightSimulatorClientHandler implements ClientHandler {
    public static volatile boolean stop;
    private final int timeout;

    public FlightSimulatorClientHandler(int timeout) {
        stop = false;
        this.timeout = timeout; // ms
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) throws IOException {
        BufferedReader b_in = new BufferedReader(new InputStreamReader(in));
        String client_input;
        try {
            while (!stop && !(client_input = b_in.readLine()).equals("bye")) {
                String[] vars = client_input.split(",");

                Parser.symbolTable.put("x", vars[0]);
                Parser.symbolTable.put("y", vars[1]);
                Parser.symbolTable.put("z", vars[2]);

                Parser.updateBindings("x", vars[0]);
                Parser.updateBindings("y", vars[1]);
                Parser.updateBindings("z", vars[2]);

                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
