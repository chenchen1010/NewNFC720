package com.chen.chennfctest1;


import java.io.Serializable;

/**
 * Created by chenc on 2017/4/3.
 */

public class ListData implements Serializable {
    TagData tagData;
    String date;
    String time;

    public ListData(TagData tagData, String date, String time) {
        this.tagData = tagData;
        this.date = date;
        this.time = time;
    }

    public TagData getTagData() {
        return tagData;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
