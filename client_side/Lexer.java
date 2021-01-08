package client_side;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {
    private static Lexer instance = null;

    private Lexer() {
    }

    public static Lexer getInstance() {
        if (instance == null) {
            instance = new Lexer();
        }

        return instance;
    }

    public String[] lex(String line) {
        String[] split_line = line.split("\\s+");
        if (split_line[0].equals("set")) {
            return split_line;
        }

        return splitExpression(split_line);
    }

    private static String[] splitExpression(String[] command) {
        List<String> newCommand = new ArrayList<>();
        for (String c : command) {
            String[] split = c.split("(?<=[-+=*/()])|(?=[-+*/=()])");
            newCommand.addAll(Arrays.asList(split));
        }

        return newCommand.toArray(new String[0]);
    }

}
