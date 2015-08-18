package nicklau.android.customcalc;

import java.util.Stack;
import java.util.StringTokenizer;

public class InfixToPostfix {
	
	
	//Checks if the input is a number or not
	public static boolean isNumeric(String input)
	{
		return input.matches("[-+]?\\d+(\\.\\d+)?");
	}
	
	
	//Checks if input is an operator or not
	public static boolean isOperator(String input)
	{
		return ( input.equals("^") || input.equals("~") || input.equals("*")
				|| input.equals("/") || input.equals("+") || input.equals("-") );				
	}
	
	
	//Returns true if op1 has higher or equal precedence than op2
	//Returns false if op1 has lower with op2 
	//op1 = operator on left, op2 = operator on right
	public static boolean higherPrecedence(String op1, String op2)
	{
		if( (op1.equals("+") || op1.equals("-")) && 
				(op2.equals("+") || op2.equals("-")) )
		{
			return true;
		}
		else if( (op1.equals("*") || op1.equals("/")) && 
				(op2.equals("+") || op2.equals("-") || op2.equals("*") || op2.equals("/")) )
		{
			return true;
		}
		else if( op1.equals("~") && !op2.equals("^") )
		{
			return true;
		}
		else if( op1.equals("^") )
		{	
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	//Checks for left parenthesis without preceding operator
	//Example: 10(3+5) == 10*(3+5)
	//If this case exists, will add * before parenthesis
    public static String implicitMult(String expression)
    {
        String newExpress = "";														//Declare String variable newExpression - will hold new expression

    Stack<String> expressionStack = new Stack<String>();					            	//Instantiates a String stack to hold strings from tokens
        StringTokenizer tokenizer=new StringTokenizer(expression, "(~*/+-^)", true);//Instantiates StringTokenizer - splits expression whenever an
        //operator is met, operators are also included as tokens

        while(tokenizer.hasMoreTokens())											//Loops while expression has tokens (numbers or operators)
        {
            String token = tokenizer.nextToken();									//Assigns next number or operator as a string token
            //System.out.println(token);

            if(token.equals("("))													//Token is left parenthesis
            {
                if(expressionStack.empty() || expressionStack.peek().equals("(") )	//Left parenthesis is beginning of expression
                {
                    expressionStack.push(token);									//Push parenthesis into stack
                }
                else if( !isOperator(expressionStack.peek() ) )						//If parenthesis doesn't follow an operator: ^,/,*,-,+,~
                {
                    expressionStack.push("*");										//Push multiplication into stack
                    expressionStack.push(token);									//Push parenthesis into stack
                }
                else																//Left parenthesis is normal
                {
                    expressionStack.push(token);									//Push parenthesis into stack
                }
            }
            else if(isNumeric(token) && !expressionStack.isEmpty() && expressionStack.peek().equals(")"))			//If right parenthesis is followed directly by a number, then multiply
            {
                expressionStack.push("*");
                expressionStack.push(token);
            }
            else																	//Everything else is pushed into stack
            {
                expressionStack.push(token);
            }
        }

        while(!expressionStack.isEmpty())											//Loops while expressionStack still holds something
        {
            newExpress = expressionStack.pop() + newExpress;						//Rebuilds expression
        }

        return newExpress;
    }
	
	
	//Checks for negative numbers.
	//If negative numbers exist, will use ~ as unary minus sign.
	public static String negSymbol(String expression)
	{
		String newExpress = "";															//Declare String variable newExpression - will hold new expression
		
		Stack<String> expressionStack = new Stack<String>();							//Instantiates a String stack to hold strings from tokens
        StringTokenizer tokenizer=new StringTokenizer(expression, "(*/+-^)", true);		//Instantiates StringTokenizer - splits expression whenever an
        																				//operator is met, operators are also included as tokens
        
        while(tokenizer.hasMoreTokens())												//Loops while expression has tokens (numbers or operators)
		{
			String token = tokenizer.nextToken();										//Assigns next number or operator as a string token
			
			if(token.matches("[*/+-]"))													//Token is operator
			{
				if(token.equals("-") && expressionStack.empty() )						//Very first operator is minus sign and no parenthesis - is negative symbol
				{
					expressionStack.push("~");											//Pushes ~ into stack
				}
				else if(token.equals("-") && expressionStack.peek().matches("[(*/+-]"))	//If there's a minus sign after another operator, its a negative symbol
				{
					expressionStack.push("~");											//Pushes ~ into stack
				}
				else
				{
					expressionStack.push(token);										//Pushes token into stack
				}					
			}
			else																		//Token is number
			{
				expressionStack.push(token);											//Pushes number into stack
			}
		}
        
        while(!expressionStack.isEmpty())												//Loops while expressionStack still holds something
        {
        	newExpress = expressionStack.pop() + newExpress;							//Rebuilds expression
        }
        
        return newExpress;
	}
	
	
	//Converts infix expression into a postfix expression
	public static String convert(String expression)
	{		
		expression=expression.replaceAll("[\t\n ]", "");								//Removes any whitespaces
        
		expression = implicitMult(expression);
		expression = negSymbol(expression);												//Calls negSymbol function - replaces all negative signs with ~
		
		String postfix = "";															//Declares String variable -contains postfix expression
		Stack<String> opStack = new Stack<String>();									//Instantiates String Stack object - holds and compares operators
        StringTokenizer tokenizer=new StringTokenizer(expression, "(^~/*-+)", true);	//Instantiates Stringtokenizer object - splits expression into tokens (numbers/operators)
                
        opStack.push("$");																//Denotes "$" as bottom of stack        
        while(tokenizer.hasMoreTokens())												//Loops until no more tokens - reach end of expression
        {
        	String token = tokenizer.nextToken();										//Assigns next number or operator as token
        	
        	if( isOperator(token) )														//Token is operator
          	{
        		while( higherPrecedence(opStack.peek(), token) )						//Loops as long as top of stack has higher precedence than current operator
        		{
        			postfix += " "+opStack.pop();										//Pops top of stack and added to postfix string
        		}
        	
        		opStack.push(token);													//Pushes current operator into stack
        	}        	
    		else if(token.equals("(") )													//Token is left parenthesis
    		{
    			opStack.push(token);													//Pushes left parenthesis into stack
    		}
    		else if(token.equals(")") )													//Token is right parenthesis
    		{
    			while( !(opStack.peek().equals("(")) )									//Loops until corresponding left parenthesis is found
    			{
    				postfix += " "+opStack.pop();										//Pops top of stack and added to postfix string
    			}
    			opStack.pop();															//Pops and removes left parenthesis
    		}        	
        	else																		//Token is number
        	{
        		postfix += " "+token;													//Adds number to postfix string
        	}
        }
        
        while(!(opStack.peek().equals("$")))											//Removes any remaining operators in the stack
        {
        	postfix += " "+opStack.pop();
        }
        
		return postfix;
	}
	

}
