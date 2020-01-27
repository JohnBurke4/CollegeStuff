// -------------------------------------------------------------------------

import java.util.*;

/**
 * Utility class containing validation/evaluation/conversion operations
 * for prefix and postfix arithmetic expressions.
 *
 * @author
 * @version 1/12/15 13:03:48
 */

public class Arith {


    //~ Validation methods ..........................................................

    private static boolean isOperator(String value) {
        if (value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/") || value.equals("(") || value.equals(")")) {
            return true;
        }
        return false;
    }

    private static int getPrecedence(String value) {

        if (value.equals("-")) {
            return 1;
        }
        if (value.equals("+")) {
            return 2;
        }
        if (value.equals("/")) {
            return 3;
        }
        if (value.equals("*")) {
            return 4;
        }
        return -1;
    }


    /**
     * Validation method for prefix notation.
     *
     * @param prefixLiterals : an array containing the string literals hopefully in prefix order.
     *                       The method assumes that each of these literals can be one of:
     *                       - "+", "-", "*", or "/"
     *                       - or a valid string representation of an integer.
     * @return true if the parameter is indeed in prefix notation, and false otherwise.
     **/
    public static boolean validatePrefixOrder(String prefixLiterals[]) {
        int counter = 1;
        for (String string : prefixLiterals) {
            if (counter <= 0) {
                return false;
            }

            if (isOperator(string)) {
                counter++;
            } else {
                counter--;
            }
        }
        return counter == 0;
    }


    /**
     * Validation method for postfix notation.
     *
     * @param postfixLiterals : an array containing the string literals hopefully in postfix order.
     *                        The method assumes that each of these literals can be one of:
     *                        - "+", "-", "*", or "/"
     *                        - or a valid string representation of an integer.
     * @return true if the parameter is indeed in postfix notation, and false otherwise.
     **/
    public static boolean validatePostfixOrder(String postfixLiterals[]) {
        int counter = 0;
        for (String string : postfixLiterals) {
            if (isOperator(string)) {
                counter -= 2;
                if (counter < 0) {
                    return false;
                }
            }
            counter++;
        }
        return counter == 1;
    }


    //~ Evaluation  methods ..........................................................


    /**
     * Evaluation method for prefix notation.
     *
     * @param prefixLiterals : an array containing the string literals in prefix order.
     *                       The method assumes that each of these literals can be one of:
     *                       - "+", "-", "*", or "/"
     *                       - or a valid string representation of an integer.
     * @return the integer result of evaluating the expression
     **/
    public static int evaluatePrefixOrder(String prefixLiterals[]) {
        Stack<Integer> stack = new Stack<>();
        int index = prefixLiterals.length - 1;

        while (index >= 0) {

            String symbol = prefixLiterals[index];

            if (isOperator(symbol)) {
                int num1 = stack.pop();
                int num2 = stack.pop();

                if (symbol.equals("+")) {
                    stack.push(num1 + num2);
                } else if (symbol.equals("-")) {
                    stack.push(num1 - num2);
                } else if (symbol.equals("*")) {
                    stack.push(num1 * num2);
                } else {
                    stack.push(num1 / num2);
                }
            } else {
                stack.push(Integer.valueOf(symbol));
            }

            index--;

        }
        return stack.pop();
    }


    /**
     * Evaluation method for postfix notation.
     *
     * @param postfixLiterals : an array containing the string literals in postfix order.
     *                        The method assumes that each of these literals can be one of:
     *                        - "+", "-", "*", or "/"
     *                        - or a valid string representation of an integer.
     * @return the integer result of evaluating the expression
     **/
    public static int evaluatePostfixOrder(String postfixLiterals[]) {
        Stack<Integer> stack = new Stack<>();

        for (String symbol : postfixLiterals) {
            if (isOperator(symbol)) {
                int num1 = stack.pop();
                int num2 = stack.pop();

                if (symbol.equals("+")) {
                    stack.push(num2 + num1);
                } else if (symbol.equals("-")) {
                    stack.push(num2 - num1);
                } else if (symbol.equals("*")) {
                    stack.push(num2 * num1);
                } else {
                    stack.push(num2 / num1);
                }
            } else {
                stack.push(Integer.valueOf(symbol));
            }
        }
        return stack.pop();
    }


    //~ Conversion  methods ..........................................................


