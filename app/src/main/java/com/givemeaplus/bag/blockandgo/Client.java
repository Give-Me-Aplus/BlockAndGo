package com.givemeaplus.bag.blockandgo;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class Client {

    private static final String DEST_IP = "";
    private static int PORT_NUM = 9878;

    private static Client mClient = null;

    private static Socket connectionSocket = null;
    private static BufferedReader reader = null;
    private static BufferedWriter writer = null;
    private static Thread thread = null;

    public static Client getInstance(){
        if(mClient==null) mClient = new Client();
        return mClient;
    }

    private Client(){
        try{
            connectionSocket = new Socket(DEST_IP, PORT_NUM);

            reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public boolean sendName(String name){

        // 서버에 이름을 보내서 중복되지 않으면 보낸 이름을 되돌려받음

        String receiveMsg="";

        try {
            writer.write("Nickname#"+name+"\n");
            writer.flush();
            receiveMsg = reader.readLine().toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return receiveMsg.contains(name)? true:false;

    }
}
