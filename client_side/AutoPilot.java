package client_side;

import java.util.ArrayList;

public class AutoPilot {
    Parser p;
    public int numOfCommand = 0;
    public static Thread autoPilot;
    public static volatile boolean stop = true;
    public static volatile boolean close = false;

    public AutoPilot(Parser p) {
        this.p = p;
    }

    public void parse() {
        p.parse();
        numOfCommand = 0;
    }

    public void stop() {
        Var stopVar = Parser.symbolTable.get("stop");
        if (stopVar != null)
            stopVar.setValue(0);
        AutoPilot.stop = true;
        autoPilot.stop();
    }

    public void start() {
        Var stopVar = Parser.symbolTable.get("stop");
        if (stopVar != null)
            stopVar.setValue(1);
    }

    public void add(ArrayList<String[]> lines) {
        p.lines.clear();
        p.lines.addAll(lines);
        Parser.symbolTable.put("stop", new Var(1));
        for (String[] s : p.lines) {
            if (s[0].equals("while")) {
                s[s.length - 2] = s[s.length - 2] + "&&stop!=0";
            }
        }
    }

    public void execute() {
        autoPilot = new Thread(() -> {
            while (!close) {
                while (!stop && numOfCommand < p.commands.size()) {
                    p.commands.get(numOfCommand).calculate();
                    numOfCommand++;
                }
            }
        });
        autoPilot.start();
    }
}