    /**
     * Converts prefix to postfix.
     *
     * @param prefixLiterals : an array containing the string literals in prefix order.
     *                       The method assumes that each of these literals can be one of:
     *                       - "+", "-", "*", or "/"
     *                       - or a valid string representation of an integer.
     * @return the expression in postfix order.
     **/
    public static String[] convertPrefixToPostfix(String prefixLiterals[]) {
        //TODO
        return convertInfixToPostfix(convertPrefixToInfix(prefixLiterals));
    }


    /**
     * Converts postfix to prefix.
     *
     * @param postfixLiterals : an array containing the string literals in postfix order.
     *                        The method assumes that each of these literals can be one of:
     *                        - "+", "-", "*", or "/"
     *                        - or a valid string representation of an integer.
     * @return the expression in prefix order.
     **/
    public static String[] convertPostfixToPrefix(String postfixLiterals[]) {

        return convertInfixToPrefix(convertPostfixToInfix(postfixLiterals));
    }

    /**
     * Converts prefix to infix.
     *
     * @param prefixLiterals : an array containing the string literals in prefix order.
     *                       The method assumes that each of these literals can be one of:
     *                       - "+", "-", "*", or "/"
     *                       - or a valid string representation of an integer.
     * @return the expression in infix order.
     **/
    public static String[] convertPrefixToInfix(String prefixLiterals[]) {
        Stack<String> operands = new Stack<>();
        for (int i = prefixLiterals.length - 1; i >= 0; i--) {
            String symbol = prefixLiterals[i];
            if (!isOperator(symbol)) {
                operands.push(symbol);
            } else {
                String num1 = operands.pop();
                String num2 = operands.pop();
                operands.push("( " + num1 + " " + symbol + " " + num2 + " )");
            }
        }
        String[] result = operands.pop().split("\\ ");
        return result;
    }

    /**
     * Converts postfix to infix.
     *
     * @param postfixLiterals : an array containing the string literals in postfix order.
     *                        The method assumes that each of these literals can be one of:
     *                        - "+", "-", "*", or "/"
     *                        - or a valid string representation of an integer.
     * @return the expression in infix order.
     **/
    public static String[] convertPostfixToInfix(String postfixLiterals[]) {
        Stack<String> operands = new Stack<>();
        for (String symbol : postfixLiterals) {
            if (!isOperator(symbol)) {
                operands.push(symbol);
            } else {
                String num1 = operands.pop();
                String num2 = operands.pop();
                operands.push("( " + num2 + " " + symbol + " " + num1 + " )");
            }
        }
        String[] result = operands.pop().split("\\ ");
        return result;
    }



    public static String[] convertInfixToPrefix(String infixLiterals[]) {
        String[] reverse = new String[infixLiterals.length];
        int index = infixLiterals.length-1;
        for (int i = 0; i < reverse.length; i++){
            if (infixLiterals[index].equals("(")){
                reverse[i] = ")";
            }
            else if (infixLiterals[index].equals(")")){
                reverse[i] = "(";
            }
            else {
                reverse[i] = infixLiterals[index];
            }
            index--;
        }
        reverse = convertInfixToPostfix(reverse);
        String[] result = new String[reverse.length];
        index = reverse.length-1;
        for (int i = 0; i < reverse.length; i++){
            result[i] = reverse[index];
            index--;
        }
        return result;
    }

    public static String[] convertInfixToPostfix(String infixLiterals[]) {
        Stack<String> operators = new Stack<>();
        LinkedList<String> result = new LinkedList<>();
        for (String symbol : infixLiterals) {
            if (!isOperator(symbol)) {
                result.addLast(symbol);
            } else {
                if (symbol.equals("(")) {
                    operators.push(symbol);
                } else if (symbol.equals(")")) {
                    while (!operators.peek().equals("(")) {
                        result.addLast(operators.pop());
                    }
                    operators.pop();
                } else {
                    int precedence = getPrecedence(symbol);
                    while (!operators.isEmpty() && !operators.peek().equals("(") && getPrecedence(operators.peek()) >= precedence) {
                        result.addLast(operators.pop());
                    }
                    operators.push(symbol);
                }
            }
        }
        while (!operators.isEmpty()) {
            result.addLast(operators.pop());
        }

        Object[] array = result.toArray();
        String[] resultArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            resultArray[i] = (String) array[i];
        }
        return resultArray;

    }


    // Data Structures
    // Stack:
    //  Pop() - Theta(1) worst case running time
    //      Pops top item from stack
    //  Push() - Theta(1) worst case running time
    //      Pushes item onto top of stack
    //  Peek() - Theta(1) running time
    //  empty() - Theta(1) running time

    // LinkedList
    //  addLast() - Theta(1) running time
    //  toArray() - Theta(n) running time




}