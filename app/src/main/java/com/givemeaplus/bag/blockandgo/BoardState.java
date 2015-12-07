package com.givemeaplus.bag.blockandgo;


import android.util.Log;

import java.util.Random;

/**
 * Created by Song  Ji won on 2015-11-22.
 */
public class BoardState {

    PlayerInformation mPlayerInformation;

    private static BoardState mBoardState = null;

    private static int[][] block;
    private static int[][] wall_h;
    private static int[][] wall_v;

    public static int my_i;
    public static int my_j;

    private static int enemy_i;
    private static int enemy_j;

    public static String changedWalltype = null;
    public static int changedWall_i;
    public static int changedWall_j;
    public static int my_numOfWall;
    public static int enemy_numOfWall;

    public static int[] itemState;


    public static BoardState getInstance() {
        if (mBoardState == null) mBoardState = new BoardState();
        return mBoardState;
    }

    private BoardState() {

        mPlayerInformation = PlayerInformation.getMyPlayer();

        block = new int[11][7];
        wall_h = new int[10][7];
        wall_v = new int[11][6];

        itemState = new int[3];

        my_i = 10;
        my_j = 3;
        enemy_i = 0;
        enemy_j = 3;

        changedWalltype = null;
        changedWall_i = -1;
        changedWall_j = -1;
        my_numOfWall = 8;
        enemy_numOfWall = 8;

    }

    public int[][] getBlock() {
        return block;
    }


    public int[][] getWall_h() {
        return wall_h;
    }


    public int[][] getWall_v() {
        return wall_v;
    }


    public void setBlock(int i, int j, boolean meOrEnemy) {//true면 me, false면 enemy
        // 상대방의 정보를 갱신하는 거라면 여기서 색깔 바꿔주면 되겠네

        if (meOrEnemy == true) {

            block[my_i][my_j] = 0;
            my_i = i;
            my_j = j;
            block[my_i][my_j] = mPlayerInformation.getType()==1? 1 : 2;

        } else {
            block[enemy_i][enemy_j] = 0;

            enemy_i = i;
            enemy_j = j;
            block[enemy_i][enemy_j] = mPlayerInformation.getType()==1? 2 : 1;
        }
    }

    public void setWall_h(int i, int j, boolean meOrEnemy) {

        changedWalltype = "h";
        changedWall_i = i;
        changedWall_j = j;

        if (meOrEnemy == true) {
            wall_h[i][j] = 1;
            my_numOfWall--;
        } else {
            wall_h[i][j] = 1;
            enemy_numOfWall--;
        }
    }


    public void deleteWall_h(int i, int j, boolean meOrEnemy) {

        changedWalltype = "h";
        changedWall_i = i;
        changedWall_j = j;

        wall_h[i][j] = 0;
    }


    public void setWall_v(int i, int j, boolean meOrEnemy) {

        changedWalltype = "v";
        changedWall_i = i;
        changedWall_j = j;

        if (meOrEnemy == true) {
            wall_v[i][j] = 1;
            my_numOfWall--;
        } else {
            wall_v[i][j] = 1;
            enemy_numOfWall--;
        }
    }


    public void deleteWall_v(int i, int j, boolean meOrEnemy) {

        changedWalltype = "v";
        changedWall_i = i;
        changedWall_j = j;

        wall_v[i][j] = 0;
    }

    public void increaseNumOfWall(){

        my_numOfWall++;
    }

    public void setEnemyWall(int x){
        enemy_numOfWall = x;
        // 여기에 색깔바꿔주기
    }

    public void showBlock() {

        int i, j;
        //       Log.d("dSJW", "block : ");

        for (i = 0; i < 11; i++) {

            System.out.println(block[i][0] + "\t" + block[i][1] + "\t" + block[i][2] + "\t" + block[i][3] + "\t" + block[i][4] + "\t" + block[i][5] + "\t" + block[i][6]);
        }
    }


    public void showWall_h() {

        int i, j;
        Log.d("dSJW", "wall_h : ");

        for (i = 0; i < 10; i++) {

            Log.d("dSJW", "\t" + wall_h[i][0] + "\t" + wall_h[i][1] + "\t" + wall_h[i][2] + "\t" + wall_h[i][3] + "\t" + wall_h[i][4] + "\t" + wall_h[i][5] + "\t" + wall_h[i][6] + "\n");
        }
    }


