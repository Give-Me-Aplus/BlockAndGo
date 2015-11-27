package com.givemeaplus.bag.blockandgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Hye Jeong on 2015-11-25.
 */
public class BaseAdapter_Room extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    Client connection;
    ArrayList<Room> rooms;

    public BaseAdapter_Room(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        rooms = new ArrayList<Room>();

        //connection = Client.getInstance();

        for(int i=0; i<10; i++){
            Room r = new Room(i, "HJ "+i);
            System.out.println("HJ");
            rooms.add(r);
        }

        for(int i=0; i<10; i++){
            System.out.println(rooms.get(i).toString());
        }

    }

    public void add(){
        // room 추가, 추가해서 서버에 알리고 room # 받아옴
    }

    public void refresh(){
        // 서버로부터 모든 방을 받아오고 notifyDatasetChanged()
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

            itemLayout.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) itemLayout.getTag();
        }

        viewHolder.roomNum.setText(rooms.get(position).num);
        viewHolder.roomName.setText(rooms.get(position).name);

        return itemLayout;

    }
}
