import java.io.IOException;

public class Part1 {

    public static int result;
    public static char token;
    
    public static void main(String[] args) throws IOException {
        
        System.out.println("Please type your expression:");
        token = (char) System.in.read();
        
        if((token >= '0' && token <= '9') || token == '(' || token == '+' || token == '-') {
            expr();
        }
        else if(token == '*' || token == '/' || token == ')') {
            System.out.println("Invalid expression!");
            System.exit(-1);
        }
        else {
            System.out.println("Invalid Symbol!");
            System.exit(-1);
        }
        
        System.out.println("Result = " + result);
        System.exit(0);
        
    }

    public static int expr() throws IOException {
        term();
        if(expr2() == false && token != '\n' && token != ')' && token != -1) {
            System.out.println("Invalid symbol read!");
            System.exit(0);
        }
        return result;
    }

    public static int term() throws IOException {
        result = factor();
        result = term2();
        
        return result;
    }

    public static boolean expr2() throws IOException {
        switch (token) {
            case '+':
                token = (char) System.in.read();
                result = result + term();
                expr2();
                break;
            case '-':
                token = (char) System.in.read();
                result = result - term();
                expr2();
                break;
            case ')':
                token = (char) System.in.read();
            case '\n':
                return true;
            default:
                return false;
        }
        return true;
    }

    public static int factor() throws IOException {
        int tok = 0;
        if(token >= '0' && token <= '9') {
            tok = Character.getNumericValue(token);
            token = (char) System.in.read();
        }
        else if(token == '(') {
            token = (char) System.in.read();
            result = expr();
            return result;
        }
        else {
            System.out.println("Invalid symbol read!");
            System.exit(0);
        }
        
        return tok;
    }

    public static int term2() throws IOException {
        switch (token) {
            case '*':
                token = (char) System.in.read();
                result = result * factor();
                term2();
                break;
            case '/':
                token = (char) System.in.read();
                result = result / factor();
                term2();
                break;
        }
        return result;
    }
    
}

//2*(9/(1+2))-1
/*
0       
-3
20+15
2-3*(4-2)       -4
(1+2)+3*5+(8-2) 24
(9-(8-4*(6-3))) 13
(6-4)*9*(8-7)-7 

*/