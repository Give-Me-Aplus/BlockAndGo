package com.givemeaplus.bag.blockandgo;

import android.widget.Toast;

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
    private static InputStreamReader reader = null;
    private static OutputStreamWriter writer = null;
    private static Thread thread = null;

    public Client getInstance(){
        if(mClient==null) mClient = new Client();
        return mClient;
    }

    private Client(){
        try{
            connectionSocket = new Socket(DEST_IP, PORT_NUM);
            reader = new InputStreamReader(connectionSocket.getInputStream());
            writer = new OutputStreamWriter(connectionSocket.getOutputStream());
        }catch(Exception e){}
    }


    public void send(PlayerInformation player){

    }
}
