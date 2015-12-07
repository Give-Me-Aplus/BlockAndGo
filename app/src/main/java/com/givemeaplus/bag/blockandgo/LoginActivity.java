package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hye Jeong on 2015-11-27.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    Button enter;
    EditText text;

    Client connection;

    long backKeyPressedTime = 0;
    Toast toast;

    PlayerInformation myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myPlayer = PlayerInformation.getMyPlayer();

        backKeyPressedTime = 0;
        toast = new Toast(this);

        connection = Client.getInstance(this);

        text = (EditText)findViewById(R.id.edit_name);
        enter = (Button)findViewById(R.id.btn_enter);
        enter.setOnClickListener(this);


        text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                }
            }
        });



    }

    @Override
    public void onClick(View v) {

        String name = text.getText().toString().trim();

        if(name.compareTo("")==0){
            Toast.makeText(getApplicationContext(), "Put your nickname!", Toast.LENGTH_SHORT).show();
        }
        else if(name.contains("#")){
            Toast.makeText(getApplicationContext(), "Cannot use '#' in nickname", Toast.LENGTH_SHORT).show();
        }
        else {
            connection.sendName(name);

            myPlayer.setName(name);

            Intent intent = new Intent(LoginActivity.this, WaitingRoomActivity.class);
            startActivity(intent);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if((keyCode== KeyEvent.KEYCODE_BACK)) {

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast.makeText(getApplicationContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
                toast.cancel();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
