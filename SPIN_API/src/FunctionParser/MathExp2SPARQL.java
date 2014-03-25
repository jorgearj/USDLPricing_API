/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * Parses and transforms a mathematical function to a SPARQL Query.
 * Based on:
 * -https://code.google.com/p/symja/wiki/MathExpressionParser
 * -http://scanftree.com/Data_Structure/prefix-to-infix
 *
 */

package FunctionParser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.matheclipse.core.convert.ConversionException;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IInteger;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.ast.FloatNode;
import org.matheclipse.parser.client.ast.FractionNode;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.PatternNode;
import org.matheclipse.parser.client.ast.StringNode;
import org.matheclipse.parser.client.ast.SymbolNode;
import org.matheclipse.parser.client.operator.Operator;

import usdl.servicemodel.*;

public class MathExp2SPARQL {
	private String SPARQLQuery = "";
	private Stack<String> stack = new Stack<String>();
	private Stack<String> infixstack = new Stack<String>();
	private ArrayList<String> usageVariables = new ArrayList<>();
	private Map<String, String> opmap = new HashMap<String, String>();
	public static final String[] SYMBOLS = { "True", "False", "List",
			"Modulus", "Flat", "HoldAll", "HoldFirst", "HoldRest", "Listable",
			"NumericFunction", "OneIdentity", "Orderless", "Slot",
			"SlotSequence", "Abs", "AddTo", "And", "Apart", "Append",
			"AppendTo", "Apply", "ArcCos", "ArcSin", "ArcTan", "Arg", "Array",
			"AtomQ", "Binomial", "Blank", "Block", "Boole", "Break", "Cancel",
			"CartesianProduct", "Cases", "Catalan", "CatalanNumber", "Catch",
			"Ceiling", "CharacteristicPolynomial", "ChessboardDistance",
			"Chop", "Clear", "ClearAll", "Coefficient", "CoefficientList",
			"Complement", "Complex", "ComplexInfinity", "ComposeList",
			"CompoundExpression", "Condition", "Conjugate", "ConstantArray",
			"Continue", "ContinuedFraction", "CoprimeQ", "Cos", "Cosh", "Cot",
			"Count", "Cross", "Csc", "Curl", "D", "Decrement", "Default",
			"Definition", "Degree", "Delete", "Denominator", "Depth",
			"Derivative", "Det", "DiagonalMatrix", "DigitQ", "Dimensions",
			"Discriminant", "Distribute", "Divergence", "DivideBy", "Do",
			"Dot", "Drop", "E", "Eigenvalues", "Eigenvectors", "Equal", "Erf",
			"EuclidianDistance", "EulerGamma", "EulerPhi", "EvenQ", "Exp",
			"Expand", "ExpandAll", "Exponent", "ExtendedGCD", "Extract",
			"Factor", "Factorial", "Factorial2", "FactorInteger",
			"FactorSquareFree", "FactorSquareFreeList", "FactorTerms",
			"Fibonacci", "FindRoot", "First", "Fit", "FixedPoint", "Floor",
			"Fold", "FoldList", "For", "FractionalPart", "FreeQ",
			"FromCharacterCode", "FromContinuedFraction", "FullForm",
			"FullSimplify", "Function", "Gamma", "GCD", "Glaisher",
			"GoldenRatio", "Greater", "GreaterEqual", "GroebnerBasis",
			"HarmonicNumber", "Head", "HilbertMatrix", "Hold", "Horner", "I",
			"IdentityMatrix", "If", "Im", "Increment", "Infinity", "Inner",
			"IntegerPartitions", "IntegerQ", "Integrate", "Intersection",
			"Inverse", "InverseFunction", "JacobiMatrix", "JacobiSymbol",
			"JavaForm", "Join", "Khinchin", "KOrderlessPartitions",
			"KPartitions", "Last", "LCM", "LeafCount", "Length", "Less",
			"LessEqual", "LetterQ", "Level", "Limit", "LinearProgramming",
			"LinearSolve", "Log", "LowerCaseQ", "LUDecomposition",
			"ManhattanDistance", "Map", "MapAll", "MapThread", "MatchQ",
			"MatrixPower", "MatrixQ", "Max", "Mean", "Median", "MemberQ",
			"Min", "Mod", "Module", "MoebiusMu", "Most", "Multinomial", "N",
			"Negative", "Nest", "NestList", "NextPrime", "NFourierTransform",
			"NIntegrate", "NonCommutativeMultiply", "NonNegative", "Norm",
			"Not", "NRoots", "NumberQ", "Numerator", "NumericQ", "OddQ", "Or",
			"Order", "OrderedQ", "Out", "Outer", "Package", "PadLeft",
			"PadRight", "ParametricPlot", "Part", "Partition", "Pattern",
			"Permutations", "Pi", "Plot", "Plot3D", "Plus",
			"PolynomialExtendedGCD", "PolynomialGCD", "PolynomialLCM",
			"PolynomialQ", "PolynomialQuotient", "PolynomialQuotientRemainder",
			"PolynomialRemainder", "Position", "Positive", "PossibleZeroQ",
			"Power", "PowerExpand", "PowerMod", "PreDecrement", "PreIncrement",
			"Prepend", "PrependTo", "PrimeQ", "PrimitiveRoots", "Print",
			"Product", "Quotient", "RandomInteger", "RandomReal", "Range",
			"Rational", "Rationalize", "Re", "Reap", "ReplaceAll",
			"ReplacePart", "ReplaceRepeated", "Rest", "Resultant", "Return",
			"Reverse", "Riffle", "RootIntervals", "Roots", "RotateLeft",
			"RotateRight", "Round", "Rule", "RuleDelayed", "SameQ", "Scan",
			"Sec", "Select", "Set", "SetAttributes", "SetDelayed", "Sign",
			"SignCmp", "Simplify", "Sin", "SingularValueDecomposition", "Sinh",
			"Solve", "Sort", "Sow", "Sqrt", "SquaredEuclidianDistance",
			"SquareFreeQ", "StirlingS2", "StringDrop", "StringJoin",
			"StringLength", "StringTake", "Subsets", "SubtractFrom", "Sum",
			"SyntaxLength", "SyntaxQ", "Table", "Take", "Tan", "Tanh",
			"Taylor", "Thread", "Through", "Throw", "Times", "TimesBy",
			"Timing", "ToCharacterCode", "Together", "ToString", "Total",
			"ToUnicode", "Tr", "Trace", "Transpose", "TrigExpand",
			"TrigReduce", "TrigToExp", "TrueQ", "Trunc", "Unequal", "Union",
			"UnitStep", "UnsameQ", "UpperCaseQ", "ValueQ", "VandermondeMatrix",
			"Variables", "VectorQ", "While" };;

