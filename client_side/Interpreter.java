package client_side;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interpreter {
    Lexer lexer;
    AutoPilot parser;

    public Interpreter() {

        try {
            Scanner sc = new Scanner(new File("start_commands.txt"));
            List<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) { lines.add(sc.nextLine()); }
            String[] start = lines.toArray(new String[0]);
            lexer = new Lexer(start);
            parser = new AutoPilot(new Parser(lexer.lex()));
            parser.parse();
            AutoPilot.stop = false;
            parser.execute();
            Thread.sleep(1500);
            AutoPilot.stop = true;
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void interpret(String[] list) {
        lexer = new Lexer(list);
        parser.add(lexer.lex());
        parser.parse();
    }

    public void stop() {
        parser.stop();
    }

    public void execute() {
        if (parser.numOfCommand != 0)
            parser.numOfCommand--;
        parser.start();
        AutoPilot.stop = false;
    }
}
