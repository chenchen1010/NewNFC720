package com.chen.chennfctest1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.List;



/**
 * Created by chenc on 2017/4/9.
 */

public class TagDataAdapter extends ArrayAdapter<ListData> {

    private int resourceid;

    public TagDataAdapter(Context context, int resource, List<ListData> objects) {
        super(context, resource, objects);
        resourceid = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListData listData = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceid,null);

        TextView lv_id = (TextView) view.findViewById(R.id.lv_id);
        TextView lv_address = (TextView) view.findViewById(R.id.address);
        TextView lv_elseText = (TextView) view.findViewById(R.id.elseText);
        TextView lv_time = (TextView) view.findViewById(R.id.time);
        TextView lv_date = (TextView) view.findViewById(R.id.date);



        lv_id.setText(listData.getTagData().getId());
        lv_address.setText(listData.getTagData().getAddress());
        lv_elseText.setText(listData.getTagData().getIDItself());

        lv_time.setText(listData.getTime());
        lv_date.setText(listData.getDate());


        return view;
    }
}