	static final Map<String, String> SYMBOLS_MAP = new HashMap<String, String>();

	static {
		for (String str : SYMBOLS) {
			SYMBOLS_MAP.put(str.toLowerCase(), str);
		}
	}

	public MathExp2SPARQL(String StringFunction,List<Provider> prov,List<Usage> usage) {
		Parser p = new Parser(true);
		Map<String,Operator> temp =p.getFactory().getIdentifier2OperatorMap();

		Iterator<String> it = temp.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next().toString();
			String val = temp.get(key).getOperatorString();
			//System.out.println(key + "  " +val); //print the key-value entry
			opmap.put(key, val);
		}
		
		if(StringFunction.contains("IF"))
		{
			SPARQLQuery = "SELECT ?result\n" +
					"WHERE {\n";
			String[] partitions = StringFunction.split("~"); 
			ArrayList<String> parcels = new ArrayList<String>();
			int counter=0;
			for(String part : partitions)
			{
				String[] data = part.split(";");
				String cond = data[0].replace("IF", "");
				cond = cond.replace("ELSEIF", "");
				cond = cond.replace("ELSE", "");
				String form = data[1];
				//System.out.println("Cond:  "+cond+"\nForm:  "+form);
				
				stack.clear();
				infixstack.clear();
				usageVariables.clear();
				
				ASTNode objcond = p.parse(cond);
				convert(objcond);
				String fa = prefixToInfix(stack,infixstack);
				fa=fa.replace("[", "");
				fa=fa.replace("]", "");
				
				for(String s : usageVariables)//here we can detect the type of the variable by matching the variables name with the JAVA Object. Through the JAVA object we can see if its a Qualitative or Quantitative Value and treat it as such
				{
					if(!SPARQLQuery.contains(s))
					{
						if (!StringUtils.isAllUpperCase(s))
							SPARQLQuery = SPARQLQuery + "\n:" + s + " price:hasValue ?"
									+ s + "_instance .\n" + "?" + s
									+ "_instance gr:hasValue ?" + s + "_value .\n";
						else
						{
							//add syntax to deal with constant qualitative attributes like, WINDOWS will probably be 'windows' in the SPARQL Query
						}
					}
				}
				
				

				stack.clear();
				infixstack.clear();
				usageVariables.clear();
				
				ASTNode objform= p.parse(form);
				convert(objform);
				 String fb = prefixToInfix(stack,infixstack);
				fb=fb.replace("[", "");
				fb=fb.replace("]", "");
				
				for(String s : usageVariables)//here we can detect the type of the variable by matching the variables name with the JAVA Object. Through the JAVA object we can see if its a Qualitative or Quantitative Value and treat it as such
				{
					if(!SPARQLQuery.contains(s))
					{
						if (!StringUtils.isAllUpperCase(s))
							SPARQLQuery = SPARQLQuery + "\n:" + s + " price:hasValue ?"
									+ s + "_instance .\n" + "?" + s
									+ "_instance gr:hasValue ?" + s + "_value .\n";
						else
						{
							//add syntax to deal with constant qualitative attributes like, WINDOWS will probably be 'windows' in the SPARQL Query
						}
					}
				}
				
				//BIND (IF(((?gbs > 1) && (?gbs <= (10 * 1024))), ((?gbs - 1) * ?price10), 0) AS ?priceA) .
				
				String parcel = "BIND(IF(( "  +fa + " ),("+fb+"),0) AS ?result"+counter+++" ).";
				parcels.add(parcel);
				//System.out.println("Cond : "+fa+"\nForm:  "+fb+"\n***************************");
				
			}
			//  BIND ((((?priceA + ?priceB) + ?priceC) + ?priceD) AS ?price) .
			
			
			String sum = "";
			counter--;
			for(; counter>=0;counter--)
			{
				if(counter==0)
					sum = sum + "?result"+counter;
				else
					sum = sum + "?result" + counter + "+";
			}
			
			String lastParcel = "BIND(("+sum+") AS ?result ) .";
			
			
			
			for(String s : parcels)
				SPARQLQuery = SPARQLQuery + s + "\n" ;
			
			SPARQLQuery = SPARQLQuery + lastParcel+"\n";
			SPARQLQuery = SPARQLQuery + "\n}\n";
			
			//System.out.println(SPARQLQuery);
		}
		else
		{
			ASTNode obj = p.parse(StringFunction);//insert the mathematical formula here
			convert(obj);

			SPARQLQuery = "SELECT ?result\n" +
					"WHERE {\n";
			for(String s : usageVariables)//here we can detect the type of the variable by matching the variables name with the JAVA Object. Through the JAVA object we can see if its a Qualitative or Quantitative Value and treat it as such
			{
				if (!StringUtils.isAllUpperCase(s))
					SPARQLQuery = SPARQLQuery + "\n:" + s + " price:hasValue ?"
							+ s + "_instance .\n" + "?" + s
							+ "_instance gr:hasValue ?" + s + "_value .\n";
				else
				{

				}
					//add syntax to deal with constant qualitative attributes like, WINDOWS will probably be 'windows' in the SPARQL Query
			}

			String f = prefixToInfix(stack,infixstack);
			f=f.replace("[", "");
			f=f.replace("]", "");
			SPARQLQuery = SPARQLQuery + "\nBIND(("+ f + ") AS ?result  ) .\n" +
					"}";
		}
	}

	public ArrayList<String> getParsedVariables()
	{
		return this.usageVariables;
	}
	public String getSPARQLQuery()
	{
		return this.SPARQLQuery;
	}
	public IExpr convert(ASTNode node) throws ConversionException {

		if (node == null) {
			return null;
		}

		if (node instanceof FunctionNode) { //function node
			final FunctionNode functionNode = (FunctionNode) node;
			final IAST ast = F.ast(convert((ASTNode) functionNode.get(0)));
			for (int i = 1; i < functionNode.size(); i++) {
				ast.add(convert((ASTNode) functionNode.get(i)));
			}
			// code below
			return ast;
		}

		if (node instanceof FractionNode) { //fraction node
			FractionNode fr = (FractionNode) node;
			if (fr.isSign()) {
				return F.fraction((IInteger) convert(fr.getNumerator()),(IInteger) convert(fr.getDenominator())).negate();
			}
			return F.fraction(
					(IInteger) convert(((FractionNode) node).getNumerator()),(IInteger) convert(((FractionNode) node).getDenominator()));
		}

		if (node instanceof PatternNode) { //pattern node
			final PatternNode pn = (PatternNode) node;
			return F.pattern((ISymbol) convert(pn.getSymbol()),convert(pn.getConstraint()));
		}

		if (node instanceof SymbolNode) {//symbol node
			if (SYMBOLS_MAP.containsKey(node.getString().toLowerCase()))
			{
				//System.out.println("Operator - " + node.getString());
				stack.add(opmap.get(node.toString()));
			}
			else
			{
				//System.out.println("Variable - " + node.getString());
				if(StringUtils.isAllUpperCase(node.getString()))
					stack.add(node.getString());
				else
					stack.add("?"+node.getString().concat("_value"));

				usageVariables.add(node.getString());
			}
			return F.symbol(node.getString());

		}

		if (node instanceof IntegerNode) { // integer node
			final IntegerNode integerNode = (IntegerNode) node;
			final String iStr = integerNode.getString();
			if (iStr != null) {
				//System.out.println("IntegerNode - "+F.integer(iStr, integerNode.getNumberFormat()));
				stack.add(""+F.integer(iStr, integerNode.getNumberFormat()));
				return F.integer(iStr, integerNode.getNumberFormat());
			}
			//System.out.println("IntegerNode - " + integerNode.getIntValue());
			stack.add(""+integerNode.getIntValue());
			return F.integer(integerNode.getIntValue());
		}

		if (node instanceof StringNode) { //string node
			 //System.out.println("StringNode - " + node.getString());
			return F.stringx(node.getString());
		}

		if (node instanceof FloatNode) { //float node
			//System.out.println("FloatNode - " + node.getString());
			stack.add(node.getString());
			return F.num(node.getString());
		}

		return F.symbol(node.toString());
	}

	private String prefixToInfix(Stack<String> stack, Stack<String>infixstack) {
		String s;
		while (!stack.isEmpty()) {
			s = stack.pop();
			if (opmap.containsValue(s)) {
				String nelem = "( " +infixstack.pop()  +" "+ s +" "+ infixstack.pop() + " )";
				infixstack.push(nelem);
			} else {
				infixstack.add(s);
			}
		}
		return infixstack.toString();
	}

}