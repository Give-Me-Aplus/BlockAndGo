package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class WaitingRoomActivity extends Activity {

    ListView mainListView;
    BaseAdapter_Room mAdapter;

    PlayerInformation myPlayer;

    Button addRoom, setting, refreshRoom;

    Client client = null;

    // 방 입장
    PopupWindow mPopupWindow = null;
    View mPopupLayout_WaitingRoom = null;

    static Button ready_red, ready_blue, close;
    static TextView redName, blueName, roomInfo;

    static boolean isGameStart = false;
    static boolean isRoomLoaded = false;
    static boolean isRoomError = false;

    static String roomNum = null;

    // 방만들기
    PopupWindow mPopupWindow_MakingRoom = null;
    View mPopupLayout_MakingRoom = null;

    EditText roomName;
    Button makeRoom, closeMakeRoom;

    // 설정 프리퍼런스
    PopupWindow mPopupWindow_setting = null;
    View mPopupLayout_setting = null;

    CheckBox background, effect;
    Button btn_help, btn_developer, close_setting;

    CustomPreference preference;

    private ProgressDialog Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingroom);

        client = Client.getInstance(this);
        myPlayer = PlayerInformation.getMyPlayer();
        preference = CustomPreference.getInstance(this);

        Loading = new ProgressDialog(WaitingRoomActivity.this);
        Loading.setCancelable(true);
        Loading.setCanceledOnTouchOutside(false);
        Loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Loading.setMessage("방을 불러오는 중입니다...");

        addRoom = (Button)findViewById(R.id.btn_add);
        setting = (Button)findViewById(R.id.btn_setting);
        refreshRoom = (Button)findViewById(R.id.btn_refresh);


        // 대기실 팝업윈도우 변수 초기화
        mPopupLayout_WaitingRoom = getLayoutInflater().inflate(R.layout.popupwindow_waitingroom, null);

        ready_red = (Button)mPopupLayout_WaitingRoom.findViewById(R.id.btn_ready_red);
        ready_blue = (Button)mPopupLayout_WaitingRoom.findViewById(R.id.btn_ready_blue);
        close = (Button)mPopupLayout_WaitingRoom.findViewById(R.id.btn_close);

        redName = (TextView)mPopupLayout_WaitingRoom.findViewById(R.id.waiting_name_red);
        blueName = (TextView)mPopupLayout_WaitingRoom.findViewById(R.id.waiting_name_blue);
        roomInfo = (TextView)mPopupLayout_WaitingRoom.findViewById(R.id.popup_roomInfo);


        // 방만들기 팝업윈도우 변수 초기화
        mPopupLayout_MakingRoom = getLayoutInflater().inflate(R.layout.popupwindow_makingroom, null);

        roomName = (EditText)mPopupLayout_MakingRoom.findViewById(R.id.makingroom_name);
        makeRoom = (Button)mPopupLayout_MakingRoom.findViewById(R.id.btn_make);
        closeMakeRoom = (Button)mPopupLayout_MakingRoom.findViewById(R.id.btn_close_makingroom);


        // 프리퍼런스 팝업윈도우 변수 초기화
        mPopupLayout_setting = getLayoutInflater().inflate(R.layout.popupwindow_setting, null);

        background = (CheckBox) mPopupLayout_setting.findViewById(R.id.check_backgroundMusic);
        effect = (CheckBox) mPopupLayout_setting.findViewById(R.id.check_effectSound);

        btn_help = (Button) mPopupLayout_setting.findViewById(R.id.btn_help);
        btn_developer = (Button) mPopupLayout_setting.findViewById(R.id.btn_developer);
        close_setting = (Button) mPopupLayout_setting.findViewById(R.id.close_setting);


        // 리스트뷰
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
                client.requestRoom(tmp.num);

                roomNum = tmp.num;
                roomInfo.setText("Room " + tmp.num + "   " + tmp.name);

                Loading.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Handler mHandler = new Handler(Looper.getMainLooper());

                        while(true){

                            if(isRoomLoaded ||isRoomError) break;
                        }

                        if(isRoomLoaded){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    popupWaitingRoom();
                                }
                            });
                        }

                        isRoomLoaded = false;
                        isRoomError = false;

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Loading.dismiss();
                            }
                        });

                    }
                }).start();
            }
        });

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMakeRoom();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSetting();
            }
        });

        refreshRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.refresh();
            }
        });

    }

    public void popupSetting(){
        mPopupWindow_setting = new PopupWindow(mPopupLayout_setting, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow_setting.setFocusable(true);
        mPopupWindow_setting.setOutsideTouchable(false);

        background.setChecked(preference.getBackgroundMusicState());
        effect.setChecked(preference.getEffectSoundState());

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (background.isChecked()) {
                    preference.setBackgroundMusicState(true);
                } else {
                    preference.setBackgroundMusicState(false);
                }
            }
        });

        effect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(background.isChecked()){
                    preference.setEffectSoundState(true);
                }
                else{
                    preference.setEffectSoundState(false);
                }
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        close_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow_setting.dismiss();
            }
        });


        mPopupWindow_setting.showAtLocation(mainListView, Gravity.CENTER_VERTICAL, 0, 0);
    }

    public void popupMakeRoom(){
        mPopupWindow_MakingRoom = new PopupWindow(mPopupLayout_MakingRoom, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow_MakingRoom.setFocusable(true);
        mPopupWindow_MakingRoom.setOutsideTouchable(false);

        // 키보드 바깥 터치하면 키보드 사라지는 기능
        roomName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(roomName.getWindowToken(), 0);
                }
            }
        });

        closeMakeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow_MakingRoom.dismiss();
            }
        });

        makeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = roomName.getText().toString().trim();

                if (tmp.compareTo("") == 0)
                    Toast.makeText(getApplicationContext(), "Put name of room!", Toast.LENGTH_SHORT).show();
                else if (tmp.contains("#"))
                    Toast.makeText(getApplicationContext(), "Cannot contain '#' in the name!", Toast.LENGTH_SHORT).show();
                else {
                    client.makeRoom(tmp);

                    mPopupWindow_MakingRoom.dismiss();
                    popupWaitingRoom();

                }
            }
        });

        mPopupWindow_MakingRoom.showAtLocation(mainListView, Gravity.CENTER_VERTICAL, 0, 0);
    }

    // 게임 직전 레디를 위해 띄우는 대기 화면
    // 서버로부터 방을 확실히 받아 온 뒤에 띄울 것
    public void popupWaitingRoom(){

        mPopupWindow = new PopupWindow(mPopupLayout_WaitingRoom, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 방에서 나간다고 서버에 보내야돼
                client.exitRoom();

                roomNum = "";

                roomInfo.setText("방을 불러오는 중입니다...");
                roomName.setText("");
                redName.setText("");
                blueName.setText("");

                close.setEnabled(true);
                ready_red.setEnabled(true);
                ready_blue.setEnabled(true);

                ready_red.setBackgroundResource(R.drawable.btn_not_ready);
                ready_blue.setBackgroundResource(R.drawable.btn_not_ready);

                mPopupWindow.dismiss();
                mAdapter.refresh();
            }
        });

        // 나의 ready 정보를 보내야 함
        ready_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendReady();
            }
        });

        ready_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendReady();
            }
        });

        mPopupWindow.showAtLocation(mainListView, Gravity.CENTER_VERTICAL, 0, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isGameStart){
            isGameStart = false;
            client.requestRoom(roomNum);
            popupWaitingRoom();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPopupWindow != null) {

            if(!isGameStart) client.exitRoom();

            roomInfo.setText("방을 불러오는 중입니다...");
            redName.setText("");
            blueName.setText("");

            close.setEnabled(true);
            ready_red.setEnabled(true);
            ready_blue.setEnabled(true);

            ready_red.setBackgroundResource(R.drawable.btn_not_ready);
            ready_blue.setBackgroundResource(R.drawable.btn_not_ready);

            mPopupWindow.dismiss();
        }
    }
}
