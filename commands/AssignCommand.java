package commands;

import client_side.Parser;
import expressions.Algo;

public class AssignCommand implements Command {
    @Override
    public int doCommand(String[] args) {
        /*
         * args[2] can be "bind" or a math expression
         * */
        if (args[2].equals("bind")) {
            String varName = args[0];
            String boundTo = args[3];
            Parser.bindsTable.put(varName, boundTo);
        } else {
            StringBuilder exp = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                exp.append(args[i]);
            }
            double result = Algo.calc(exp.toString());
            String val = Double.toString(result);
            String key = args[0];
            Parser.symbolTable.put(key, val);
            Parser.updateBindings(key, val);
        }
        return 0;
    }
}
