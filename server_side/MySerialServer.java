package server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

    private int port;
    private ClientHandler c;
    private volatile boolean stop;


    public MySerialServer() {
        this.stop = false;
    }

    @Override
    public void start(int port, ClientHandler c) {
        this.port = port;
        this.c = c;
        new Thread(() -> {
            try {
                runServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stop() {
        stop = true;
    }

    private void runServer() throws Exception {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is start. waiting for clients...");
        server.setSoTimeout(300000000);
        while (!stop) {
            try {
                Socket aClient = server.accept();
                System.out.println("Client connected to the server");
                try {
                    c.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    aClient.close();
                } catch (IOException e) {
                    System.out.println("Something is invalid");
                    e.printStackTrace();
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Time Out");
                e.printStackTrace();
            }
        }
        server.close();
    }
}
