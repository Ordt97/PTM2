package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SimulatorModel {
    private static PrintWriter out;
    private static Socket socket;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("Connected to the Server :)");
        } catch (IOException e) {
            System.out.println("Could not connect to server :(");
        }
    }

    public void send(String[] data) {
        try {
            for (String s : data) {
                out.println(s);
                out.flush();
                System.out.println(s);
            }
        } catch (NullPointerException e) {
            System.out.println("First connect to server");
        }
    }

    public void stop() {
        if (out != null) {
            out.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
