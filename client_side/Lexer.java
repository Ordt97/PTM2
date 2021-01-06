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
        String[] splitted = line.split("\\s+");
        if (splitted[0].equals("set")) {
            return splitted;
        }

        return splitExpression(splitted);
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
