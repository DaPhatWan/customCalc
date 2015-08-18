package nicklau.android.customcalc;

import java.util.Stack;
import java.util.StringTokenizer;

/*
 *  For negative numbers, use ~ as unary minus sign.
 *  For subtraction, use - as binary minus sign.
 */

public class PostfixCalculator extends InfixToPostfix {
	
	//Checks if input is an operator or not
	public static boolean isOp(String input)
	{
		return ( input.equals("^") || input.equals("*") || 
				input.equals("/") || input.equals("+") || input.equals("-") );
				
	}
	
	//Computes Postfix equation.
	//Utilizes Stack and StringTokenizer
	//StringTokenizer - breaks numbers and operators apart
	//Stack - holds numbers until operator is approached
	public static double compute(String expression)
	{
		Stack<Double> numberStack = new Stack<Double>();								//Instantiates a Double stack (holds numbers/operands)
		String whiteSpace = "[\t\n ]";													//Declares String variable - used to split equation into tokens (by whitespace)
		StringTokenizer tokenizer = new StringTokenizer(expression, whiteSpace, false);	//Instantiates StringTokenizer, splits expression via String variable whiteSpace

		Double op1, op2, temp;						//Declare double variables (temp - temporary storage, op1/op2 - holding operands for calculation)
		
		while(tokenizer.hasMoreTokens())			//Loops while expression has tokens (numbers OR operators)
		{
			String token = tokenizer.nextToken();	//Assigns next number or operator as a string token
			
			if(token.equals("~"))					//Checks if token is a unary minus sign
			{
				if( !numberStack.empty() )			//Checks if stack has numbers (should never fail - unary minus sign after number ALWAYS)
				{
					temp = numberStack.pop();		//pops number off stack
					numberStack.push(-temp);		//pushes the negative number onto stack					
				}					
			}
			else if( isOp(token) )		//Checks if token is an operator
			{
				if(token.equals("*"))				//Multiplication
				{
					op2 = numberStack.pop();		//Pops top of stack, assigns to op2 and op1 respectively
					op1 = numberStack.pop();		//Order is IMPORTANT - top of stack = right end of the equation
					numberStack.push(op1 * op2);
				}
				else if(token.equals("/"))			//Division
				{
					op2 = numberStack.pop();
					op1 = numberStack.pop();
					numberStack.push(op1 / op2);
				}
				else if(token.equals("+"))			//Addition
				{
					op2 = numberStack.pop();
					op1 = numberStack.pop();
					numberStack.push(op1 + op2);
				}
				else if(token.equals("-"))			//Subtraction
				{
					op2 = numberStack.pop();
					op1 = numberStack.pop();
					numberStack.push(op1 - op2);
				}
				else if(token.equals("^"))			//Exponent
				{
					op2 = numberStack.pop();
					op1 = numberStack.pop();
					numberStack.push( Math.pow(op1, op2) );
				}
				
			}
			else									//If none of the above, its a number 
			{
				temp = Double.parseDouble(token);	//Parses token into a double and assigns into temp
				numberStack.push(temp);				//Pushes temp value into the numberStack
			}
			
		}
		
		return numberStack.pop();					//Pops last number (final result)
	}
	
}
