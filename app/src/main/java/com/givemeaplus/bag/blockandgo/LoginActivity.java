package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by Hye Jeong on 2015-11-27.
 */
public class LoginActivity extends Activity {

    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enter = (Button)findViewById(R.id.btn_enter);

    }
}
