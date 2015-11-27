package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class WaitingRoomActivity extends Activity {

    ListView mainListView;
    BaseAdapter_Room mAdapter;

    long backKeyPressedTime = 0;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        backKeyPressedTime = 0;
        toast = new Toast(this);

        mainListView = (ListView)findViewById(R.id.listview_room);
        mainListView.setDivider(new ColorDrawable(Color.rgb(200, 200, 200)));
        mainListView.setDividerHeight(3);


        mAdapter = new BaseAdapter_Room(this);
        mainListView.setAdapter(mAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int a, long id) {

                // 다이얼로그? 로 상대방 들어오는거 대기타기!
                    Intent intent = new Intent(WaitingRoomActivity.this, MainActivity.class);
                    startActivity(intent);
            }
        });

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
