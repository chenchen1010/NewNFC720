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

        TextView textViewNoteText = (TextView) view.findViewById(R.id.textViewNoteText);
        TextView textViewNoteComment = (TextView) view.findViewById(R.id.textViewNoteComment);



        textViewNoteText.setText(listData.getTagData().getId() +"   "+ listData.getTagData().getAddress() );
        textViewNoteComment.setText("Added at "+listData.getTime()+" "+listData.getDate());


        return view;
    }
}
