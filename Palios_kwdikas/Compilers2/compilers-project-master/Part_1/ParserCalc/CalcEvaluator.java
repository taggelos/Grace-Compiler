import java.io.InputStream;
import java.io.IOException;

public class CalcEvaluator {
    private int lookaheadToken;
    private final InputStream in;
    private String prefix;

    public CalcEvaluator(InputStream in) throws IOException {
        this.in = in;
        lookaheadToken = in.read();
        prefix = "";
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookaheadToken != symbol)
            throw new ParseError();
        do 
            lookaheadToken = in.read();
        while (lookaheadToken == ' ');
    }

    private int evalDigit(int digit) {
	return digit - '0';
    }
  
    private void Goal() throws IOException, ParseError {
        if ((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(')
            throw new ParseError();
        prefix = Expr();
    }

    private String Expr() throws IOException, ParseError {
        if ((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(')
            throw new ParseError();
        String s = Term();
        return Expr2(s);
    }

    private String Expr2(String s) throws IOException, ParseError {
        if (lookaheadToken == ')' || lookaheadToken == '\n' || lookaheadToken == -1)
            return s;
        if (lookaheadToken != '+' && lookaheadToken != '-')
            throw new ParseError();
        if (lookaheadToken == '+')
            s = "+ " + s;
        else if (lookaheadToken == '-')
            s = "- " + s;
        consume(lookaheadToken);
        String tmp = Term();
        tmp = '(' + s + " " + tmp + ')';
        return Expr2(tmp);
    }

    private String Term() throws IOException, ParseError {
        if ((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(')
            throw new ParseError();
        String s = Factor();
        return Term2(s);
    }

    private String Term2(String s) throws IOException, ParseError {
        if (lookaheadToken == '+' || lookaheadToken == '-' || lookaheadToken == ')' || lookaheadToken == '\n' || lookaheadToken == -1)
            return s;
        if (lookaheadToken != '*' && lookaheadToken != '/')
            throw new ParseError();
        if (lookaheadToken == '*')
            s = "* " + s;
        else if (lookaheadToken == '/')
            s = "/ " + s;
        consume(lookaheadToken);
        String tmp = Factor();
        tmp = '(' + s + " " + tmp + ')';
        return Term2(tmp);
    }

    private String Factor() throws IOException, ParseError {
        String s = "";
        if (lookaheadToken >= '0' && lookaheadToken <= '9') {
            s += evalDigit(lookaheadToken);
            consume(lookaheadToken);
            return s;
        }
        if (lookaheadToken != '(')
            throw new ParseError();
        consume('(');
        s = Expr();
        consume(')');
        return '(' + s + ')';
    }
    
    public String eval() throws IOException, ParseError {
        Goal();
        if (lookaheadToken != '\n' && lookaheadToken != -1)
            throw new ParseError();
        return prefix;
    }

}

