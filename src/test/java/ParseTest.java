import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ParseTest {
    public static void main(String[] args) {
        long l = System.nanoTime();
        Parser p = new Parser();
        Scope s = p.getScope();
        Variable x = s.getVariable("x");
        Expression e = null;
        try {
            e = p.parse("2 + (7 - 5) * 3.14159 * x^(12-10) + sin(-3.141)", s);
        } catch(ParseException parseException) {
            parseException.printStackTrace();
        }
        List<Double> vals = new ArrayList<>();
        for(int i = 0; i < 1000000; i++) {
            x.setValue(i);
            vals.add(e.evaluate());
        }
        System.out.println("Parsed to " + e.evaluate() + " " + vals.size() + " times in " + (System.nanoTime() - l) / 1000000 + "ms");
        System.out.println((double) ((System.nanoTime() - l)) / vals.size() + "ns per");
    }
}
