package com.chen.chennfctest1;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main3Activity extends AppCompatActivity {

    private EditText ID ;
    private EditText address ;
    private EditText elsetext ;
    private Button btnsend ;
    private ChenNfcHelper chenNfcHelper;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        activity = this;

        Log.d("chen","act3 send activity(),,,");
        ID = (EditText) findViewById( R.id.ID);
        address = (EditText) findViewById( R.id.address);
        elsetext = (EditText) findViewById( R.id.elsetext);
        btnsend = (Button) findViewById( R.id.btnsend);
        chenNfcHelper = new ChenNfcHelper();

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagData tagData = new TagData(ID.getText().toString(),address.getText().toString(),elsetext.getText().toString());
                TagData tagData2 = new TagData("10000","16hse","asdjfgkadfa");
                Log.d("chen","dianji,,,");

                chenNfcHelper.sendDataToTag(tagData2,activity);
            }
        });
    }

}
