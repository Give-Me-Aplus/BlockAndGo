package com.givemeaplus.bag.blockandgo;

/**
 * Created by Hye Jeong on 2015-11-24.
 */
public class PlayerInformation {

    private String userName;

    private int numWall;
    private int numItem;

    private int x, y;

    // 1이면 red, 2면 blue
    private int type;

    public PlayerInformation(String name, int type){
        userName = name;
        this.type = type;
        numWall = 8;
        numItem = 0;

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
