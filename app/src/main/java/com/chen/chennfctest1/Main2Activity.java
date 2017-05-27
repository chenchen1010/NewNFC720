package com.chen.chennfctest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView ID ;
    private TextView address ;
    private TextView elsetext ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("chen","act22222,,,,");

        Intent intentGet = getIntent();
        ListData listDataGet = (ListData) intentGet.getSerializableExtra("ItemData");
        Log.d("chen","act22222,,,,get ItemData "+ listDataGet.getTagData().getId());
        ID = (TextView) findViewById( R.id.ID);
        ID.setText(listDataGet.getTagData().getId());
        address = (TextView) findViewById( R.id.address);
        address.setText(listDataGet.getTagData().getAddress());
        elsetext = (TextView) findViewById( R.id.elsetext);
        elsetext.setText(listDataGet.getTagData().getIDItself());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.finish();
        return super.onKeyDown(keyCode, event);

    }
}
