package test;

import server_side.ClientHandler;
import server_side.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MySerialServer implements Server
{
    private static ServerSocket listener = null;
    private static boolean _run = true;

    public void start(int port, ClientHandler c)
    {
        try
        {
            this.listener = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        Runnable runnable = () ->
        {
            while(this._run)
            {
                try
                {
                    this.listener.setSoTimeout(1000);
                    Socket s = this.listener.accept();

                    c.handleClient(s.getInputStream(), s.getOutputStream());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                catch (IOException e) { }

            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stop()
    {
        this._run = false;

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
