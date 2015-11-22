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

    //Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = (LinearLayout)findViewById(R.id.parent_layout);
        setOnClickListener(parentLayout);

        //singleton = Singleton.getInstance();


    }


    private void setOnClickListener(ViewGroup viewGroup){

        View[] childViews = getChildViews(viewGroup);

        for(int i=0; i<childViews.length; i++){

            if(childViews[i] instanceof Button){

                childViews[i].setOnClickListener(this);
            } else if(childViews[i] instanceof ViewGroup){

                setOnClickListener((ViewGroup) childViews[i]);
            }
        }
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

        if(v.getTag() != null) {
            id_index = v.getTag().toString();
            //Log.d("dSJW", id_index);

            if(id_index.contains("block")){

                id_index = id_index.replace("block", "");
                int i = Integer.parseInt(id_index.subSequence(0,1).toString());
                int j = Integer.parseInt(id_index.subSequence(1,2).toString());
                Log.d("dSJW", id_index);
                //Singleton.setBlock(i, j);
                //Singleton.showBlock();
            }
            else if(id_index.contains("wall_h")){

                id_index = id_index.replace("wall_h", "");
                int i = Integer.parseInt(id_index.subSequence(0,1).toString());
                int j = Integer.parseInt(id_index.subSequence(1,2).toString());

                BoardState.setWall_h(i, j);
                BoardState.showWall_h();
            }
            else if(id_index.contains("wall_v")){

                id_index = id_index.replace("wall_v", "");
                int i = Integer.parseInt(id_index.subSequence(0,1).toString());
                int j = Integer.parseInt(id_index.subSequence(1,2).toString());

                BoardState.setWall_v(i, j);
                BoardState.showWall_v();
            }
        }

    }



}
