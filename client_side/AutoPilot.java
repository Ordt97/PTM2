package client_side;

import java.util.ArrayList;

public class AutoPilot {
    Parser p;
    public static volatile boolean stop = true;
    public static volatile boolean close = false;
    public static Thread autoPilot;
    public int i = 0;

    public AutoPilot(Parser p) {
        this.p = p;
    }

    public void parse() {
        p.parse();
        i = 0;
    }

    public void execute() {
        autoPilot = new Thread(() -> {
            while (!close) {
                while (!stop && i < p.commands.size()) {
                    p.commands.get(i).calculate();
                    i++;
                }
            }
        });
        autoPilot.start();
    }

    public void add(ArrayList<String[]> lines) {
        p.lines.clear();
        p.lines.addAll(lines);
        Parser.symbolTable.put("stop", new Var(1));
        for (String[] s : p.lines) {
            if (s[0].equals("while")) {
                StringBuilder builder = new StringBuilder(s[s.length - 2]);
                builder.append("&&stop!=0");
                s[s.length - 2] = builder.toString();
            }
        }
    }

    public void stop() {
        Var stopVar = Parser.symbolTable.get("stop");
        if (stopVar != null)
            stopVar.setValue(0);
        AutoPilot.stop = true;
    }

    public void start() {
        Var stopVar = Parser.symbolTable.get("stop");
        if (stopVar != null)
            stopVar.setValue(1);
    }
}
