package com.givemeaplus.bag.blockandgo;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class Room {
    String num;
    String name;

    String numOfPlayer;

    public Room(String x, String y, String p){
        num = x;
        name = y;
        numOfPlayer = p;
    }

    public String toString(){
        return "Room "+num+"    "+name;
    }
}
