import java.util.Scanner;
import java.util.Stack;

public class Main {

    // Greeting Function
    public static void greetUser(String name) {
        System.out.println("Hello, " + name + "! Welcome to the Calculator");
    }

    // Evaluate Mathematical Expression
    public static double evaluateExpression(String expression) {

        String[] tokens = expression.split(" ");
        Stack<Double> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                operandStack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && hasHigherPrecedence(operatorStack.peek(), token)) {
                    applyOperation(operandStack, operatorStack.pop());
                }
                operatorStack.push(token);
            }
            else if (isSquaredOperator(token)) {
                // Extract the base and exponent from the token (e.g., "5^2" => base=5, exponent=2)
                String[] parts = token.split("\\^");
                double base = Double.parseDouble(parts[0]);
                double exponent = Double.parseDouble(parts[1]);

                // Calculate the squared value
                double squaredValue = Math.pow(base, exponent);
                operandStack.push(squaredValue);
            }
        }

        while (!operatorStack.isEmpty()) {
            applyOperation(operandStack, operatorStack.pop());
        }

        if (operandStack.size() == 1) {
            return operandStack.pop();
        } else {
            System.out.println("Invalid expression. Please enter a valid mathematical expression.");
            return Double.NaN;
        }
    }

    //Checking if the inputs are Numbers
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Function to check if the input is an Operator
    private static boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/");
    }

    // Function to check if the input is a Squared Operator
    // \\d represents a digit from 0-9, the + behind it implies that it can be one or more, ensuring that the two components of a squared operation are given
    private static boolean isSquaredOperator(String str) {
        return str.matches("\\d+\\^\\d+");
    }

    // Function to actually prioritize "*" and "/", as it should be in math!
    private static boolean hasHigherPrecedence(String op1, String op2) {
        return (op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"));
    }

    // Now THIS is where the math is done ^^
    private static void applyOperation(Stack<Double> operandStack, String operator) {
        if (operandStack.size() < 2) {
            System.out.println("Invalid expression. Please enter a valid mathematical expression.");
            System.exit(1);
        }

        double operand2 = operandStack.pop();
        double operand1 = operandStack.pop();

        // Actual Operators (such as +,-,* and / (Modulo as well as Squared still need to be added)
        switch (operator) {

            case "+":
                operandStack.push(operand1 + operand2);
                break;

            case "-":
                operandStack.push(operand1 - operand2);
                break;

            case "*":
                operandStack.push(operand1 * operand2);
                break;

            case "/":
                // Zero Check for Divisions
                if (operand2 != 0) {
                    operandStack.push(operand1 / operand2);
                } else {
                    System.out.println("Cannot divide by zero.");
                    System.exit(1);
                }
                break;

            default:
                System.out.println("Invalid operator: " + operator);
                System.exit(1);
        }
    }

    // Function to display the Result of Calculations
    public static void displayResult(double result) {
        System.out.println("Result: " + result);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        double result;

        // Next 3 lines can be commented out, as they solely serve as introduction
        System.out.print("Please enter your name: ");
        String userName = scanner.nextLine();
        greetUser(userName);

        // In order to ensure that you can run multiple Calculations, a while loop featuring an "isRunning"-boolean is used
        while (isRunning) {
            System.out.println("Please enter a mathematical expression (e.g.: 5 * 2):");
            String expression = scanner.nextLine();


            if (expression.equalsIgnoreCase("exit")) {
                System.out.println("Hey " + userName + ", I hope you had fun using the calculator. Have a lovely day!");
                break;
            }

            result = evaluateExpression(expression);
            if (!Double.isNaN(result)) {
                displayResult(result);

                System.out.println("Do you want to continue with the result, clear it, or exit? (continue/clear/exit)");
                String choice = scanner.nextLine();

                // typing exit changes the boolean to false
                if (choice.equalsIgnoreCase("exit")) {
                    isRunning = false;

                } else if (choice.equalsIgnoreCase("clear")) {
                    result = Double.NaN;
                }
            }
        }

        scanner.close();
    }
}

//Current Agenda:

//Functional Requirement
// 0. Implement Option to actually keep on calculating with the previous Result, if existant!


//Software Quality Issues
// 1. Writing actual tests for the functions of this application to ensure a better tested Calculator

//Database Issues:
// 2. Create A User Database, so in the case that you already have used it before, you may log back in
// 3. If you are logged in, you can access the previous results, as well as the inputs
