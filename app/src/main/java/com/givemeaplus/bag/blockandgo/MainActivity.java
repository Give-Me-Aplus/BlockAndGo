package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.*;

public class MainActivity extends Activity {

    LinearLayout parentLayout;

    Timer timer;
    TimerTask task;
    Handler mHandler;

    BoardState mBoardState;

    Client client = null;

    public static Button[][] blockArr = new Button[11][7];
    public static Button[][] wall_hArr = new Button[10][7];
    public static Button[][] wall_vArr = new Button[11][6];


    PlayerInformation myPlayer;

    static boolean isMyTurn = false;
    static boolean startCountDown = false;
    int countDown = 30;


    /*activity_main.xml애서 기능적으로 사용되는 View들*/
    //플레이어 이름 알려주는 TextView
    static TextView player1Name;
    static TextView player2Name;

    //상대방의 남은 벽 갯수 나타내는 버튼들
    //있으면 색으로 채워져있고 없으면 어둡게 변함
    Button[] enemyWall;

    //나의 남은 벽 갯수 나타내는 버튼들
    //있으면 색으로 채워져있고 없으면 어둡게 변함
    Button[] myWall;

    //남은시간 알려주는 TextView
    TextView timeLimit;

    //획득한 아이템 획득한거 알려주는 버튼
    //획득하면 점등되고 사용하면 꺼짐
    //완쪽 부터 01, 02, 03 이고 각각 2회이동, 벽갯수+=1, 벽하나 없애기 이다.
    Button myitem01;
    Button myitem02;
    Button myitem03;

    //아래 3개의 버튼은 게임화면 우측 하단에 있는 빨간색 동그라미버튼.
    //매 스테이지 마다 저 3가지중 한가지만 선택가능.
    Button moveBtn;
    Button wallBtn;
    Button itemBtn;
    /*activity_main.xml애서 기능적으로 사용되는 View들.끝*/



    /*게임을 진행하면서 사용되는 변수들*/

    int enemy_i, enemy_j;//적위치
    int my_i, my_j;//내위치
    int[] itemState = new int[3];//아이템 획득하면 아이템에 해당하는 인덱스에 1 들어간다
    int[][] itemLocation;//아이템의 위치 저장하는 배열. 각 인덱스에 해당하는 아이템의 좌표가 들어간다.
    int numOfWall;//남은 벽 갯수
    int numOfItem;//사용할 수 있는 아이템 갯수


    int firstClicked_hi;//벽이 두번 눌리면 변경되도록할때 이전에 누른 버튼 위치 저장하는 변수들
    int firstClicked_hj;
    int firstClicked_vi;
    int firstClicked_vj;

    int delete_hi;//벽 지울때 벽이 두번 눌리면 지워지도록 할 때 이전에 누른 버튼 위치 저장하는 변수들
    int delete_hj;
    int delete_vi;
    int delete_vj;


    public void setViews(){

        player1Name = (TextView) findViewById(R.id.player_enemy);
        player2Name = (TextView) findViewById(R.id.player_me);

        enemyWall = new Button[8];
        enemyWall[7] = (Button) findViewById(R.id.enemywall08);
        enemyWall[6] = (Button) findViewById(R.id.enemywall07);
        enemyWall[5] = (Button) findViewById(R.id.enemywall06);
        enemyWall[4] = (Button) findViewById(R.id.enemywall05);
        enemyWall[3] = (Button) findViewById(R.id.enemywall04);
        enemyWall[2] = (Button) findViewById(R.id.enemywall03);
        enemyWall[1] = (Button) findViewById(R.id.enemywall02);
        enemyWall[0] = (Button) findViewById(R.id.enemywall01);

        myWall = new Button[8];
        myWall[0] = (Button) findViewById(R.id.mywall01);
        myWall[1] = (Button) findViewById(R.id.mywall02);
        myWall[2] = (Button) findViewById(R.id.mywall03);
        myWall[3] = (Button) findViewById(R.id.mywall04);
        myWall[4] = (Button) findViewById(R.id.mywall05);
        myWall[5] = (Button) findViewById(R.id.mywall06);
        myWall[6] = (Button) findViewById(R.id.mywall07);
        myWall[7] = (Button) findViewById(R.id.mywall08);

        timeLimit = (TextView) findViewById(R.id.time_limit);

        myitem01 = (Button) findViewById(R.id.myitem01);
        myitem02 = (Button) findViewById(R.id.myitem02);
        myitem03 = (Button) findViewById(R.id.myitem03);

        moveBtn = (Button) findViewById(R.id.btn_move);
        wallBtn = (Button) findViewById(R.id.btn_wall);
        itemBtn = (Button) findViewById(R.id.btn_item);

    }

