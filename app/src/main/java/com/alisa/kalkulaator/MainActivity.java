package com.alisa.kalkulaator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;




public class MainActivity extends AppCompatActivity {

    //screen
    private EditText screen;

    //  operation buttons
    private Button btnSum;
    private Button btnSub;
    private Button btnMlt;
    private Button btnDiv;
    private Button btnEq;

    private String value1 = "";
    private String value2 = "";
    private String operation = "";
    private boolean operationPressed = false;
    private Animation animation;
    private boolean picFlag = false;
    public String result ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (EditText) findViewById(R.id.txtResult);

        animation = AnimationUtils.loadAnimation(this, R.anim.button_anim);


        if (savedInstanceState != null) {
            value1 = savedInstanceState.getString("value1");
            value2 = savedInstanceState.getString("value2");
            operation = savedInstanceState.getString("operation");
            operationPressed = savedInstanceState.getBoolean("pressed");
            picFlag = savedInstanceState.getBoolean("flag");
            screen.setSelected(picFlag);
        }
        screen.setText(R.string.dflt);
        btnSum = (Button) findViewById(R.id.buttonSum);
        btnSub = (Button) findViewById(R.id.buttonSub);
        btnMlt = (Button) findViewById(R.id.buttonMlt);
        btnDiv = (Button) findViewById(R.id.buttonDiv);
        btnEq = (Button) findViewById(R.id.buttonEq);


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("value1", value1);
        savedInstanceState.putString ("value2", value2);
        savedInstanceState.putString("operation", operation);
        savedInstanceState.putBoolean("pressed", operationPressed);
        savedInstanceState.putBoolean("flag", picFlag);

    }

    public void buttonClick(View view) {
        screen.setSelected(true);
        picFlag = true;
        switch (view.getId()) {
            // any math operation button
            case R.id.buttonSum:
            case R.id.buttonSub:
            case R.id.buttonMlt:
            case R.id.buttonDiv: {
                view.startAnimation(animation);
                Button b = (Button) view;

                if (!value1.equals("")) {
                    Intent intent=new Intent("com.alisa.calculator.action.calculate");
                    this.startService(intent);
                    //////////////////////////////
                    btnEq.performClick();
                    ////////////////////////////////////
                    operationPressed = true;
                    operation = b.getText().toString();
                } else {
                    operationPressed = true;
                    operation = b.getText().toString();
                    value1 = String.valueOf(screen.getText());
                }

                break;
            }


            case R.id.buttonC: {
                screen.setSelected(false);
                picFlag = false;
                view.startAnimation(animation);
                screen.setText(R.string.dflt);
                value1 = "";
                operation = "";
                operationPressed = false;


                break;
            }

            case R.id.buttonEq: {

                view.startAnimation(animation);
                switch (operation) {

                    case "+": {
                        screen.setText(String.valueOf(value + getDouble(screen.getText())));
                        break;
                    }
                    case "-": {
                        screen.setText(String.valueOf(value - getDouble(screen.getText())));
                        break;
                    }
                    case "*": {
                        screen.setText(String.valueOf(value * getDouble(screen.getText())));
                        break;
                    }
                    case "/": {
                        screen.setText(String.valueOf(value / getDouble(screen.getText())));
                        break;
                    }
                    default:
                        break;
                }// end eq switch
                value = getDouble(screen.getText());
                operation = "";
                operationPressed = true;


                break;
            }

            // all numeric buttons
            default: {
                view.startAnimation(animation);

                Button b = (Button) view;

                // in case of new input, start from scratch
                if ((String.valueOf(screen.getText()).equals(getResources().getString(R.string.dflt))) || (String.valueOf(screen.getText()).equals(getResources().getString(R.string.zero))) || (operationPressed == true)) {
                    operationPressed = false;
                    screen.setText("");
                }
                screen.setText(screen.getText() + (b.getText()).toString());


            }
        }// end button switch
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                result = bundle.getString("EXTRA_RESULT");

            }
        }
    };
}

