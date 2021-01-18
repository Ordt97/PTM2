package expressions;

import client_side.Parser;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYard {

    public static double calc(String exp) {
        Queue<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        Stack<Expression> stackExp = new Stack<>();

        String[] split = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
        for (String s : split) {
            if (isDouble(s)) {
                queue.add(s);
            } else if (Parser.symbolTable.containsKey(s)) {
                queue.add(String.valueOf(Parser.symbolTable.get(s)));
            } else {
                switch (s) {
                    case "/":
                    case "*":
                    case "(":
                        stack.push(s);
                        break;
                    case "+":
                    case "-":
                        while (!stack.empty() && (!stack.peek().equals("("))) {
                            queue.add(stack.pop());
                        }
                        stack.push(s);
                        break;
                    case ")":
                        while (!stack.peek().equals("(")) {
                            queue.add(stack.pop());
                        }
                        stack.pop();
                        break;
                }
            }
        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }

        for (String str : queue) {
            if (isDouble(str)) {
                stackExp.push(new Number(Double.parseDouble(str)));
            } else if (stackExp.size() > 2) {
                Expression right = stackExp.pop();
                Expression left = stackExp.pop();

                switch (str) {
                    case "/":
                        stackExp.push(new Div(left, right));
                        break;
                    case "*":
                        stackExp.push(new Mul(left, right));
                        break;
                    case "+":
                        stackExp.push(new Plus(left, right));
                        break;
                    case "-":
                        stackExp.push(new Minus(left, right));
                        break;
                }
            }
        }
        if (!stackExp.empty()) return Math.floor((stackExp.pop().calculate() * 1000)) / 1000;
        else return 1.0;
    }

    private static boolean isDouble(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
}
