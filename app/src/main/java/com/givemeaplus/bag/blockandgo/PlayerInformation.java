package com.givemeaplus.bag.blockandgo;

/**
 * Created by Hye Jeong on 2015-11-24.
 */
public class PlayerInformation {

    private static PlayerInformation myPlayer = null;

    private static String userName;

    private static int numWall;
    private static int numItem;

    private static int x, y;

    // 1이면 red, 2면 blue
    private int type;


    public static PlayerInformation getMyPlayer(){
        if(myPlayer==null) myPlayer = new PlayerInformation("");
        return myPlayer;
    }

    private PlayerInformation(String name){
        userName = name;
        numWall = 8;
        numItem = 0;

        x=3; y=10;  // 시작위치
    }

    public void setName(String name){
        userName = name;
    }

    public String getName(){
        return userName;
    }

    public int getType(){
        return type;
    }

    public void setType(int a){
        type = a;
    }

    public void decreaseWall(){

        numWall--;
    }

    public void decreaseItem(){

        numItem--;
    }

    public void increaseItem(){

        numItem++;
    }

}
