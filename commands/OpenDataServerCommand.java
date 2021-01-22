package commands;

import client_side.Parser;
import expressions.ShuntingYard;
import server_side.MySerialServer;
import server_side.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenDataServerCommand extends ConnectCommand {

    Server server;
    public static volatile boolean stop = false;
    public static final Object wait = new Object();

    @Override
    public void doCommand(String[] args) {
        stop = false;
        server = new MySerialServer();
        server.start(Integer.parseInt(args[1]), (in, out) -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            extracted();
            while (!stop) {
                try {
                    String Line;
                    Line = reader.readLine();
                    String[] vars = Line.split(",");
                    for (int i = 0; i < vars.length; i++)
                        if (Double.parseDouble(vars[i]) != Parser.symbolTable.get(Parser.vars.get(i)).getValue())
                            Parser.symbolTable.get(Parser.vars.get(i)).setValue(Double.parseDouble(vars[i]));
                    long calc = (long) ShuntingYard.calc("1000/" + args[2]);
                    Thread.sleep(calc);
                } catch (IOException | InterruptedException e1) {
                    System.out.println("Someone interrupt us to sleep");
                }
            }
            server.stop();
        });
    }

    private static void extracted() {
        synchronized (OpenDataServerCommand.wait) {
            OpenDataServerCommand.wait.notifyAll();
        }
    }
}