    private View[] getChildViews(ViewGroup viewGroup) {

        int childCnt = viewGroup.getChildCount();
        final View[] childViews = new View[childCnt];

        for (int i = 0; i < childCnt; i++) {
            childViews[i] = viewGroup.getChildAt(i);
        }

        return childViews;
    }

    private void makeBtnArr(ViewGroup viewGroup) {

        View[] childViews = getChildViews(viewGroup);

        for (int i = 0; i < childViews.length; i++) {

            if (childViews[i] instanceof LinearLayout) {

                makeBtnArr((ViewGroup) childViews[i]);

            } else if (childViews[i] instanceof Button) {

                if (childViews[i].getTag() != null) {

                    String tagTemp = childViews[i].getTag().toString();

                    System.out.println(tagTemp);

                    int ii, ij;

                    if (tagTemp.contains("block")) {

                        String tmp = tagTemp.replace("block", "");
                        int index = Integer.parseInt(tmp);

                        ii = index / 10;
                        ij = index % 10;

                        blockArr[ii][ij] = (Button) childViews[i];

                    } else if (tagTemp.contains("wall_h")) {

                        String tmp = tagTemp.replace("wall_h", "");
                        int index = Integer.parseInt(tmp);

                        ii = index / 10;
                        ij = index % 10;

                        wall_hArr[ii][ij] = (Button) childViews[i];

                    } else if (tagTemp.contains("wall_v")) {

                        String tmp = tagTemp.replace("wall_v", "");
                        int index = Integer.parseInt(tmp);

                        ii = index / 10;
                        ij = index % 10;

                        wall_vArr[ii][ij] = (Button) childViews[i];

                    }
                }
            }
        }
    }






























    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = Client.getInstance(getBaseContext());
        mBoardState = BoardState.getInstance();
        myPlayer = PlayerInformation.getMyPlayer();
        mHandler = new Handler(Looper.getMainLooper());

