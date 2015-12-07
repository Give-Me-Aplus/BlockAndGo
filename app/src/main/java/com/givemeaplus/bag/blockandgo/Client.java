package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class Client implements Runnable{

    private static final String DEST_IP = "114.71.42.73";
    private static int PORT_NUM = 9878;

    private static Client mClient = null;

    private static Context mContext = null;

    private static Socket connectionSocket = null;
    private static BufferedReader reader = null;
    private static BufferedWriter writer = null;
    private static Thread thread = null;

    private static StringTokenizer token = null;

    private static boolean connection = false;
    private PlayerInformation myPlayer = null;
    private BoardState boardState = null;

    private ArrayList<Room> refresh = null;

    //private static ProgressDialog Loading = null;

    public static Client getInstance(Context context){

        mContext = context;

        if(mClient==null) mClient = new Client();
        return mClient;
    }

    private Client(){

        myPlayer = PlayerInformation.getMyPlayer();
        boardState = BoardState.getInstance();

        connection = false;

        thread = new Thread(this);
        thread.start();
    }

    public void enterGame(){
        try {
            writer.write("Game#enter\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void characterMove(){
        try {
            writer.write("Game#endTurn#move#"+boardState.my_i+"#"+boardState.my_j+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putWall(){

        try {
            writer.write("Game#endTurn#put#"+boardState.changedWalltype+"#"+
                            boardState.changedWall_i+"#"+boardState.changedWall_j+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteWall(){

        try {
            writer.write("Game#endTurn#delete#"+boardState.changedWalltype+"#"+
                         boardState.changedWall_i+"#"+boardState.changedWall_j+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeWallNum(){
        try {
            writer.write("Game#endTurn#wallChanged#"+boardState.my_numOfWall+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTimeOut(){
        try {
            writer.write("Game#exit\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendName(String name){

        try {
            writer.write("Nickname#"+name+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshRoom(){
        try {
            writer.write("Room#refresh\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeRoom(String name){
        try {
            writer.write("Room#make#"+name+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestRoom(String num){

        try {
            writer.write("Room#request#"+num+"\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitRoom(){

        try {
            writer.write("Room#exit\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendReady(){

        try {

            writer.write("Room#ready\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        Handler mHandler = new Handler(Looper.getMainLooper());

        try {
            connectionSocket = new Socket(DEST_IP, PORT_NUM);

            connectionSocket.setSoTimeout(60000);  // 60 second time limit for reading

            reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream(), "euc-kr"));
            writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

            connection = true;

            //sendName(myPlayer.getName());

        }
        catch(IOException e){
            e.printStackTrace();
        }

        while(connection){

            try {
                String msg = reader.readLine().toString().trim();

                token = new StringTokenizer(msg, "#");

                String command = token.nextToken();

                if(command.contains("Nickname")){
                    System.out.println("nickname success "+token.nextToken());

                }
                else if(command.contains("Room")){

                    String nextCommand = token.nextToken();

                    if(nextCommand.compareTo("refresh_start")==0){
                        refresh = new ArrayList<Room>();
                        System.out.println("refresh_start");
                    }
                    else if(nextCommand.compareTo("refresh")==0){
                        while(token.hasMoreTokens()){
                            refresh.add(new Room(token.nextToken(), token.nextToken(), token.nextToken()));
                        }
                    }
                    else if(nextCommand.compareTo("refresh_end")==0){
                        System.out.println("refresh_end");
                        BaseAdapter_Room.refreshRoom(refresh);
                        BaseAdapter_Room.dataChanged = true;
                    }
                    else if(nextCommand.compareTo("make")==0){

                        final String num = token.nextToken();
                        final String name = token.nextToken();

                        WaitingRoomActivity.roomNum = num;

                        // waitingRoom popup에 방 이름 바꿔주기
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView title = WaitingRoomActivity.roomInfo;
                                title.setText("Room " + num + "   " + name);
                            }
                        });
                    }
                    else if(nextCommand.compareTo("response")==0){
                        final String tmp = token.nextToken();

                        if(tmp.contains("NOT_FOUND")||tmp.contains("FULL")){

                            WaitingRoomActivity.isRoomError = true;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "방에 입장할 수 없습니다!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{

                            final String red = token.nextToken();
                            final String blue = token.nextToken();

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                TextView redName = WaitingRoomActivity.redName;
                                TextView blueName = WaitingRoomActivity.blueName;

                                Button redReady = WaitingRoomActivity.ready_red;
                                Button blueReady = WaitingRoomActivity.ready_blue;

                                redName.setText(red);
                                blueName.setText(blue);

                                if (myPlayer.getName().compareTo(red) == 0) {
                                    redReady.setEnabled(true);
                                    blueReady.setEnabled(false);
                                    myPlayer.setEnemyName(blue);
                                    myPlayer.setType(1);
                                } else {
                                    blueReady.setEnabled(true);
                                    redReady.setEnabled(false);
                                    myPlayer.setEnemyName(red);
                                     myPlayer.setType(2);
                                }
                                }
                            });

                            WaitingRoomActivity.isRoomLoaded = true;

                        }
                    }
                    else if(nextCommand.compareTo("ready")==0){

                        final String red_ready = token.nextToken();
                        final String blue_ready = token.nextToken();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                boolean redReady = (red_ready.compareTo("T")==0)? true : false;
                                boolean blueReady = (blue_ready.compareTo("T")==0)? true : false;

                                Button btn_redReady = WaitingRoomActivity.ready_red;
                                Button btn_blueReady = WaitingRoomActivity.ready_blue;

                                if(redReady) btn_redReady.setBackgroundResource(R.drawable.btn_ready_red);
                                else btn_redReady.setBackgroundResource(R.drawable.btn_not_ready);

                                if(blueReady) btn_blueReady.setBackgroundResource(R.drawable.btn_ready_blue);
                                else btn_blueReady.setBackgroundResource(R.drawable.btn_not_ready);
                            }
                        });
                    }
                    else if(nextCommand.compareTo("gameStart")==0){

                        WaitingRoomActivity.isGameStart = true;

                        // 게임시작
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "잠시 후 게임이 시작됩니다!", Toast.LENGTH_SHORT).show();

                                TextView redName = WaitingRoomActivity.redName;
                                TextView blueName = WaitingRoomActivity.blueName;

                                Button btn_redReady = WaitingRoomActivity.ready_red;
                                Button btn_blueReady = WaitingRoomActivity.ready_blue;
                                Button close = WaitingRoomActivity.close;

                                btn_redReady.setEnabled(false);
                                btn_blueReady.setEnabled(false);
                                close.setEnabled(false);

                                if(myPlayer.getType()==1)
                                    myPlayer.setEnemyName(blueName.getText().toString());
                                else
                                    myPlayer.setEnemyName(redName.getText().toString());

                            }
                        });

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
                                try {
                                    pendingIntent.send(mContext, 1, intent);
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 3000);
                    }

                }
                else if(command.contains("Game")){

                    String nextCommand = token.nextToken();

                    if(nextCommand.compareTo("turn")==0){
                        String turn = token.nextToken();

                        if(turn.compareTo("RED")==0 && myPlayer.getType()==1) {
                            MainActivity.isMyTurn = true;
                        }
                        else if(turn.compareTo("BLUE")==0 && myPlayer.getType()==2) {
                            MainActivity.isMyTurn = true;
                        }
                        else {
                            MainActivity.isMyTurn = false;
                        }

                        MainActivity.startCountDown = true;
                    }
                    else if(nextCommand.compareTo("move")==0){
                        int i = Integer.parseInt(token.nextToken());
                        int j = Integer.parseInt(token.nextToken());

                        boardState.setBlock(i, j, false);

                    }
                    else if(nextCommand.compareTo("put")==0){

                        String wallType = token.nextToken();

                        int i = Integer.parseInt(token.nextToken());
                        int j = Integer.parseInt(token.nextToken());

                        if(wallType.compareTo("h")==0)
                            boardState.setWall_h(i, j, false);
                        else
                            boardState.setWall_v(i, j, false);

                    }
                    else if(nextCommand.compareTo("delete")==0){

                        String wallType = token.nextToken();

                        int i = Integer.parseInt(token.nextToken());
                        int j = Integer.parseInt(token.nextToken());

                        if(wallType.compareTo("h")==0)
                            boardState.deleteWall_h(i, j, false);
                        else
                            boardState.deleteWall_v(i, j, false);

                    }
                    else if(nextCommand.compareTo("wallChange")==0){

                        int i = Integer.parseInt(token.nextToken());

                        boardState.setEnemyWall(i);

                    }
                    else if(nextCommand.compareTo("exitEnemy")==0){
                        //win dialog 띄우기
                    }

                }
                else if(command.contains("Connection")){

                    String nextCommand = token.nextToken();

                    if(nextCommand.compareTo("OK")==0) {
                        System.out.println("connection success");
                    }
                    else if(nextCommand.compareTo("well")==0){
                        System.out.println("connecting well");
                    }
                }



            } catch (Exception e) {
                // 이거 서버와의 연결이 끊어졌습니다. 재 접속 합니다.
                // 다이얼로그 띄우고 아예 어플리케이션 재시작!
                e.printStackTrace();

                //mClient = new Client();

                break;
            }

        }

    }
/*
    public void reConnect(){
        mClient = null;

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                Loading = new ProgressDialog(mContext);
                Loading.setCancelable(true);
                Loading.setCanceledOnTouchOutside(false);
                Loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                Loading.setMessage("서버와 연결 중입니다...");

                Loading.show();
                Loading.dismiss();

                mClient = new Client();

            }
        });
    }
*/
    public static boolean isConnected(){
        return connection;
    }

    public void missingConnection(){
        try {
            if(connectionSocket != null) {
                connection = false;

                writer.write("Connection#end\n");
                writer.flush();

                connectionSocket.close();
                System.out.println("socket close");

                mClient = null;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mClient = null;


    }
}
