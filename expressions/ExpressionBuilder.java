package expressions;

import client_side.Parser;

import java.util.LinkedList;
import java.util.Stack;

public class ExpressionBuilder {

    public static double calc(String exp) {
        StringBuilder token;
        int len = exp.length();
        Stack<String> stack = new Stack<>();
        LinkedList<String> queue = new LinkedList<>();

        for (int i = 0; i < len; i++) {
            token = new StringBuilder(exp.charAt(i) + "");
            if (exp.charAt(i) >= '0' && exp.charAt(i) <= '9') {
                while ((i + 1 < len && exp.charAt(i + 1) >= '0' && exp.charAt(i + 1) <= '9') || (i + 1 < len && exp.charAt(i + 1) == '.'))
                    token.append(exp.charAt(++i));
            } else if ((exp.charAt(i) >= 'A' && exp.charAt(i) <= 'Z') || (exp.charAt(i) >= 'a' && exp.charAt(i) <= 'z')) {
                while (i < exp.length() - 1 && ((exp.charAt(i + 1) >= 'A' && exp.charAt(i + 1) <= 'Z') || (exp.charAt(i + 1) >= 'a' && exp.charAt(i + 1) <= 'z')))
                    token.append(exp.charAt(++i));
                token = new StringBuilder(Parser.symbolTable.get(token.toString()).getValue() + "");
            }

            switch (token.toString()) {
                case "+":
                case "-":
                    while (!stack.isEmpty() && !stack.peek().equals("("))
                        queue.addFirst(stack.pop());
                    stack.push(token.toString());
                    break;
                case "*":
                case "/":
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/")))
                        queue.addFirst(stack.pop());
                    stack.push(token.toString());
                    break;
                case "(":
                    stack.push(token.toString());
                    break;
                case ")":
                    while (!stack.isEmpty() && !(stack.peek().equals("(")))
                        queue.addFirst(stack.pop());
                    stack.pop();
                    break;
                default:
                    queue.addFirst(token.toString());
                    break;
            }
        }
        while (!stack.isEmpty())
            queue.addFirst(stack.pop());
        Expression finalExpression = buildExpression(queue);
        double answer = finalExpression.calculate();
        return Double.parseDouble(String.format("%.3f", answer));
    }

    private static Expression buildExpression(LinkedList<String> queue) {
        Expression returnedExpression;
        Expression right = null;
        Expression left = null;
        String currentExpression = queue.removeFirst();
        if (currentExpression.equals("+") || currentExpression.equals("-") || currentExpression.equals("*")
                || currentExpression.equals("/")) {
            right = buildExpression(queue);
            left = buildExpression(queue);
        }
        switch (currentExpression) {
            case "+":
                returnedExpression = new Plus(left, right);
                break;
            case "-":
                returnedExpression = new Minus(left, right);
                break;
            case "*":
                returnedExpression = new Mul(left, right);
                break;
            case "/":
                returnedExpression = new Div(left, right);
                break;
            default:
                returnedExpression = new Number(
                        Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
                break;
        }
        return returnedExpression;
    }

}

