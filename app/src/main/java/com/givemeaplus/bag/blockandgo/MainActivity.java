package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements View.OnClickListener {


    LinearLayout parentLayout;
    View[] childViews;

    BoardState mBoardState;

    String[][] blockArr = new String[11][7];
    String[][] wall_hArr = new String[10][7];
    String[][] wall_vArr = new String[11][6];



    //Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = (LinearLayout)findViewById(R.id.parent_layout);
//        setOnClickListener(parentLayout);
        makeBtnArr(parentLayout);

        mBoardState = BoardState.getInstance();

        mBoardState.showBlock();
    }

    private void makeBtnArr(ViewGroup viewGroup){//block과 wall_h

        childViews = getChildViews(viewGroup);

        for(int i=0; i<childViews.length; i++){

            if(childViews[i].getTag() != null){

                String tag = childViews[i].getTag().toString().trim();
                String tagTemp = tag;
                int ii,ij;

                if(tagTemp.contains("block")){

                    tagTemp.replace("block", "");
                    int index = Integer.parseInt(tagTemp);

                    ii = index/10;
                    ij = index%10;

                    blockArr[ii][ij] = tag;
                }else if(tagTemp.contains("wall_h")){

                    tagTemp.replace("wall_h", "");
                    int index = Integer.parseInt(tagTemp);

                    ii = index/10;
                    ij = index%10;

                    wall_hArr[ii][ij] = tag;
                }else if(tagTemp.contains("wall_v")){

                    tagTemp.replace("wall_v", "");
                    int index = Integer.parseInt(tagTemp);

                    ii = index/10;
                    ij = index%10;

                    wall_vArr[ii][ij] = tag;
                }
            }
        }
    }

//    private void setOnClickListener(ViewGroup viewGroup){
//
//        childViews = getChildViews(viewGroup);
//
//        for(int i=0; i<childViews.length; i++){
//
//            if(childViews[i].getTag() != null) {
//                String tag = childViews[i].getTag().toString();
//
//                if (tag.contains("block") || tag.contains("wall") || tag.contains("btn"))
//                    childViews[i].setOnClickListener(this);
//            }
////            if(childViews[i] instanceof Button)
////                childViews[i].setOnClickListener(this);
////            else if(childViews[i] instanceof ViewGroup)
////                setOnClickListener((ViewGroup) childViews[i]);
//        }
//    }

    public void onClickBlock(View v){

        String id_index = v.getTag().toString();
        int index;

        id_index = id_index.replace("block","");
        index = Integer.parseInt(id_index);
        int i = index/10;
        int j = index%10;

        System.out.println(i+" "+j);

        mBoardState.setBlock(i, j, true);
    }

    public void onClickWall_h(View v){
        String id_index = v.getTag().toString();
        int index;

        id_index = id_index.replace("wall_h","");
        index = Integer.parseInt(id_index);
        int i = index/10;
        int j = index%10;

        mBoardState.setWall_h(i, j, true);
    }


    private View[] getChildViews(ViewGroup viewGroup){

        int childCnt = viewGroup.getChildCount();
        final View[] childViews = new View[childCnt];

        for(int i=0; i<childCnt; i++){

            childViews[i] = viewGroup.getChildAt(i);
        }

        return childViews;
    }

    @Override
    public void onClick(View v) {

        String id_index;
        int index;

        if(v.getTag() != null) {
            id_index = v.getTag().toString();
            //Log.d("dSJW", id_index);

            if(id_index.contains("block")){

                index = Integer.parseInt(id_index);
                int i = index/10;
                int j = index%10;

                mBoardState.setBlock(i, j, true);
            }
            else if(id_index.contains("wall_h")){

                index = Integer.parseInt(id_index);
                int i = index/10;
                int j = index%10;

                mBoardState.setWall_h(i, j, true);
            }
            else if(id_index.contains("wall_v")){

                index = Integer.parseInt(id_index);
                int i = index/10;
                int j = index%10;

                mBoardState.setWall_v(i, j, true);
            }
        }

        mBoardState.showBlock();

    }



}
