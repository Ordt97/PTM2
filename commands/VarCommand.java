package commands;

import client_side.Parser;
import client_side.Var;
import expressions.Algo;

public class VarCommand implements Command {

    @Override
    public void doCommand(String[] args){
        Var v = new Var();
        if (args.length > 2) {
            if (args[3].equals("bind")) {
                Parser.symbolTable.put(args[1], Parser.symbolTable.get(args[4]));
            } else {
                StringBuilder exp = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    exp.append(args[i]);
                v.setV(Algo.calc(exp.toString()));
                Parser.symbolTable.put(args[1], v);
            }
        } else
            Parser.symbolTable.put(args[1], new Var());

    }
}
