package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimulatorModel {
    private static PrintWriter out;
    private static Socket socket;
    BufferedReader br;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("Connected to the Server");
        } catch (IOException e) {
            System.out.println("Server is up?");
            System.out.println("If server is up, Wrong ip or port");
        }
    }

    public String[] requestFor(String[] data) {
        String[] h = null;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (String s : data) {
                out.println(s);
                out.flush();
                h = br.readLine().split(" ");
                System.out.println(s);
            }
        } catch (NullPointerException e) {
            System.out.println("First connect to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return h;
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
