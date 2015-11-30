package com.givemeaplus.bag.blockandgo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import android.os.Handler;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class WaitingRoomActivity extends Activity {

    ListView mainListView;
    BaseAdapter_Room mAdapter;

    PlayerInformation myPlayer;
    String enemyName;

    PopupWindow mPopupWindow = null;
    View mPopupLayout = null;

    Button ready_red, ready_blue, close;
    TextView redName, blueName, roomInfo;

    boolean ready_count_red, ready_count_blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        ready_count_red = true;     // 나중에 false로 바꿔놓을것!
        ready_count_blue = false;

        myPlayer = PlayerInformation.getMyPlayer();

        // 나중에 지울것 **테스트용
        enemyName = "HI Hyejeong";


        mPopupLayout = getLayoutInflater().inflate(R.layout.popupwindow_waitingroom, null);

        ready_red = (Button)mPopupLayout.findViewById(R.id.btn_ready_red);
        ready_blue = (Button)mPopupLayout.findViewById(R.id.btn_ready_blue);
        close = (Button)mPopupLayout.findViewById(R.id.btn_close);

        redName = (TextView)mPopupLayout.findViewById(R.id.waiting_name_red);
        blueName = (TextView)mPopupLayout.findViewById(R.id.waiting_name_blue);
        roomInfo = (TextView)mPopupLayout.findViewById(R.id.popup_roomInfo);


        mainListView = (ListView)findViewById(R.id.listview_room);
        mainListView.setDivider(new ColorDrawable(Color.rgb(200, 200, 200)));
        mainListView.setDividerHeight(3);

        mAdapter = new BaseAdapter_Room(this);
        mainListView.setAdapter(mAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int a, long id) {

                myPlayer.setType(2);

                Room tmp = mAdapter.getItem(a);

                //enemyplayer 정보 받아오고나서 팝업띄워야해

                popupWaitingRoom(tmp);
                // 다이얼로그? 로 상대방 들어오는거 대기타기!
            }
        });

    }

    public void popupWaitingRoom(Room tmp){

        mPopupWindow = new PopupWindow(mPopupLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);

        // 뒤로버튼 눌렀을 때 방 나가는거도 고려해야해!
        // 들어오면 ready 계속 체크하면서 버튼 이미지 변경해주는 스레드 돌리기

        // 방을 선택해서 들어가거나, 직접 만들어서 들어가는 경우
        // 선택해서 들어가는 경우 상대방을 먼저 서버로부터 받고 red/blue 배치시킬것
        // 만들어서 들어가는 경우 나를 red에 넣고 상대방 기다리기
        // 상대방을 서버로 받을 때 받아야하는 정보는 이름이랑 색깔(보내줘야하는것도 동일하군)

        roomInfo.setText("Room " + tmp.num +"   "+ tmp.name);

        if(myPlayer.getType()==1) {
            redName.setText(myPlayer.getName());
            ready_blue.setEnabled(false);
            // 상대방 오는거 기다리기 - CLient에서 스레드 돌리기
        }
        else{
            redName.setText(enemyName);
            blueName.setText(myPlayer.getName());
            ready_red.setEnabled(false);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ready_count_red = true;     //false로 바꿔줄 것!
                ready_count_blue = false;

                close.setEnabled(true);
                ready_red.setEnabled(true);
                ready_blue.setEnabled(true);

                ready_red.setBackgroundResource(R.drawable.btn_not_ready);
                ready_blue.setBackgroundResource(R.drawable.btn_not_ready);

                mPopupWindow.dismiss();
            }
        });

        ready_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ready_count_red) ready_red.setBackgroundResource(R.drawable.btn_not_ready);
                else ready_red.setBackgroundResource(R.drawable.btn_ready_red);
                ready_count_red = !ready_count_red;
            }
        });

        ready_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ready_count_red) ready_blue.setBackgroundResource(R.drawable.btn_not_ready);
                else ready_blue.setBackgroundResource(R.drawable.btn_ready_blue);
                ready_count_blue = !ready_count_blue;
            }
        });

        // 게임시작띄우고 레디버튼 회색으로 만드는 스레드
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    if(ready_count_red && ready_count_blue) {
                        ready_count_red=true;   // 네트워크 하면 false로 바꿀것
                        ready_count_blue=false;
                        break;
                    }
                }
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        close.setEnabled(false);
                        ready_red.setEnabled(false);
                        ready_blue.setEnabled(false);

                        Toast.makeText(getApplicationContext(), "잠시 후 게임이 시작됩니다!", Toast.LENGTH_SHORT).show();
                    }
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ready_red.setBackgroundResource(R.drawable.btn_not_ready);
                        ready_blue.setBackgroundResource(R.drawable.btn_not_ready);
                    }
                });
            }
        });
        t.start();

        // 3초 뒤 인텐트 넘어가는 작업
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(ready_count_red && ready_count_blue) {
                        break;
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(WaitingRoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        t2.start();

        mPopupWindow.showAtLocation(mainListView, Gravity.CENTER_VERTICAL, 0, 0);

    }


    @Override
    protected void onStop() {
        super.onStop();


        if(mPopupWindow != null) {

            //연결 끊기

            ready_count_red = true; // false로 바꿔줄것
            ready_count_blue = false;

            close.setEnabled(true);
            ready_red.setEnabled(true);
            ready_blue.setEnabled(true);

            ready_red.setBackgroundResource(R.drawable.btn_not_ready);
            ready_blue.setBackgroundResource(R.drawable.btn_not_ready);

            mPopupWindow.dismiss();
        }

    }
}
