package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class StartActivity extends Activity {

    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Toast.makeText(getApplicationContext(), "서버에 접속중입니다...", Toast.LENGTH_LONG).show();

        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(i++<4){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(StartActivity.this, WaitingRoomActivity.class);
                startActivityForResult(intent, 0);

            }
        });

        timer.start();

        // 서버에 접속을 지금 해두자 되면 다음 화면으로 넘어가도록!
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            finish();
        }
    }
}
