package com.givemeaplus.bag.blockandgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class BaseAdapter_Room extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;

    private ProgressDialog Loading;

    Client client;
    public static ArrayList<Room> rooms;

    public static boolean dataChanged = false;

    public BaseAdapter_Room(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        rooms = new ArrayList<Room>();

        client = Client.getInstance(mContext);

        Loading = new ProgressDialog(mContext);
        Loading.setCancelable(true);
        Loading.setCanceledOnTouchOutside(false);
        Loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Loading.setMessage("방 목록을 불러오는 중입니다...");

        refresh();

    }

    public static void refreshRoom(ArrayList<Room> data){
        rooms.clear();
        rooms.addAll(data);
    }

    public void refresh(){
        client.refreshRoom();

        Loading.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!dataChanged){}
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        Loading.dismiss();
                    }
                });
                dataChanged = false;
            }
        }).start();
//
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                Handler mHandler = new Handler(Looper.getMainLooper());
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        notifyDataSetChanged();
//                        Loading.dismiss();
//                    }
//                });
//            }
//        };
//        // 1.5초뒤 실행 (다이얼로그 띄워주면 좋겠다)
//        timer.schedule(task, 1500);
    }

    @Override
    public int getCount() {

        return rooms.size();
    }

    @Override
    public Room getItem(int position) {

        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemLayout = convertView;
        ViewHolder viewHolder = null;

        if (itemLayout == null) {

            itemLayout = mLayoutInflater.inflate(R.layout.adapter_room, null);

            viewHolder = new ViewHolder();
            viewHolder.roomName = (TextView) itemLayout.findViewById(R.id.roomName);
            viewHolder.roomNum = (TextView) itemLayout.findViewById(R.id.roomNum);
            viewHolder.playerNum = (TextView) itemLayout.findViewById(R.id.playerNum);

            itemLayout.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) itemLayout.getTag();
        }

        viewHolder.roomNum.setText(rooms.get(position).num);
        viewHolder.roomName.setText(rooms.get(position).name);
        viewHolder.playerNum.setText(rooms.get(position).numOfPlayer);

        return itemLayout;

    }
}
