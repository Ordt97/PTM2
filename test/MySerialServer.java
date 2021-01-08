package test;

import server_side.ClientHandler;
import server_side.Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySerialServer implements Server
{
    private static ServerSocket listener = null;
    private static boolean _run = true;

    public void start(int port, ClientHandler c)
    {
        try
        {
            listener = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Runnable runnable = () ->
        {
            while(_run)
            {
                try
                {
                    listener.setSoTimeout(1000);
                    Socket s = listener.accept();

                    c.handleClient(s.getInputStream(), s.getOutputStream());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stop()
    {
        _run = false;

        try
        {
            listener.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
