package FunctionParser;



import java.util.Iterator;

import org.matheclipse.core.convert.ConversionException;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.reflection.system.Blank;
import org.matheclipse.core.reflection.system.Complex;
import org.matheclipse.core.reflection.system.Pattern;
import org.matheclipse.core.reflection.system.Rational;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.ast.FloatNode;
import org.matheclipse.parser.client.ast.FractionNode;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.PatternNode;
import org.matheclipse.parser.client.ast.StringNode;
import org.matheclipse.parser.client.ast.SymbolNode;

/**
 * Tests parser functions for the simple parser style
 */
public class MathParser {
	
		public static void main(String[] args)
		{
			MathParser s = new MathParser();
			s.testParser0();
		}
        public MathParser() {
        }
        public void testParser0() {
                try { 
                        Parser p = new Parser(true);
                        
                        //System.out.println(p.getFactory().getOperator2ListMap());
                        ASTNode obj = p.parse("((a/0.01) + (c*0.016) + (disksize+0.29)) - UsageTime");
                        System.out.println(obj);
                        IExpr exp = convert(obj);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        /**
         * Converts a parsed ASTNode expression into an IExpr expression
         */
        public IExpr convert(ASTNode node) throws ConversionException {
          if (node == null) {
            return null;
          }
          if (node instanceof FunctionNode) {
            final FunctionNode functionNode = (FunctionNode) node;
            final IAST ast = F.ast(convert((ASTNode) functionNode.get(0)));
            for (int i = 1; i < functionNode.size(); i++) {
              ast.add(convert((ASTNode) functionNode.get(i)));
            }
            IExpr head = ast.head();
            if (head.equals(F.PatternHead)) {
              final IExpr expr = Pattern.CONST.evaluate(ast);
              if (expr != null) {
            	 // System.out.println("PatternHead" + expr);
                return expr;
              }
            } else if (head.equals(F.BlankHead)) {
              final IExpr expr = Blank.CONST.evaluate(ast);
              if (expr != null) {
            	 // System.out.println("BlankHead" + expr);
                return expr;
              }
            } else if (head.equals(F.ComplexHead)) {
              final IExpr expr = Complex.CONST.evaluate(ast);
              if (expr != null) {
            	 // System.out.println("ComplexHead" + expr);
                return expr;
              }
            } else if (head.equals(F.RationalHead)) {
              final IExpr expr = Rational.CONST.evaluate(ast);
              if (expr != null) {
            	//  System.out.println("RationalHead" + expr);
                return expr;
              }
            }
            return ast;
          }
          if (node instanceof SymbolNode) {
        	  //System.out.println("SymbolNode - " + node.getString());
            return F.symbol(node.getString());
            
          }
          if (node instanceof PatternNode) {
            final PatternNode pn = (PatternNode) node;
            return F.pattern((ISymbol) convert(pn.getSymbol()), convert(pn
                .getConstraint()));
          }
          if (node instanceof IntegerNode) {
            final IntegerNode integerNode = (IntegerNode) node;
            final String iStr = integerNode.getString();
            if (iStr != null) {
            	//System.out.println("IntegerNode - " + integerNode.getNumberFormat());
              return F.integer(iStr, integerNode.getNumberFormat());
            }
            return F.integer(integerNode.getIntValue());
          }
          if (node instanceof FractionNode) {
            FractionNode fr = (FractionNode) node;
            if (fr.isSign()) {
              return F.fraction((IInteger) convert(fr.getNumerator()),
                  (IInteger) convert(fr.getDenominator())).negate();
            }
            return F.fraction(
                (IInteger) convert(((FractionNode) node).getNumerator()),
                (IInteger) convert(((FractionNode) node).getDenominator()));
          }
          if (node instanceof StringNode) {
        	  //System.out.println("StringNode - " + node.getString());
            return F.stringx(node.getString());
          }
          if (node instanceof FloatNode) {
        	  //System.out.println("FloatNode - " + node.getString());
            return F.num(node.getString());
          }

          return F.symbol(node.toString());
        }

}
