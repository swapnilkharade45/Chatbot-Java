import java.util.Scanner;

public class Chatbot {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Hey, what's your name? ");
        String name = sc.nextLine().trim();
        System.out.println("Nice to meet you, " + name + "!");

        while (true) {
            System.out.println("\nAsk me a math question (like 5+7 or 12*8), or type 'exit' to quit:");
            String question = sc.nextLine().trim();

            if (question.equalsIgnoreCase("exit") || question.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye, " + name + "!");
                break;
            }

            try {
                double result = eval(question);
                // Print without decimal if it's actually an integer
                if (result == (long) result) {
                    System.out.printf("Answer: %d%n", (long) result);
                } else {
                    System.out.printf("Answer: %s%n", result);
                }
            } catch (Exception e) {
                System.out.println("Oops! I couldn't process that. (" + e.getMessage() + ")");
            }
        }

        sc.close();
    }

    // Simple expression parser (supports + - * / and parentheses and decimals)
    public static double eval(final String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}