    public void showWall_v() {

        int i, j;
        Log.d("dSJW", "wall_v : ");

        for (i = 0; i < 11; i++) {

            Log.d("dSJW", "\t" + wall_v[i][0] + "\t" + wall_v[i][1] + "\t" + wall_v[i][2] + "\t" + wall_v[i][3] + "\t" + wall_v[i][4] + "\t" + wall_v[i][5] + "\n");
        }
    }


    //wall로 경로를 막았을때 게임이 성립하는지 안하는지 체크하는 함수.
    //wall을 놓기전 놓아도되는곳인가 체크하는데 사용.
    //놓아도 괜찮으면 true 반환, 안되면 false 반환.
    //canGoToGoalPoint배열을 사용하는데 canGoToGoalPoint[i][j]이 0이면 block[i][j]에서 GOAL지점까지 이동불가이고
    //canGoToGoalPoint[i][j]이 1이면 block[i][j]에서 GOAL지점으로 이동할수있다.
    //현재 자신의 말의 위치가 1이면 성립한다!
    public boolean checkARoute(int wall_i, int wall_j, int type) {

        int[][] canGoToGoalPoint = new int[11][7];

        int i, j;//loop 변수

        boolean tof = false;

        //새로 추가한 부분!!!!!! 여기 다시 한번 바바문제 샹기면
        if (type == 0) {

            wall_h[wall_i][wall_j] = 1;
        } else if (type == 1) {

            wall_v[wall_i][wall_j] = 1;
        }


        for (j = 0; j < 7; j++) {//도착해야할 GOAL지점 1로 설정

            canGoToGoalPoint[0][j] = 1;

        }

        for (i = 1; i < 11; i++) {

            for (j = 0; j < 7; j++) {//wall_h을 보고 GOAL에 갈수있는지 갈수없는지 체크

                if (wall_h[i][j] == 0 && canGoToGoalPoint[i - 1][j] == 1) {//위에 wall_h이 없고 wall_h위의 칸에서 GOAL에 접근 가능하다면

                    canGoToGoalPoint[i][j] = 1;
                } else {

                    canGoToGoalPoint[i][j] = 0;
                }
            }


            int startPoint = 0, endPoint = 0, canGo = 0;

            for (j = 0; j < 6; j++) {//wall_v를 보고 GOAL에 갈수있는지 갈수없는지 체크

                if (wall_v[i][j] == 0) {

                    canGo += canGoToGoalPoint[i][j];
                    endPoint++;
                } else {

                    if (canGo != 0) {//같은 행 wall_v와 wall_v사이에 1이 하나라도 있으면

                        for (int k = startPoint; k <= endPoint; k++) {//그 wall_v사이의 모든 칸에 1 넣는다.

                            canGoToGoalPoint[i][k] = 1;
                        }

                        //20141206 오늘 발견한 오류. 한번 끝내면 canGo 0이 되야한다. 추가함!
                        canGo = 0;
                    }

                    startPoint = endPoint + 1;
                    endPoint = startPoint;
                }

                if (endPoint == 6) {//wall_v가 끝나지 않고 마지막 버튼 까지 이어져 있는 경우

                    canGo += canGoToGoalPoint[i][6];

                    if (canGo != 0) {

                        for (int k = startPoint; k <= endPoint; k++) {

                            canGoToGoalPoint[i][k] = 1;
                        }
                    }
                }
            }
        }


        int startPoint = 0, endPoint = 0, canGo = 0;

        for (i = 0; i < 11; i++) {

            if (wall_h[i][my_j] == 0) {

                canGo += canGoToGoalPoint[i][my_j];
                endPoint++;

            } else {

                if (canGo != 0) {

                    for (int k = startPoint; k <= endPoint; k++) {

                        canGoToGoalPoint[k][my_j] = 1;
                    }
                }

                canGo = 0;
                startPoint = endPoint + 1;
                endPoint = startPoint;
            }

            if (endPoint == 11) {

                canGo += canGoToGoalPoint[11][my_j];

                if (canGo != 0) {

                    for (int k = startPoint; k <= endPoint; k++) {

                        canGoToGoalPoint[k][my_j] = 1;
                    }
                }
            }
        }


        if (type == 0) {

            wall_h[wall_i][wall_j] = 0;
        } else if (type == 1) {

            wall_v[wall_i][wall_j] = 0;
        }


        ///잘못하면 여기서 문제 생길수도 있을것같아!!!!!!!!! ㅠㅜ
        return canGoToGoalPoint[my_i][my_j] == 1 ? true : false;

    }


