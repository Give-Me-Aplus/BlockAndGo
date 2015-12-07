package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class StartActivity extends Activity {

    private ProgressDialog Loading;

    Client client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        client = Client.getInstance(this);

        Loading = new ProgressDialog(StartActivity.this);
        Loading.setCancelable(true);
        Loading.setCanceledOnTouchOutside(false);
        Loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Loading.setMessage("서버에 접속 중 입니다...");

        makeDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(!Client.isConnected());

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        }).start();

    }

    public void makeDialog(){    // 버퍼링 다이얼로그를 생성하기에 앞서 핸들러를 이용한 쓰레드
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Handler mHandler = new Handler(Looper.getMainLooper());

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Loading.show();
                    }
                });


            }
        });
        t.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(Loading.isShowing() && Client.isConnected()) Loading.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){

            client.missingConnection();

            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if((keyCode== KeyEvent.KEYCODE_BACK)) {
            if(Loading.isShowing()) Loading.dismiss();
            else finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
