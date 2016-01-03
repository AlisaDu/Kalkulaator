package com.alisa.kalkulaator;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    double value = 0;
    String operation = "";
    boolean operationPressed = false;
    private Animation animation;
    private boolean picFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (EditText) findViewById(R.id.txtResult);

        animation = AnimationUtils.loadAnimation(this, R.anim.button_anim);


        if (savedInstanceState != null){
            value =savedInstanceState.getDouble("value");
            operation = savedInstanceState.getString("operation");
            operationPressed = savedInstanceState.getBoolean("pressed");
            picFlag = savedInstanceState.getBoolean("screenState");
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
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("value", value);
        savedInstanceState.putString("operation", operation);
        savedInstanceState.putBoolean("pressed", operationPressed);
        savedInstanceState.putBoolean ("screenState", picFlag);

    }

    public void buttonClick (View view) {
        screen.setSelected(true);
        picFlag = true;
        switch (view.getId()){
            // any math operation button
            case R.id.buttonSum:
            case R.id.buttonSub:
            case R.id.buttonMlt:
            case R.id.buttonDiv: {
                view.startAnimation(animation);
                Button b = (Button)view;

                if (value != 0){
                    btnEq.performClick();
                    operationPressed = true;
                    operation = b.getText().toString();
                } else {
                    operationPressed = true;
                    operation = b.getText().toString();
                    value = getDouble(screen.getText());
                }


                break;
            }


            case R.id.buttonC:{
                screen.setSelected(false);
                picFlag = false;
                view.startAnimation(animation);
                screen.setText(R.string.dflt);
                value = 0;
                operation = "";
                operationPressed = false;


                break;
            }

            case R.id.buttonEq:{
                view.startAnimation(animation);
                switch (operation){

                    case "+":{
                        screen.setText (String.valueOf(value + getDouble (screen.getText())));
                        break;
                    }
                    case "-":{
                        screen.setText (String.valueOf(value - getDouble (screen.getText())));
                        break;
                    }
                    case "*":{
                        screen.setText (String.valueOf(value * getDouble (screen.getText())));
                        break;
                    }
                    case "/":{
                        screen.setText (String.valueOf(value / getDouble (screen.getText())));
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
            default:{
                view.startAnimation(animation);

                Button b = (Button)view;

                // in case of new input, start from scratch
                if ((String.valueOf(screen.getText()).equals(getResources().getString(R.string.dflt))) ||(String.valueOf(screen.getText()).equals(getResources().getString(R.string.zero)))|| (operationPressed == true)) {
                    operationPressed = false;
                    screen.setText("");
                }
                screen.setText(screen.getText() + (b.getText()).toString());


            }
        }// end button switch
    }



    private double getDouble(Object screenInput) {
        double result;
        try{
            result = Double.valueOf(screenInput.toString());
        } catch (Exception e){
            screen.setText(R.string.error);
            e. printStackTrace();
            result = 0;
        }
        return result;


    }


}
