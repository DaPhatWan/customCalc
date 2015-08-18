package nicklau.android.customcalc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {

    private TextView mCalculatorInput, mCalculatorOutput;
    private String expression = "";
    private static final String DIGITS = "0123456789.";
    private CalculatorBrain mCalculatorBrain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculatorBrain = new CalculatorBrain();
        mCalculatorInput = (TextView)findViewById(R.id.Input);
        mCalculatorOutput = (TextView)findViewById(R.id.Output);

        //numbers
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button0).setOnClickListener(this);

        //operations
        findViewById(R.id.buttonEquals).setOnClickListener(this);

        findViewById(R.id.buttonDecimalPoint).setOnClickListener(this);
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSubtract).setOnClickListener(this);
        findViewById(R.id.buttonMultiply).setOnClickListener(this);
        findViewById(R.id.buttonDivide).setOnClickListener(this);
        findViewById(R.id.buttonPower).setOnClickListener(this);
        findViewById(R.id.buttonToggleSign).setOnClickListener(this);

        findViewById(R.id.buttonClear).setOnClickListener(this);

        findViewById(R.id.buttonLeftParenthesis).setOnClickListener(this);
        findViewById(R.id.buttonRightParenthesis).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        String buttonPressed = ( (Button) v).getText().toString();

        final HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.hsv1);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 50L);

        //Number or period was pressed
        if(DIGITS.contains(buttonPressed) || buttonPressed.equals("+/-") )
        {
            if(buttonPressed.equals("+/-") )
            {
                expression += mCalculatorBrain.numberPressed(buttonPressed);
                mCalculatorInput.setText(expression);
            }
            else {
                expression += mCalculatorBrain.numberPressed(buttonPressed);
                mCalculatorInput.setText(expression);
            }
        }
        //Operator was pressed
        else
        {
            if(buttonPressed.equals("CLR") )
            {
                mCalculatorBrain.clearPressed();
                expression = "";
                mCalculatorInput.setText("");
                mCalculatorOutput.setText("");
            }
            else if(buttonPressed.equals("+") || buttonPressed.equals("-") ||
                    buttonPressed.equals("x") || buttonPressed.equals("÷") ||
                    buttonPressed.equals("^") )
            {
                if(expression.length()==0 || expression.substring(expression.length()-1)=="-" )
                {
                    //
                }
                else {
                    expression = mCalculatorBrain.operatorPressed(buttonPressed);
                    mCalculatorInput.setText(expression);
                }
            }
            else if(buttonPressed.equals("(") || buttonPressed.equals(")") )
            {
                expression = mCalculatorBrain.parenthesisPressed(buttonPressed);
                mCalculatorInput.setText(expression);
            }
            else if(buttonPressed.equals("=") )
            {
                if(expression.length()==0)
                {
                    //
                }
                else {
                    mCalculatorOutput.setText( mCalculatorBrain.equalsPressed
                            (mCalculatorInput.getText().toString()));
                    expression = "";
                    mCalculatorBrain.completed();
                }
            }

        }
    }

}