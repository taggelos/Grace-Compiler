import java.io.InputStream;
import java.io.IOException;

public class CalcParser {

    private int lookaheadToken;
    private final InputStream in;

    public CalcParser(InputStream in) throws IOException {
        this.in = in;
        lookaheadToken = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookaheadToken != symbol)
            throw new ParseError();
        lookaheadToken = in.read();
    }

    private void Goal() throws IOException, ParseError {
        if ((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(')
            throw new ParseError();
        Expr();
    }

    private void Expr() throws IOException, ParseError {
        if ((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(')
            throw new ParseError();
        Term();
        Expr2();
    }

    private void Expr2() throws IOException, ParseError {
        if (lookaheadToken == ')' || lookaheadToken == '\n' || lookaheadToken == -1)
            return;
        if (lookaheadToken != '+' && lookaheadToken != '-')
            throw new ParseError();
        consume(lookaheadToken);
        Term();
        Expr2();
    }

    private void Term() throws IOException, ParseError {
        if ((lookaheadToken < '0' || lookaheadToken > '9') && lookaheadToken != '(') {
            System.err.println("error in Term");
            throw new ParseError();
        }
        Factor();
        Term2();
    }

    private void Term2() throws IOException, ParseError {
        if (lookaheadToken == '+' || lookaheadToken == '-'
         || lookaheadToken == ')' || lookaheadToken == '\n' || lookaheadToken == -1)
            return;
        if (lookaheadToken != '*' && lookaheadToken != '/') {
            System.err.println("Error Term2 " + lookaheadToken);
            throw new ParseError();
        }
        consume(lookaheadToken);
        Factor();
        Term2();
    }

    private void Factor() throws IOException, ParseError {
        if (lookaheadToken >= '0' && lookaheadToken <= '9') {
            consume(lookaheadToken);
            return;
        }
        if (lookaheadToken != '(') {
            System.err.println("Error in Factor");
            throw new ParseError();
        }
        consume('(');
        Expr();
        consume(')');
    }

    public void parse() throws IOException, ParseError {
        Goal();
        if (lookaheadToken != '\n' && lookaheadToken != -1) {
            throw new ParseError();
        }
    }

}
