package client_side;

import commands.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Parser {
    public static GenericFactory<Command> commandFactory = new GenericFactory<>();
    public static HashMap<String, Var> symbolTable = new HashMap<>();
    public ArrayList<String[]> lines;
    public ArrayList<SetCommand> commands = new ArrayList<>();
    public static ArrayList<String> vars;
    public static double returnValue;

    public Parser(ArrayList<String[]> lines) {

        HashMap<String, SetCommand> commandTable = new HashMap<>();
        this.lines = lines;

        commandFactory.insertCommand("openDataServer", OpenDataServerCommand.class);
        commandFactory.insertCommand("connect", ConnectCommand.class);
        commandFactory.insertCommand("while", WhileCommand.class);
        commandFactory.insertCommand("var", VarCommand.class);
        commandFactory.insertCommand("return", ReturnCommand.class);
        commandFactory.insertCommand("=", AssignCommand.class);
        commandFactory.insertCommand("disconnect", DisconnectCommand.class);
        commandFactory.insertCommand("predicate", PredicateCommand.class);
        commandFactory.insertCommand("print", PrintCommand.class);
        commandFactory.insertCommand("sleep", SleepCommand.class);
        commandFactory.insertCommand("if", IfCommand.class);

        commandTable.put("openDataServer", new SetCommand(new OpenDataServerCommand()));
        commandTable.put("connect", new SetCommand(new ConnectCommand()));
        commandTable.put("while", new SetCommand(new WhileCommand()));
        commandTable.put("var", new SetCommand(new VarCommand()));
        commandTable.put("return", new SetCommand(new ReturnCommand()));
        commandTable.put("=", new SetCommand(new AssignCommand()));
        commandTable.put("disconnect", new SetCommand(new DisconnectCommand()));
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader("simulator_vars.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        vars = new ArrayList<>();

        while (Objects.requireNonNull(s).hasNext()) vars.add(s.nextLine());

        vars.forEach(str -> symbolTable.put(str, new Var(str)));
    }

    private Command parseCondition(ArrayList<String[]> array) {

        ConditionCommand c = (ConditionCommand) commandFactory.getNewCommand(array.get(0)[0]);
        int i = 0;
        ArrayList<SetCommand> tmp = new ArrayList<>();
        SetCommand tmpExpression = new SetCommand(commandFactory.getNewCommand("predicate"));
        tmpExpression.setS(array.get(0));
        tmp.add(tmpExpression);
        c.setCommands(tmp);
        c.getCommands().addAll(1, this.parseCommands(new ArrayList<>(array.subList(i + 1, array.size()))));
        return c;
    }

    private ArrayList<SetCommand> parseCommands(ArrayList<String[]> array) {
        ArrayList<SetCommand> commands = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            SetCommand e = new SetCommand(commandFactory.getNewCommand(array.get(i)[0]));
            if (e.getC() != null) {
                if (array.get(i)[0].equals("while") || array.get(i)[0].equals("if")) {
                    int index = i;
                    i += loopSize(new ArrayList<>(array.subList(i + 1, array.size()))) + 1;
                    e.setC(this.parseCondition(new ArrayList<>(array.subList(index, i))));
                }
            } else {

                e = new SetCommand(commandFactory.getNewCommand(array.get(i)[1]));
            }
            e.setS(array.get(i));
            commands.add(e);
        }
        return commands;
    }

    private int loopSize(ArrayList<String[]> array) {
        Stack<String> stack = new Stack<>();
        stack.push("{");
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i)[0].equals("while") || array.get(i)[0].equals("if"))
                stack.push("{");
            if (array.get(i)[0].equals("}")) {
                stack.pop();
                if (stack.isEmpty())
                    return i;
            }
        }
        return 0;
    }

    public void parse() {
        this.commands = this.parseCommands(lines);
    }
}