    //20151206 시붐 멘붕ㅇ이다
    //적의 루트 확인하는 함수
    public boolean checkARoute2(int wall_i, int wall_j, int type) {

        int[][] canGoToGoalPoint = new int[11][7];

        int i, j;//loop 변수

        boolean tof = false;

        //새로 추가한 부분!!!!!! 여기 다시 한번 바바문제 샹기면
        if (type == 0) {

            wall_h[wall_i][wall_j] = 1;
        } else if (type == 1) {

            wall_v[wall_i][wall_j] = 1;
        }


        for (j = 0; j < 7; j++) {//도착해야할 GOAL지점 1로 설정

            canGoToGoalPoint[10][j] = 1;

        }

        for (i = 9; i >= 0; i--) {

            for (j = 0; j < 7; j++) {//wall_h을 보고 GOAL에 갈수있는지 갈수없는지 체크

                if (wall_h[i][j] == 0 && canGoToGoalPoint[i + 1][j] == 1) {//위에 wall_h이 없고 wall_h위의 칸에서 GOAL에 접근 가능하다면

                    canGoToGoalPoint[i][j] = 1;
                } else {

                    canGoToGoalPoint[i][j] = 0;
                }
            }


            int startPoint = 0, endPoint = 0, canGo = 0;

            for (j = 0; j < 6; j++) {//wall_v를 보고 GOAL에 갈수있는지 갈수없는지 체크

                if (wall_v[i][j] == 0) {

                    canGo += canGoToGoalPoint[i][j];
                    endPoint++;
                } else {

                    if (canGo != 0) {//같은 행 wall_v와 wall_v사이에 1이 하나라도 있으면

                        for (int k = startPoint; k <= endPoint; k++) {//그 wall_v사이의 모든 칸에 1 넣는다.

                            canGoToGoalPoint[i][k] = 1;
                        }

                        //20141206 오늘 발견한 오류. 한번 끝내면 canGo 0이 되야한다. 추가함!
                        canGo = 0;
                    }

                    startPoint = endPoint + 1;
                    endPoint = startPoint;
                }

                if (endPoint == 6) {//wall_v가 끝나지 않고 마지막 버튼 까지 이어져 있는 경우

                    canGo += canGoToGoalPoint[i][6];

                    if (canGo != 0) {

                        for (int k = startPoint; k <= endPoint; k++) {

                            canGoToGoalPoint[i][k] = 1;
                        }
                    }
                }
            }
        }


        int startPoint = 0, endPoint = 0, canGo = 0;

        for (i = 10; i > 0; i--) {

            if (wall_h[i - 1][enemy_j] == 0) {

                canGo += canGoToGoalPoint[i][enemy_j];
                endPoint--;

            } else {

                if (canGo != 0) {

                    for (int k = startPoint; k >= endPoint; k--) {

                        canGoToGoalPoint[k][enemy_j] = 1;
                    }
                }

                startPoint = endPoint - 11;
                endPoint = startPoint;

                canGo = 0;
            }

            if (endPoint == 0) {

                canGo += canGoToGoalPoint[0][enemy_j];

                for (int k = startPoint; k <= endPoint; k--) {


                    if (canGo != 0) {

                        canGoToGoalPoint[k][enemy_j] = 1;
                    }
                }
            }
        }


        if (type == 0) {

            wall_h[wall_i][wall_j] = 0;
        } else if (type == 1) {

            wall_v[wall_i][wall_j] = 0;
        }


        ///잘못하면 여기서 문제 생길수도 있을것같아!!!!!!!!! ㅠㅜ
        return canGoToGoalPoint[enemy_i][enemy_j] == 1 ? true : false;

    }


    //20151206 저녁에 추가한 코드

    public void setItemState(int item_id) {

        itemState[item_id] = 1;
    }
}
