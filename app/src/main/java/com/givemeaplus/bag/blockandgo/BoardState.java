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

            System.out.println(block[i][0] + "\t"+block[i][1] + "\t"+block[i][2] + "\t"+block[i][3] + "\t"+block[i][4] + "\t"+block[i][5] + "\t"+block[i][6]);
        }
    }


    public void showWall_h(){

        int i,j;
        Log.d("dSJW", "wall_h : ");

        for(i=0; i<10; i++) {

            Log.d("dSJW", "\t" + wall_h[i][0] + "\t" + wall_h[i][1] + "\t" + wall_h[i][2] + "\t" + wall_h[i][3] + "\t" + wall_h[i][4] + "\t" + wall_h[i][5] + "\t" + wall_h[i][6] + "\n");
        }
    }



    public void showWall_v(){

        int i,j;
        Log.d("dSJW", "wall_v : ");

        for(i=0; i<11; i++) {

            Log.d("dSJW", "\t"+wall_v[i][0] + "\t"+wall_v[i][1] + "\t"+wall_v[i][2] + "\t"+wall_v[i][3] + "\t"+wall_v[i][4] + "\t"+wall_v[i][5] + "\n");
        }
    }


    //wall로 경로를 막았을때 게임이 성립하는지 안하는지 체크하는 함수.
    //wall을 놓기전 놓아도되는곳인가 체크하는데 사용.
    //놓아도 괜찮으면 true 반환, 안되면 false 반환.
    //canGoToGoalPoint배열을 사용하는데 canGoToGoalPoint[i][j]이 0이면 block[i][j]에서 GOAL지점까지 이동불가이고
    //canGoToGoalPoint[i][j]이 1이면 block[i][j]에서 GOAL지점으로 이동할수있다.
    //현재 자신의 말의 위치가 1이면 성립한다!
    public boolean checkARoute(int x, int y){


        int[][] canGoToGoalPoint = new int[11][7];

        int i,j;//loop 변수

        for(j=0; j<7; j++){//도착해야할 GOAL지점 1로 설정

            canGoToGoalPoint[0][j]=1;
        }

        for(i=0; i<11; i++){

            for(j=0; j<7; j++){//wall_h을 보고 GOAL에 갈수있는지 갈수없는지 체크

                if(wall_h[i][j]==0 && canGoToGoalPoint[i-1][j]==1){//위에 wall_h이 없고 wall_h위의 칸에서 GOAL에 접근 가능하다면

                    canGoToGoalPoint[i][j]=1;
                }else {

                    canGoToGoalPoint[i][j]=0;
                }
            }


            int startPoint=0, endPoint=0, canGo=0;

            for(j=0; j<6; j++){//wall_v를 보고 GOAL에 갈수있는지 갈수없는지 체크

                if(wall_v[i][j]==0){

                    canGo+=canGoToGoalPoint[i][j];
                    endPoint++;
                } else{

                    if(canGo !=0){//같은 행 wall_v와 wall_v사이에 1이 하나라도 있으면

                        for(int k=startPoint; k<=endPoint; k++){//그 wall_v사이의 모든 칸에 1 넣는다.

                            canGoToGoalPoint[i][k]=1;
                        }
                    }

                    startPoint = endPoint+1;
                    endPoint = startPoint;
                }

                if(endPoint==6){//wall_v가 끝나지 않고 마지막 버튼 까지 이어져 있는 경우

                    canGo+=canGoToGoalPoint[i][6];

                    if(canGo !=0){

                        for(int k=startPoint; k<=endPoint; k++){

                            canGoToGoalPoint[i][k]=1;
                        }
                    }
                }
            }
        }


        int startPoint=0, endPoint=0, canGo=0;

        for(i=0; i<11; i++){

            if(wall_h[i][y] == 0 ){

                canGo+=canGoToGoalPoint[i][y];
                endPoint++;

            }else{

                if(canGo!=0){

                    for(int k=startPoint; k<=endPoint; k++){

                        canGoToGoalPoint[k][y] = 1;
                    }
                }

                startPoint = endPoint+1;
                endPoint = startPoint;
            }

            if(endPoint==11){

                canGo+=canGoToGoalPoint[11][y];

                for(int k=startPoint; k<=endPoint; k++){

                    canGoToGoalPoint[k][y] = 1;
                }

            }
        }


        return canGoToGoalPoint[x][y]==1 ?  true : false;

    }
}
