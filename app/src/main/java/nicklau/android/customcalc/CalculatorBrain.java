package nicklau.android.customcalc;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CalculatorBrain extends PostfixCalculator {

    private String mEquation, mOperand;

    private static List<String> operand = new ArrayList<>();
    private static List<String> expression = new ArrayList<String>();

    public static final String DECIMAL = ".";
    public static final String TOGGLESIGN = "+/-";

    //Checks if the input is a number or not
    public static boolean isNumeric(String input)
    {
        return input.matches("[-+]?\\d+(\\.\\d+)?");
    }

    //Checks if input is an operator or not
    public static boolean isOperator(String input)
    {
        return ( input.equals("^") ||  input.equals("x")
                || input.equals("÷") || input.equals("+") || input.equals("-") );
    }

    public int countNumberEqual(List<String> itemList, String itemToCheck)
    {
        int count = 0;
        for (String i : itemList)
        {
            if (i.equals(itemToCheck)) {
                count++;
            }
        }
        return count;
    }

    //Default Constructor
    public CalculatorBrain()
    {
        operand.clear();
        expression.clear();
        mOperand = "";
        mEquation = "";
    }

    //Number was pressed.
    //"." counts as a number - checks if decimal is already used or not
    public String numberPressed(String number)
    {
        if (number.equals(TOGGLESIGN))
        {
            if (operand.contains("-") || !operand.isEmpty()) {
                //Do nothing - prevents ability ot enter multiple unary minus signs
                return "";
            }
            else                                //Should be the first thing in operand arrayList
            {
                operand.add( 0, "-");
                mOperand = "-";
            }

        } else if (number.equals(DECIMAL))        //Typing a decimal
        {
            if (operand.contains(DECIMAL)) {
                //Do nothing - prevents ability to enter multiple decimals
                return "";
            } else                                //In the middle of typing number
            {
                operand.add(DECIMAL);
                mOperand = ".";
            }
        } else                                    //Typing a digit
        {
            operand.add(number);
            mOperand = number;
        }
        return mOperand;
    }

    //Clear was pressed.
    //Clears all variables and arrayLists in CalculatorBrain
    public void clearPressed()
    {
        operand.clear();
        expression.clear();
        mOperand = "";
        mEquation = "";
    }

    //Mathematical operator was pressed.
    public String operatorPressed(String operator)
    {
        if(!operand.isEmpty() && !operand.get(operand.size()-1).equals("-")) {
            String joined = TextUtils.join("", operand);        //Makes arrayList operand into string number
            expression.add(joined);
            operand.clear();                                    //Clears arrayList for next number
        }

        if( expression.size()!=0 )
        {
            if (isOperator(expression.get(expression.size() - 1)))
            {
                expression.set(expression.size()-1, operator);
                mEquation = TextUtils.join("", expression);

            } else if (isNumeric(expression.get(expression.size() - 1)) || expression.get(expression.size() - 1).equals(")")) {
                expression.add(operator);                                                   //Adds current operator
                mEquation = TextUtils.join("", expression);                                 //Turns current collection of the expression into string
            }
        }

        return mEquation;
    }

    //Parenthesis buttons pressed.
    public String parenthesisPressed(String parenthesis)
    {
        String joined = TextUtils.join("", operand);                                        //Makes arrayList operand into string number
        expression.add(joined);
        operand.clear();

        if(parenthesis.equals("(") )
        {
            expression.add(parenthesis);
            mEquation = TextUtils.join("", expression);
        }
        else if(parenthesis.equals(")") )
        {
            if( countNumberEqual(expression, "(") > countNumberEqual(expression, ")") &&
                    (isNumeric( expression.get(expression.size()-1 ) ) ) )
            {
                expression.add(parenthesis);
                mEquation = TextUtils.join("", expression);
            }
            else
                mEquation = TextUtils.join("", expression);
        }
        return mEquation;
    }

    //Equal sign was pressed
    //Checks for syntax errors.
    public String equalsPressed(String equation)
    {
        if(!operand.isEmpty() && !operand.get(operand.size()-1).equals("-")) {
            String joined = TextUtils.join("", operand);        //Makes arrayList operand into string number
            expression.add(joined);
            operand.clear();                                    //Clears arrayList for next number
        }

        if( expression.size()!=0 && isOperator(expression.get(expression.size() - 1)))
        {
            return "Syntax Error";
        }
        else if( countNumberEqual(expression, "(") != countNumberEqual(expression, ")"))
        {
            return "Syntax Error";
        }
        else if (countNumberEqual(expression, "(") == countNumberEqual(expression, ")") )
        {
            return Double.toString(compute(convert(symbolChange(equation))) );
        }

        return Double.toString(compute(convert(symbolChange(equation))) );

    }

    //Clears arrayLists after solving problem
    public void completed()
    {
        operand.clear();
        expression.clear();
    }

    //Changes multiplication and division symbols
    // 'x' -> '*' and '÷' -> '/'
    public String symbolChange(String expression)
    {
        expression = expression.replace('x','*');
        expression = expression.replace('÷', '/');
        return expression;
    }

}
