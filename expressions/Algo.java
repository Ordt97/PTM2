package expressions;

import client_side.Parser;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Algo {

    public static final String SPLIT_REGEX = "(?<=[-+*/()])|(?=[-+*/()])";

    public static double calc(String exp) {
        Queue<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        String[] split = exp.split(SPLIT_REGEX);

        for (String s : split) {
            if (isNumber(s)) {
                queue.add(s);
            } else if (Parser.symbolTable.containsKey(s)) {
                queue.add(Parser.symbolTable.get(s));
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

        Stack<Expression> stackExp = new Stack<>();
        for (String str : queue) {
            if (isNumber(str)) {
                stackExp.push(new Number(Double.parseDouble(str)));
            } else {
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
        double expressionResult = stackExp.pop().calculate();
        return Math.floor((expressionResult * 1000)) / 1000;
    }

    public static boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

