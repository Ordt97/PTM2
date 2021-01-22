package commands;

import client_side.Parser;
import client_side.Var;
import expressions.ExpressionBuilder;

public class VarCommand implements Command {

    @Override
    public void doCommand(String[] args){
        Var var = new Var();
        if (args.length > 2) {
            if (args[3].equals("bind")) {
                Parser.symbolTable.put(args[1], Parser.symbolTable.get(args[4]));
            } else {
                StringBuilder exp = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    exp.append(args[i]);
                var.setValue(ExpressionBuilder.calc(exp.toString()));
                Parser.symbolTable.put(args[1], var);
            }
        } else
            Parser.symbolTable.put(args[1], new Var());
    }
}
