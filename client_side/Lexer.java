package client_side;

import java.util.ArrayList;
import java.util.Scanner;

public class Lexer {
    private Scanner scan;
    private final ArrayList<String[]> lines = new ArrayList<>();
    private final String[] arr;

    public Lexer(String[] s) {
        arr = s;
    }

    public ArrayList<String[]> lex() {
        if (arr != null) {
            for (String s : arr) {
                lines.add(s.replaceFirst("=", " = ").replaceFirst("\t", "").split("\\s+"));
            }
        } else
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine().replaceFirst("=", " = ").replaceFirst("\t", "").split("\\s+"));
            }
        return lines;
    }
}
