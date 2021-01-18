package expressions;

import java.util.LinkedList;
import java.util.Stack;

public class ConditionBuilder {

    public static double calc(String exp) {
        LinkedList<String> queue = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        String token;
        while (exp.contains("&") || exp.contains("|")) {
            int i = exp.indexOf("&");
            int j = exp.indexOf("|");
            if ((j < i && j != -1) || i == -1)
                i = j;
            token = ShuntingYard.calc(exp.substring(0, i)) + "";
            queue.addFirst(token);
            token = exp.charAt(i) + "";
            switch (token) {
                case "|":
                    while (!stack.isEmpty())
                        queue.addFirst(stack.pop());
                    stack.push(token);
                    break;
                case "&":
                    while (!stack.isEmpty() && (stack.peek().equals("&")))
                        queue.addFirst(stack.pop());
                    stack.push(token);
                    break;
                default:
                    queue.addFirst(token);
                    break;
            }
            exp = exp.substring(i + 2);
        }
        token = ShuntingYard.calc(exp) + "";
        queue.addFirst(token);
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
        if (currentExpression.equals("|") || currentExpression.equals("&")) {
            right = buildExpression(queue);
            left = buildExpression(queue);
        }
        switch (currentExpression) {
            case "|":
                returnedExpression = new Or(left, right);
                break;
            case "&":
                returnedExpression = new And(left, right);
                break;
            default:
                returnedExpression = new Number(
                        Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
                break;
        }
        return returnedExpression;
    }
}
