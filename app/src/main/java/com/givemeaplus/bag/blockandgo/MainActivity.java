package com.givemeaplus.bag.blockandgo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.logging.Handler;

public class MainActivity extends Activity implements View.OnClickListener {

    LinearLayout parentLayout;
    View[] childViews;

    Thread timer;


    BoardState mBoardState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = (LinearLayout)findViewById(R.id.parent_layout);
        setOnClickListener(parentLayout);

        mBoardState = BoardState.getInstance();

        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                // 1초 지날때마다 핸들러로 타이머 텍스트뷰 숫자 변경
            }
        });

    }


    private void setOnClickListener(ViewGroup viewGroup){

        childViews = getChildViews(viewGroup);

        for(int i=0; i<childViews.length; i++){

            if(childViews[i].getTag() != null) {
                String tag = childViews[i].getTag().toString();

                if (tag.contains("block") || tag.contains("wall") || tag.contains("btn"))
                    childViews[i].setOnClickListener(this);
            }
            if(childViews[i] instanceof Button)
                childViews[i].setOnClickListener(this);
            else if(childViews[i] instanceof ViewGroup)
                setOnClickListener((ViewGroup) childViews[i]);
        }
    }

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

        System.out.println("wall_h "+i+" "+j);
        mBoardState.setWall_h(i, j, true);
    }

    public void onClickWall_v(View v){
        String id_index = v.getTag().toString();
        int index;

        id_index = id_index.replace("wall_v", "");
        index = Integer.parseInt(id_index);
        int i = index/10;
        int j = index%10;

        System.out.println("wall_v "+i+" "+j);
        mBoardState.setWall_v(i, j, true);
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
