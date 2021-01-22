package commands;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConnectCommand implements Command, Observer {

    public static volatile boolean stop = false;
    public static PrintWriter out;

    @Override
    public void doCommand(String[] args) {
        stop = false;
        new Thread(() -> {
            try {
                Socket socket;
                extracted();
                Thread.sleep(10000);
                socket = new Socket(args[1], Integer.parseInt(args[2]));
                out = new PrintWriter(socket.getOutputStream());
                out.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private static void extracted() throws InterruptedException {
        synchronized (OpenDataServerCommand.wait) {
            OpenDataServerCommand.wait.wait();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.getClass() == String.class) {
            out.println("set " + o.toString() + " " + arg);
            out.flush();
            System.out.println("set " + o.toString() + " " + arg);
        }
    }
}
