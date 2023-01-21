package com.klashdevelopment.plumcode;

import java.util.Stack;

public class Numbers {
    public static double evalMathExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);
            if (Character.isDigit(current)) {
                String number = "";
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    number += expression.charAt(i);
                    i++;
                }
                i--;
                numbers.push(Double.parseDouble(number));
            } else if (current == '+' || current == '-' || current == '*' || current == '/') {
                while (!operators.empty() && hasPrecedence(current, operators.peek())) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(current);
            } else if (current == '(') {
                operators.push(current);
            } else if (current == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop();
            }
        }

        while (!operators.empty()) {
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        } else {
            return true;
        }
    }

    public static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }
}
