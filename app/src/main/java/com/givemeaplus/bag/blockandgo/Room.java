package com.givemeaplus.bag.blockandgo;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class Room {
    String num;
    String name;

    public Room(int x, String y){
        num = String.valueOf(x);
        name = y;
    }

    public String toString(){
        return num+" "+name;
    }
}
