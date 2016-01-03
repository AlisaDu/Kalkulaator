package com.alisa.kalkulaator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    //result screen
    private EditText screen;
    private Button btnEq;
    double value = 0;
    String operation = "";
    boolean operationPressed = false;
    private Animation animation; // button animation
    private boolean picFlag = false; //controls appearance of screen
    private boolean animFlag = true; // controls the animation of Eq button, when pressed by hand or programmatically


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
        btnEq = (Button) findViewById(R.id.buttonEq);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("value", value);
        savedInstanceState.putString("operation", operation);
        savedInstanceState.putBoolean("pressed", operationPressed);
        savedInstanceState.putBoolean("screenState", picFlag);
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
                    animFlag =false;

                    btnEq.performClick();
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



                if (animFlag) {
                    view.startAnimation(animation);
                    broadcastIntent(view);
                }
                    animFlag = true;
                    broadcastIntent(view);
                    operationPressed = true;
                    operation = "";
                break;
            }

            // all numeric buttons
            default:{
                view.startAnimation(animation);
                Button b = (Button)view;

                // in case of new input, clear screen
                if ((String.valueOf(screen.getText()).equals(getResources().getString(R.string.dflt))) ||(String.valueOf(screen.getText()).equals(getResources().getString(R.string.zero)))|| (operationPressed)) {
                    operationPressed = false;
                    screen.setText("");
                }
                // else concatenate to existing
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




    public void broadcastIntent(View view){

        if ( operation != "") {
            Intent intent = new Intent();
            intent.setAction("com.alisa.sendCalcRequest");
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.putExtra("firstOp", value);
            intent.putExtra("secondOp", getDouble(screen.getText()));
            intent.putExtra("operation", operation);
            sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String feedback = getResultData();
                    screen.setText(feedback);
                    value = getDouble(feedback);

                }
            }, null, Activity.RESULT_OK, null, null);
        }
    }
}
