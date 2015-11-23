package com.givemeaplus.bag.blockandgo;


import android.util.Log;

/**
 * Created by Song  Ji won on 2015-11-22.
 */
public class BoardState {

    private static int[][] block;
    private static int[][] wall_h;
    private static int[][] wall_v;

    private static int location_redX;
    private static int location_redY;
    private static int location_blueX;
    private static int location_blueY;

    private static BoardState mBoardState = null;


    public static BoardState getInstance(){
        if(mBoardState==null) mBoardState = new BoardState();
        return mBoardState;
    }

    private BoardState(){
        // 0이면 아무것도 없는 상태, 1이면 red, 2이면 blue
        // set 함수에서 true면 red, false면 blue

        location_blueX = 3;
        location_blueY = 0;
        location_redX = 3;
        location_redY = 10;

        block = new int[11][7];
        wall_h = new int[10][7];
        wall_v = new int[11][6];
    }

    public int[][] getBlock(){
        return block;
    }


    public int[][] getWall_h(){
        return wall_h;
    }


    public int[][] getWall_v(){
        return wall_v;
    }



    /* set 함수에서는 행렬 위치와 true/false 입력을 통해
        red/blue 구별 가능하게 한번 해보자.*/

    public void setBlock(int i, int j, boolean type){
        if(type) {  // red 변경
            block[location_redY][location_redX] = 0;
            block[i][j] = 1;
            location_redY = i;
            location_redX = j;
        }
        else {  // blue 변경
            block[location_blueY][location_blueX] = 0;
            block[i][j] = 2;
            location_blueY = i;
            location_blueX = j;
        }

    }

    // wall 은 한칸 기분점 잡아서 그 벽의 오른쪽 혹은 아래 벽과 쌍으로 적용
    // 칸막이는 한번에 두칸씩 막으니깐!
    // 제일 끝 인덱스가 매개변수로 들어오지 않게 조심할것 *******중요****
    // 아니면 그냥 여기서 체크를 해주는 것도 좋을 것 같소이다(메인에서 하면 코드 길어지고 복잡해질듯)
    public void setWall_h(int i, int j, boolean type){
        if(type) {
            wall_h[i][j] = 1;
            wall_h[i][j+1] = 1;
        }
        else {
            wall_h[i][j] = 2;
            wall_h[i][j+1] = 2;
        }
    }


    public void setWall_v(int i, int j, boolean type){
        if(type) {
            wall_v[i][j] = 1;
            wall_v[i+1][j] = 1;
        }
        else {
            wall_v[i][j] = 2;
            wall_v[i+1][j] = 2;
        }
    }


    public void showBlock(){

        int i,j;
        Log.d("dSJW", "block : ");

        for(i=0; i<11; i++){

            Log.d("dSJW", "\t"+block[i][0] + "\t"+block[i][1] + "\t"+block[i][2] + "\t"+block[i][3] + "\t"+block[i][4] + "\t"+block[i][5] + "\t"+block[i][6] + "\n");
        }
    }


    public void showWall_h(){

        int i,j;
        Log.d("dSJW", "wall_h : ");

        for(i=0; i<10; i++) {

            Log.d("dSJW", "\t"+wall_h[i][0] + "\t"+wall_h[i][1] + "\t"+wall_h[i][2] + "\t"+wall_h[i][3] + "\t"+wall_h[i][4] + "\t"+wall_h[i][5] + "\t"+wall_h[i][6] + "\n");
        }
    }



    public void showWall_v(){

        int i,j;
        Log.d("dSJW", "wall_v : ");

        for(i=0; i<11; i++) {

            Log.d("dSJW", "\t"+wall_v[i][0] + "\t"+wall_v[i][1] + "\t"+wall_v[i][2] + "\t"+wall_v[i][3] + "\t"+wall_v[i][4] + "\t"+wall_v[i][5] + "\n");
        }
    }
}
