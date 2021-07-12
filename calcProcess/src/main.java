import ch.obermuhlner.math.big.BigDecimalMath;
import processing.CalculatorProcessing;

import java.math.BigDecimal;
import java.math.MathContext;

public class main {
    public static void main(String[] args){
        CalculatorProcessing thing = new CalculatorProcessing(new MathContext(16),true);
        System.out.println(thing.solve("⁸⁶√8",true));
    }
}
