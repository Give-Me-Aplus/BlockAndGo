package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends Activity implements View.OnClickListener {


    LinearLayout parentLayout;
    //View[] childViews;

    BoardState mBoardState;

    Button[][] blockArr = new Button[11][7];
    Button[][] wall_hArr = new Button[10][7];
    Button[][] wall_vArr = new Button[11][6];


//////////////Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = (LinearLayout)findViewById(R.id.parent_layout);
/////////////////        setOnClickListener(parentLayout);
        makeBtnArr(parentLayout);

        mBoardState = BoardState.getInstance();

        mBoardState.showBlock();

        for(int i=0; i<11; i++){

            for(int j=0; j<7; j++){

                blockArr[i][j].setEnabled(false);
            }
        }


    }

    private void makeBtnArr(ViewGroup viewGroup){

        View[] childViews = getChildViews(viewGroup);

        for(int i=0;i<childViews.length; i++){

            if(childViews[i] instanceof LinearLayout ){

                makeBtnArr((ViewGroup)childViews[i]);

            }else if(childViews[i] instanceof Button){

                if(childViews[i].getTag() != null){

                    String tag = childViews[i].getTag().toString().trim();

                    int ii,ij;

                    if(tag.contains("block")){

                        //Log.d("dSJW", "\t\t\t tag  = "+tag);
                        tag = tag.replace("block", "");

                        int index = Integer.parseInt(tag);

                        ii = index/10;
                        ij = index%10;

                        blockArr[ii][ij] = (Button)childViews[i];

                    }else if(tag.contains("wall_h")){

                        //Log.d("dSJW", "\t\t\t tag  = "+tag);

                        tag = tag.replace("wall_h", "");
                        int index = Integer.parseInt(tag);

                        ii = index/10;
                        ij = index%10;

                        wall_hArr[ii][ij] = (Button)childViews[i];

                    }else if(tag.contains("wall_v")){

                        //Log.d("dSJW", "\t\t\t tag  = "+tag);
                        tag = tag.replace("wall_v", "");
                        int index = Integer.parseInt(tag);

                        ii = index/10;
                        ij = index%10;

                        wall_vArr[ii][ij] = (Button)childViews[i];

                    }
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
