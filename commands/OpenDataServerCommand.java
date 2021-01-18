package commands;

import client_side.Parser;
import expressions.ShuntingYard;
import server_side.MySerialServer;
import server_side.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenDataServerCommand implements Command {

    public static volatile boolean stop = false;
    public static final Object wait = new Object();
    Server s;

    @Override
    public void doCommand(String[] args) {
        stop = false;
        s = new MySerialServer();
        s.start(Integer.parseInt(args[1]), (in, out) -> {
            BufferedReader Bin = new BufferedReader(new InputStreamReader(in));
            synchronized (OpenDataServerCommand.wait) {
                OpenDataServerCommand.wait.notifyAll();
            }
            while (!stop) {
                try {
                    String Line;
                    Line = Bin.readLine();
                    String[] vars = Line.split(",");
                    for (int i = 0; i < vars.length; i++) {
                        if (Double.parseDouble(vars[i]) != Parser.symbolTable.get(Parser.vars.get(i)).getValue())
                            Parser.symbolTable.get(Parser.vars.get(i)).setValue(Double.parseDouble(vars[i]));

                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    long num = (long) ShuntingYard.calc("1000/" + args[2]);
                    Thread.sleep(num);
                } catch (InterruptedException e) {
                }
            }
            s.stop();
        });
    }
}