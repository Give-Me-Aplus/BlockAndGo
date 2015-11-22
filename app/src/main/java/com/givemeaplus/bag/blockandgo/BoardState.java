package com.givemeaplus.bag.blockandgo;


import android.util.Log;

/**
 * Created by Song  Ji won on 2015-11-22.
 */
public class BoardState {

    private static int[][] block;
    private static int[][] wall_h;
    private static int[][] wall_v;


    public BoardState(){

        block = new int[11][7];
        wall_h = new int[10][7];
        wall_v = new int[11][6];
    }

    public static int[][] getBlock(){

//        if(block == null){
//            block = new int[11][7];
//        }

        return block;
    }


    public static int[][] getWall_h(){

//        if(wall_h == null){
//            wall_h = new int[10][7];
//        }

        return wall_h;
    }


    public static int[][] getWall_v(){

//        if(wall_v == null){
//            wall_v = new int[11][6];
//        }

        return wall_v;
    }


    public static void setBlock(int i, int j){

//        getBlock();//혹시 block배열이 없으면 만들어준다.

        block[i][j]+=1;
    }


    public static void setWall_h(int i, int j){

//        getWall_h();//혹시 wall_h배열이 없으면 만들어준다.

        wall_h[i][j]=1;
    }


    public static void setWall_v(int i, int j){

//        getWall_v();//혹시 wall_v배열이 없으면 만들어준다.

        wall_v[i][j]=1;
    }


    public static void showBlock(){

        int i,j;
        Log.d("dSJW", "block : ");

        for(i=0; i<11; i++){

            Log.d("dSJW", "\t"+block[i][0] + "\t"+block[i][1] + "\t"+block[i][2] + "\t"+block[i][3] + "\t"+block[i][4] + "\t"+block[i][5] + "\t"+block[i][6] + "\n");
        }
    }


    public static void showWall_h(){

        int i,j;
        Log.d("dSJW", "wall_h : ");

        for(i=0; i<10; i++) {

            Log.d("dSJW", "\t"+wall_h[i][0] + "\t"+wall_h[i][1] + "\t"+wall_h[i][2] + "\t"+wall_h[i][3] + "\t"+wall_h[i][4] + "\t"+wall_h[i][5] + "\t"+wall_h[i][6] + "\n");
        }
    }



    public static void showWall_v(){

        int i,j;
        Log.d("dSJW", "wall_v : ");

        for(i=0; i<11; i++) {

            Log.d("dSJW", "\t"+wall_v[i][0] + "\t"+wall_v[i][1] + "\t"+wall_v[i][2] + "\t"+wall_v[i][3] + "\t"+wall_v[i][4] + "\t"+wall_v[i][5] + "\n");
        }
    }
}