        countDown = 30;

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        timeLimit.setText(""+countDown);
                        countDown -= 1;
                    }
                });

                if(countDown==0){
                    // 내턴일때만 타임아웃 보내기
                    if(isMyTurn) client.sendTimeOut();
                    countDown = 30;
                    timer.cancel();
                }

            }
        };

        parentLayout = (LinearLayout) findViewById(R.id.parent_layout);
        makeBtnArr(parentLayout);

        setViews();//사용하는 view 객체들 정의

        //사용되는 변수들 설정
        enemy_i = 0;    enemy_j = 3;
        my_i = 10;      my_j = 3;
        numOfWall = 8;
        numOfItem = 0;


        firstClicked_hi = -1;
        firstClicked_hj = -1;
        firstClicked_vi = -1;
        firstClicked_vj = -1;


        delete_hi = -1;
        delete_hj = -1;
        delete_vi = -1;
        delete_vj = -1;

        player1Name.setText(myPlayer.getEnemyName());
        player2Name.setText(myPlayer.getName());

        if(myPlayer.getType()==1){
            for(int i=0; i<7; i++){
                blockArr[10][i].setBackgroundColor(getResources().getColor(R.color.block_red));
                blockArr[0][i].setBackgroundColor(getResources().getColor(R.color.block_blue));
            }
            for(int j=0; j<8; j++){
                myWall[j].setBackgroundColor(getResources().getColor(R.color.wall_red));
                enemyWall[j].setBackgroundColor(getResources().getColor(R.color.wall_blue));
            }

            blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_red_redblock);
            blockArr[enemy_i][enemy_j].setBackgroundResource(R.drawable.player_blue_blueblock);

        }
        else{
            for(int i=0; i<7; i++){
                blockArr[10][i].setBackgroundColor(getResources().getColor(R.color.block_blue));
                blockArr[0][i].setBackgroundColor(getResources().getColor(R.color.block_red));
            }
            for(int j=0; j<8; j++){
                myWall[j].setBackgroundColor(getResources().getColor(R.color.wall_blue));
                enemyWall[j].setBackgroundColor(getResources().getColor(R.color.wall_red));
            }

            blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_blue_blueblock);
            blockArr[enemy_i][enemy_j].setBackgroundResource(R.drawable.player_red_redblock);

        }

        client.enterGame();

        onStage();
    }


















    public void onStage() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    if(startCountDown) {

                        timer.schedule(task, 500, 1000);

                        if (isMyTurn) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    player1Name.setBackgroundColor(getResources().getColor(R.color.isnotmyturn));
                                    player2Name.setBackgroundColor(getResources().getColor(R.color.ismyturn));
                                }
                            });
                            onChoose();
                            isMyTurn = false;
                        }
                        else{
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    player2Name.setBackgroundColor(getResources().getColor(R.color.isnotmyturn));
                                    player1Name.setBackgroundColor(getResources().getColor(R.color.ismyturn));
                                }
                            });
                        }

                        startCountDown = false;
                    }
                    // else 조건으로 게임이 끝났는지 안끝났는지 체크
                }
            }
        }).start();

    }

    public void onChoose() {

        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //벽과 모든 블록 버튼들 비활성화
                setUnclicked(blockArr);
                setUnclicked(wall_hArr);
                setUnclicked(wall_vArr);

                //현재 나의 위치 기반으로 갈수있는 곳 체크한다.
                checkRoute(my_i, my_j);
            }
        });

        wallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wallBtnSetting();

            }
        });

        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numOfItem > 0) {

                    if (itemState[0] == 1) {

                        myitem01.setEnabled(true);
                        myitem01.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //벽과 모든 블록 버튼들 비활성화
                                setUnclicked(blockArr);
                                setUnclicked(wall_hArr);
                                setUnclicked(wall_vArr);

                                checkRoute_2(my_i, my_j);
                            }
                        });
                    }
                    if (itemState[2] == 1) {


                        myitem03.setEnabled(true);
                        myitem03.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //벽과 모든 블록 버튼들 비활성화
                                setUnclicked(blockArr);
                                setUnclicked(wall_hArr);
                                setUnclicked(wall_vArr);

                                deleteWall();
                            }
                        });
                    }


                }
            }
        });

    }


    public void onStageFinish(int type){//자기 턴 끝나면 뱐경 사항 저장하는거


        switch(type/10){

            case 1://말움직인 경우

                client.characterMove();

                if(type == 12){

                    //벽 추가 아이템을 먹었다
                    client.changeWallNum();//이함수에서 움직인것도 보내줘야한다.
                }
                break;

            case 2://벽 놓았다면

                client.putWall();
                break;

            case 3://벽 뱄다면

                if(type == 30) {//wall_h 뺀 경우

                    client.deleteWall();//0은 h 그리고 뒤의 두 값은 눌린것의 i,j
                }else if(type ==31){

                    client.deleteWall();//1은 v 그리고 뒤의 두 값은 눌린것의 i,j
                }
                break;

            default:

                client.sendTimeOut();
                break;
        }
    }
















    public void onClickBlock(View v) {

        String id_index = v.getTag().toString();
        int index;

        id_index = id_index.replace("block", "");
        index = Integer.parseInt(id_index);
        int i = index / 10;
        int j = index % 10;

        System.out.println(i + " " + j);

        //최혜정이 만든 함수라서 setBlock() 내 생각이랑 좀 다르다. 다시 짜야 할듯  mBoardState.setBlock(i, j, true);
        mBoardState.setBlock(i,j,true);

        int item_id = eatItem(i, j);


        if(myPlayer.getType()==1) {
            if (i == 0)
                blockArr[my_i][my_j].setBackgroundColor(getResources().getColor(R.color.block_blue));
            else if(i == 10)
                blockArr[my_i][my_j].setBackgroundColor(getResources().getColor(R.color.block_red));
            else
                blockArr[my_i][my_j].setBackgroundColor(getResources().getColor(R.color.block_default));
        }
        else {
            if (i == 0)
                blockArr[my_i][my_j].setBackgroundColor(getResources().getColor(R.color.block_red));
            else if (i == 10)
                blockArr[my_i][my_j].setBackgroundColor(getResources().getColor(R.color.block_blue));
            else
                blockArr[my_i][my_j].setBackgroundColor(getResources().getColor(R.color.block_default));

        }

        my_i = i;
        my_j = j;

        if(myPlayer.getType()==1) {
            if (i == 0)
                blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_red_blueblock);
            else if(i == 10)
                blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_red_redblock);
            else
                blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_red_grayblock);
        }
        else{
            if (i == 0)
                blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_blue_redblock);
            else if(i == 10)
                blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_blue_blueblock);
            else
                blockArr[my_i][my_j].setBackgroundResource(R.drawable.player_blue_grayblock);

        }

        if(item_id == -1){

            onStageFinish(10);

        }else{

            onStageFinish(11);

        }


    }

    public void onClickWall_h(View v) {
        String id_index = v.getTag().toString();
        int index;

        id_index = id_index.replace("wall_h", "");
        index = Integer.parseInt(id_index);
        int i = index / 10;
        int j = index % 10;

        System.out.println("wall_h " + i + " " + j);

        if(mBoardState.checkARoute(i, j, 0) && mBoardState.checkARoute2(i, j, 0) == false){

            Toast.makeText(getBaseContext(), "You can't put a wall there!", Toast.LENGTH_SHORT).show();
        }else {


            //색
            if (j < 6) {
                wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
                wall_hArr[i][j + 1].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
            } else {
                wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
                wall_hArr[i][j - 1].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
            }

            if (firstClicked_hi == -1 && firstClicked_hj == -1) {
                //버튼이 처음 눌렸다.

                firstClicked_hi = i;
                firstClicked_hj = j;

            } else if (firstClicked_hi == i && firstClicked_hj == j) {
                //버튼 같은게 두번눌렸다!!!!!

                if (j < 6) {

                    putWall(0, i, j, i, j + 1);
                    wall_hArr[i][j].setEnabled(false);
                    wall_hArr[i][j + 1].setEnabled(false);
                    mBoardState.setWall_h(i, j, true);
                    mBoardState.setWall_h(i, j + 1, true);

                } else {

                    putWall(0, i, j, i, j - 1);
                    wall_hArr[i][j].setEnabled(false);
                    wall_hArr[i][j - 1].setEnabled(false);
                    mBoardState.setWall_h(i, j, true);
                    mBoardState.setWall_h(i, j - 1, true);

                }

                onStageFinish(20);

            } else {

                firstClicked_hi = i;
                firstClicked_hj = j;
            }
        }
    }

    public void onClickWall_v(View v) {
        String id_index = v.getTag().toString();
        int index;

        id_index = id_index.replace("wall_v", "");
        index = Integer.parseInt(id_index);
        int i = index / 10;
        int j = index % 10;

        System.out.println("wall_v " + i + " " + j);


        if(mBoardState.checkARoute(i, j, 1) && mBoardState.checkARoute2(i, j, 1) == false){

            Toast.makeText(getBaseContext(), "You can'y put a wall there!", Toast.LENGTH_SHORT).show();
        }else {


            //색
            if (i < 10) {
                wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
                wall_vArr[i + 1][j].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
            } else {
                wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
                wall_vArr[i - 1][j].setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
            }


            if (firstClicked_vi == -1 && firstClicked_vj == -1) {
                //버튼이 처음 눌렸다.

                firstClicked_vi = i;
                firstClicked_vj = j;

            } else if (firstClicked_vi == i && firstClicked_vj == j) {
                //버튼 같은게 두번눌렸다!!!!!

                if (i < 10) {

                    putWall(1, i, j, i + 1, j);
                    wall_vArr[i][j].setEnabled(false);
                    wall_vArr[i + 1][j].setEnabled(false);
                    mBoardState.setWall_v(i, j, true);
                    mBoardState.setWall_v(i + 1, j, true);

                } else {

                    putWall(1, i, j, i - 1, j);
                    wall_vArr[i][j].setEnabled(false);
                    wall_vArr[i - 1][j].setEnabled(false);
                    mBoardState.setWall_v(i, j, true);
                    mBoardState.setWall_v(i - 1, j, false);
                }

                onStageFinish(20);

            } else {

                firstClicked_hi = i;
                firstClicked_hj = j;
            }
        }
    }





    ///20151202 오랫만의 플젝 코딩 시작

    public void setUnclicked(Button[][] Arr) {//모든 버튼 안 눌리게 설정하는 배열

        int i, j;

        for (i = 0; i < Arr.length; i++) {
            for (j = 0; j < Arr[0].length; j++) {

                Arr[i][j].setEnabled(false);
            }
        }
    }


    public void setClicked(Button[][] Arr, int type) {//안눌린 버튼만 눌리게 설정하는 배열

        int i, j;

        int[][] stateArr;

        switch(type){

            case 1:
                stateArr = mBoardState.getBlock();
                break;
            case 2:
                stateArr = mBoardState.getWall_h();
                break;
            case 3:
                stateArr = mBoardState.getWall_v();
                break;
            default:
                stateArr = new int[Arr.length][Arr[0].length];
                Log.d("dSJW", "setClicked()에서 default에 걸림! _ type 잘못 전송!!!!");
                break;
        }


        for (i = 0; i < Arr.length; i++) {
            for (j = 0; j < Arr[0].length; j++) {

                if(type == 1){

                    // 0만 활성화 시키면 아이템은 어떻게 먹지?
                    if(stateArr[i][j] ==0) {
                        Arr[i][j].setEnabled(true);
                    }else{
                        Arr[i][j].setEnabled(false);
                    }
                }else if(type == 2){

                    if(stateArr[i][j] ==0) {
                        Arr[i][j].setEnabled(true);
                    }else{
                        Arr[i][j].setEnabled(false);
                    }
                }else if(type == 3){

                    if(stateArr[i][j] ==0) {
                        Arr[i][j].setEnabled(true);
                    }else{
                        Arr[i][j].setEnabled(false);
                    }
                }

            }
        }
    }






    //말움ㅈ직이는 부분분

    public int[][] checkRoute(int i, int j) {//사용자가 말을 움직이는 케이스 선택했을때 사용자의 현재 말위치에서 갈수있는 모든 경우의 수를 알려주는 함수


        // int[][] block = mBoardState.getBlock();//현재 게임의 말판의 아이템위치와 나와 상대 위치 저장하는 배열
        int[][] wall_h = mBoardState.getWall_h();//게임판에 놓인 가로 벽 상태 저장하는 배열
        int[][] wall_v = mBoardState.getWall_v();//게임판에 놓은 세로 벽 상태 저장하는 배열

        int[][] route = new int[5][2];//사용자의 상하좌우의 좌표를 저장하는 배열. 행이 5개인 이유는 대각선으로 이동하는 경우 최대 5가지 경우가 나오기 때문

        //사용자의 상하좌우의 좌표를 저장하는 배열. 사용자의 위칸으로 가는걸 첫번째로 잡아 시계방향으로 그 다은 것을 카운팅한다.
        route[0][0] = i - 1;//즉, 12시 방향이 (1번) 행번호 0
        route[0][1] = j;
        route[1][0] = i;// 3시 방향이 (2번) 행번호 1
        route[1][1] = j + 1;
        route[2][0] = i + 1;// 6시방향이 (3번) 행번호 2
        route[2][1] = j;
        route[3][0] = i;// 9시방향이 (4번) 행번호 3
        route[3][1] = j - 1;
        route[4][0] = -1;//이 경우는 아직 가능한지 모르므로 못가는 것을 표현하는 -1 저장
        route[4][1] = -1;


        for (int li = 0; li < 4; li++) {//상하좌우에 혹시 벽이 있어 못가는지 확인해주는 부분. 벽있으면 해당 번째에 -1 들어간다.

            switch (li) {

                case 0://12시 방향

                    //벽이 바로 있는 경우
                    if (wall_h[i - 1][j] == 1) {//바로위(12시방향)에 벽있니?

                        route[0][0] = -1;
                        route[0][1] = -1; //있으면 못 가므로 -1로 설정해준다.

                    } else {//벽이 바로 없는 경우에는  적이 위에 있을 경우 갈수있는 곳이 달라진다.


                        if (enemy_i == i - 1 && enemy_j == j) {//적이 있는 경우

                            if (wall_h[i - 2][j] == 0) {//적 바로위(12시방향)에 벽이 없는 경우

                                route[0][0] = i - 2;
                                route[0][1] = j;

                            } else {//적 바로위(12시방향)에 벽이 있는 경우(wall_h[i-2][j] == 1)

                                if (wall_v[i - 1][j - 1] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 왼쪽에 벽이 없는가?

                                    route[0][0] = i - 1;
                                    route[0][1] = j - 1;

                                } else {//벽이 있는 경우

                                    route[0][0] = -1;
                                    route[0][1] = -1;
                                }

                                if (wall_v[i - 1][j] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 오른쪽에 벽이 없는가?

                                    route[4][0] = i - 1;
                                    route[4][1] = j + 1;

                                } else {//벽이 있는 경우

                                    route[4][0] = -1;
                                    route[4][1] = -1;
                                }

                            }
                        }
                    }

                    break;


                case 1://3시 방향

                    //벽이 바로 있는 경우
                    if (wall_v[i][j] == 1) {//바로옆(3시방향)에 벽있니?

                        route[1][0] = -1;
                        route[1][1] = -1; //있으면 못 가므로 -1로 설정해준다.

                    } else { //벽이 바로 없는 경우 적이 옆에 있을 경우 갈수있는 곳이 달라진다.


                        //적이 바로 옆에 있는 경우
                        if (enemy_i == i && enemy_j == j + 1) {

                            if (wall_v[i][j + 1] == 0) {//적 바로옆(3시방향)에 벽이 없는 경우

                                route[1][0] = i;
                                route[1][1] = j + 2;

                            } else {//적 바로옆(3시방향)에 벽이 있는 경우(wall_v[i][j+1] == 1)

                                if (wall_h[i - 1][j + 1] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 왼쪽에 벽이 없는가?

                                    route[1][0] = i - 1;
                                    route[1][1] = j + 1;

                                } else {//벽이 있는 경우

                                    route[1][0] = -1;
                                    route[1][1] = -1;
                                }


                                if (wall_h[i][j + 1] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 오른쪽에 벽이 없는가?

                                    route[4][0] = i + 1;
                                    route[4][1] = j + 1;

                                } else {//벽이 있는 경우

                                    route[4][0] = -1;
                                    route[4][1] = -1;
                                }

                            }
                        }
                    }

                    break;


                case 2://6시 방향

                    //벽이 바로 있는 경우
                    if (wall_h[i][j] == 1) {//바로아래(6시방향)에 벽있니?
                        route[2][0] = -1;
                        route[2][1] = -1; //있으면 못 가므로 -1로 설정해준다.
                    } else {


                        //벽이 바로 없는 경우 적이 아래에 있을 경우 갈수있는 곳이 달라진다.
                        if (enemy_i == i + 1 && enemy_j == j) {

                            if (wall_h[i + 1][j] == 0) {//적 바로아래(6시방향)에 벽이 없는 경우

                                route[2][0] = i + 2;
                                route[2][1] = j;

                            } else {//적 바로아래(6시방향)에 벽이 있는 경우(wall_h[i+1][j] == 1)

                                if (wall_v[i + 1][j] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 왼쪽에 벽이 없는가?

                                    route[2][0] = i + 1;
                                    route[2][1] = j + 1;

                                } else {//벽이 있는 경우

                                    route[2][0] = -1;
                                    route[2][1] = -1;
                                }


                                if (wall_v[i - 1][j - 1] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 오른쪽에 벽이 없는가?

                                    route[2][0] = i - 1;
                                    route[2][1] = j - 1;

                                } else {//벽이 있는 경우

                                    route[2][0] = -1;
                                    route[2][1] = -1;
                                }

                            }
                        }
                    }

                    break;


                case 3://9시 방향

                    //벽이 바로 있는 경우
                    if (wall_v[i][j - 1] == 1) {//바로옆(9시방향)에 벽있니?

                        route[3][0] = -1;
                        route[3][1] = -1; //있으면 못 가므로 -1로 설정해준다.

                    } else {

                        //벽이 바로 없는 경우 적이 옆에 있을 경우 갈수있는 곳이 달라진다.
                        if (enemy_i == i && enemy_j == j - 1) {

                            if (wall_v[i][j - 2] == 0) {//적 바로옆(9시방향)에 벽이 없는 경우

                                route[3][0] = i;
                                route[3][1] = j - 2;

                            } else {//적 바로옆(9시방향)에 벽이 있는 경우(wall_v[i-2][j] == 1)

                                if (wall_h[i][j - 1] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 왼쪽에 벽이 없는가?

                                    route[3][0] = i - 1;
                                    route[3][1] = j - 1;

                                } else {//벽이 있는 경우

                                    route[3][0] = -1;
                                    route[3][1] = -1;
                                }


                                if (wall_h[i - 1][j - 1] == 0) {//나의 위치에서 상대방향으로 바라 볼때 상대 오른쪽에 벽이 없는가?

                                    route[4][0] = i - 1;
                                    route[4][1] = j - 1;

                                } else {//벽이 있는 경우

                                    route[4][0] = -1;
                                    route[4][1] = -1;
                                }

                            }
                        }
                    }

                    break;
            }
        }



        for(int li=0; li<5; li++){//앞에서 검토한 갈수있는 4(5)가지 갈수있는 길

            int a = route[li][0];
            int b = route[li][1];

            if(a!= -1 && b!= -1){

                blockArr[a][b].setBackgroundColor(getResources().getColor(R.color.can_go));
                // setclickable 이 아니라 setEnable 일텐데?
                blockArr[a][b].setClickable(true);

            }
        }

        return route;
    }


    public int eatItem(int a, int b){

        int item_id = -1;
        for(int i=0; i<3; i++){

            if(itemLocation[i][0] == a && itemLocation[i][1] == b){

                itemState[i] = 1;

                if(i==1){
                    item_id = 1;
                    mBoardState.setItemState(item_id);
                    blockArr[a][b].setBackgroundColor(getResources().getColor(R.color.block_default));
                    myitem01.setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
                    myitem01.setEnabled(false);
                }else if(i==2){
                    item_id = 2;
                    mBoardState.setItemState(item_id);
                    blockArr[a][b].setBackgroundColor(getResources().getColor(R.color.block_default));
                    numOfWall++;
                    mBoardState.increaseNumOfWall();

                }else{
                    item_id = 3;
                    mBoardState.setItemState(item_id);
                    blockArr[a][b].setBackgroundColor(getResources().getColor(R.color.block_default));
                    myitem03.setBackgroundColor(getResources().getColor(R.color.tempcolor_najungahjabakua));
                    myitem03.setEnabled(false);
                }
            }
        }

        return item_id;
    }


















    //여기선 부턴 벽 놓는 부분!

    public void wallBtnSetting(){

        setUnclicked(blockArr);//눌리지 않을 block 버튼 배열 비활성화

        setClicked(wall_hArr, 2);
        setClicked(wall_vArr, 3);
    }


    public void putWall(int type, int i1, int j1, int i2, int j2 ){

        int btnId = getResources().getIdentifier("myWall0"+numOfWall, "id", "com.givemeaplus");
        Button wallButton = (Button)findViewById(btnId);
        wallButton.setBackgroundColor(getResources().getColor(R.color.wall_gray));
        numOfWall--;



        if(type == 0){

            wall_hArr[i1][j1].setBackgroundColor(getResources().getColor(R.color.wallishere));
            wall_hArr[i2][j2].setBackgroundColor(getResources().getColor(R.color.wallishere));
        }else{

            wall_vArr[i1][j1].setBackgroundColor(getResources().getColor(R.color.wallishere));
            wall_vArr[i2][j2].setBackgroundColor(getResources().getColor(R.color.wallishere));
        }

    }

















    //여기서 부턴 아이템 쓰는 부분


    public void checkRoute_2(int i, int j){

        int[][] firstRoute;
        ArrayList<int[][]> secondRoute = null;

        firstRoute = checkRoute(i,j);

        for(int li =0; li<firstRoute.length; li++){

            secondRoute.add(checkRoute(firstRoute[li][0], firstRoute[li][1]));
        }

        for(int li=0; li<secondRoute.size(); li++) {
            for (int lj = 0; lj < secondRoute.get(0).length; lj++) {

                int a = secondRoute.get(li)[lj][0];
                int b = secondRoute.get(li)[lj][1];

                if (a != -1 && b != -1) {

                    blockArr[a][b].setBackgroundColor(getResources().getColor(R.color.can_go));
                    blockArr[a][b].setClickable(true);
                }
            }
        }
    }



    public void deleteWall(){

        int i,j;


        firstClicked_hi = -1;
        firstClicked_hj = -1;
        firstClicked_vi = -1;
        firstClicked_vj = -1;

        for(i=0; i<mBoardState.getWall_h().length; i++){
            for(j=0; j<mBoardState.getWall_h()[0].length; j++){

                if(mBoardState.getWall_h()[i][j] == 1){

                    wall_hArr[i][j].setEnabled(true);
                    delete_hi = i;
                    delete_hj = j;

                    wall_hArr[i][j].setOnClickListener(new deleteWall_hOnClickListener());
//                    wall_hArr[i][j].setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if(firstClicked_hi == -1 && firstClicked_hj == -1){
//
//                                wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
//                                firstClicked_hi = i;
//                                firstClicked_hj = j;
//                            }else{
//
//                                if(firstClicked_hi == i && firstClicked_hj == j){
//
//                                    wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.wall_blank));
//                                    mBoardState.deleteWall_h(i,j, true);
//
//                                }else{
//
//                                    wall_hArr[firstClicked_hi][firstClicked_hj].setBackgroundColor(getResources().getColor(R.color.wall_gray));
//                                    firstClicked_hi = i;
//                                    firstClicked_hj = j;
//                                    wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
//                                }
//                            }
//                        }
//                    });
                }
            }
        }

        for(i = 0; i<mBoardState.getWall_v().length; i++){
            for(j=0; j<mBoardState.getWall_v()[0].length; j++){

                if(mBoardState.getWall_v()[i][j] == 1){

                    wall_vArr[i][j].setEnabled(true);

                    delete_vi = i;
                    delete_vj = j;

                    wall_vArr[i][j].setOnClickListener(new deleteWall_vOnClickListener());

//                    wall_vArr[i][j].setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if (firstClicked_vi == -1 && firstClicked_vj == -1) {
//
//                                wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
//                                firstClicked_vi = i;
//                                firstClicked_vj = j;
//                            } else {
//
//                                if (firstClicked_vi == i && firstClicked_vj == j) {
//
//                                    wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.wall_blank));
//                                    mBoardState.deleteWall_v(i, j, true);
//
//                                } else {
//
//                                    wall_vArr[firstClicked_vi][firstClicked_vj].setBackgroundColor(getResources().getColor(R.color.wall_gray));
//                                    firstClicked_vi = i;
//                                    firstClicked_vj = j;
//                                    wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
//                                }
//                            }
//                        }
//                    });
                }
            }
        }
    }


    public class deleteWall_hOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            int i = delete_hi;
            int j = delete_hj;

            if(firstClicked_hi == -1 && firstClicked_hj == -1){

                wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
                firstClicked_hi = i;
                firstClicked_hj = j;
            }else{

                if(firstClicked_hi == i && firstClicked_hj == j){

                    wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.wall_blank));
                    mBoardState.deleteWall_h(i, j, true);


                    onStageFinish(30);

                } else {

                    wall_hArr[firstClicked_hi][firstClicked_hj].setBackgroundColor(getResources().getColor(R.color.wall_gray));
                    firstClicked_hi = i;
                    firstClicked_hj = j;
                    wall_hArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
                }
            }

        }
    }



    public class deleteWall_vOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            int i = delete_vi;
            int j = delete_vj;

            if(firstClicked_vi == -1 && firstClicked_vj == -1){

                wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
                firstClicked_vi = i;
                firstClicked_vj = j;
            }else{

                if(firstClicked_vi == i && firstClicked_vj == j){

                    wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.wall_blank));
                    mBoardState.deleteWall_v(i, j, true);


                    onStageFinish(31);


                }else{

                    wall_vArr[firstClicked_vi][firstClicked_vj].setBackgroundColor(getResources().getColor(R.color.wall_gray));
                    firstClicked_vi = i;
                    firstClicked_vj = j;
                    wall_vArr[i][j].setBackgroundColor(getResources().getColor(R.color.agro1));
                }
            }

        }
    }


}
