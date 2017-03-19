import java.io.IOException;

public class Part1 {

    public static void main(String[] args) {
//        try {
//            CalcParser parser = new CalcParser(System.in);
//            parser.parse();
//        } catch (IOException | ParseError e) {
//            System.err.println(e.getMessage());
//        }
        try {
            CalcEvaluator evaluate;
            System.out.println("Give an arithmetic expression:");
            evaluate = new CalcEvaluator(System.in);
            System.out.println("S-Expression:\n" + evaluate.eval());
        } catch (IOException | ParseError e) {
            System.err.println(e.getMessage());
        }
        
    }
    
}
