package expressions;

import client_side.CompParser;

import java.util.LinkedList;
import java.util.Stack;

public class ShuntingYardPredicate {

    public static double calc(String expression) {
        LinkedList<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        int len = expression.length();

        StringBuilder token;
        String tmp = null;
        for (int i = 0; i < len; i++) {
            if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
                token = new StringBuilder(expression.charAt(i) + "");
                while ((i + 1 < len && expression.charAt(i + 1) >= '0' && expression.charAt(i + 1) <= '9')
                        || (i + 1 < len && expression.charAt(i + 1) == '.'))
                    token.append(expression.charAt(++i));
            } else if ((expression.charAt(i) >= '<' && expression.charAt(i) <= '>') || expression.charAt(i) == '!') {
                if (expression.charAt(i + 1) == '=') {
                    token = new StringBuilder(expression.charAt(i) + "");
                    token.append(expression.charAt(++i));
                } else
                    token = new StringBuilder(expression.charAt(i) + "");
            } else if ((expression.charAt(i) >= 'A' && expression.charAt(i) <= 'Z') || (expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z')) {
                token = new StringBuilder(expression.charAt(i) + "");
                while (i < expression.length() - 1 && ((expression.charAt(i + 1) >= 'A' && expression.charAt(i + 1) <= 'Z') || (expression.charAt(i + 1) >= 'a' && expression.charAt(i + 1) <= 'z')))
                    token.append(expression.charAt(++i));
                token = new StringBuilder(CompParser.symbolTable.get(token.toString()).getV() + "");
            } else
                token = new StringBuilder(expression.charAt(i) + "");


            switch (token.toString()) {

                case "+":
                case "-":
                    while (!stack.isEmpty() && !stack.peek().equals("("))
                        queue.addFirst(stack.pop());
                    stack.push(token.toString());
                    break;
                case "||":
                case "&&":
                case "*":
                case "/":
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/")))
                        queue.addFirst(stack.pop());
                    stack.push(token.toString());
                    break;
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "!=":
                case "==":
                    tmp = token.toString();
                    break;
                case "(":
                    stack.push(token.toString());
                    break;

                case ")":
                    while (!stack.isEmpty() && !(stack.peek().equals("(")))
                        queue.addFirst(stack.pop());
                    stack.pop();
                    break;
                default: // Always a number
                    queue.addFirst(token.toString());
                    break;
            }
        }
        while (!stack.isEmpty())
            queue.addFirst(stack.pop());
        queue.addFirst(tmp);
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
                || currentExpression.equals("/") || currentExpression.equals("<") || currentExpression.equals(">")
                || currentExpression.equals("<=") || currentExpression.equals(">=") || currentExpression.equals("==") || currentExpression.equals("!=")) {
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
            case "<=":
            case ">":
            case ">=":
            case "==":
            case "!=":
            case "<":
                returnedExpression = new PredicateExp(left, right, currentExpression);
                break;
            default:
                returnedExpression = new Number(
                        Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
                break;
        }

        return returnedExpression;
    }

}
