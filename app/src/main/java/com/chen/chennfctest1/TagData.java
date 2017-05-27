package com.chen.chennfctest1;

import java.io.Serializable;

/**
 * Created by chenc on 2017/4/3.
 */

public class TagData implements Serializable {

    public TagData(String IDItself, String address, String id) {
        this.IDItself = IDItself;
        this.address = address;
        this.id = id;
    }

    String IDItself;
    String address;
    String id;


    public String getIDItself() {
        return IDItself;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }
}
