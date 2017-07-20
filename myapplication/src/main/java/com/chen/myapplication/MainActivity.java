package com.chen.myapplication;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText ID ;
    private EditText address ;
    private EditText elsetext ;
    private Button btnsend ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("chen","app222 OnCreat(),,,");
        ID = (EditText) findViewById( R.id.ID);
        address = (EditText) findViewById( R.id.address);
        elsetext = (EditText) findViewById( R.id.elsetext);
        btnsend = (Button) findViewById( R.id.btnsend);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagData tagData = new TagData(ID.getText().toString(),address.getText().toString(),elsetext.getText().toString());
                TagData tagData2 = new TagData("10000","16hse","asdjfgkadfa");
                sendDataToTag(tagData2);
                Log.d("chen","dianji btnsend,,,");
            }
        });

    }


    private void sendDataToTag(TagData tagData) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            try {
                ndef.connect();
                NdefRecord ndefRecord = NdefRecord.createMime("text/plain", objectToByte(tagData));
                //NdefRecord ndefRecord = NdefRecord.createMime("text/plain", tvData.getBytes() );
                NdefRecord[] records = {ndefRecord};
                NdefMessage ndefMessage = new NdefMessage(records);
                ndef.writeNdefMessage(ndefMessage);
                Toast toast = Toast.makeText(this, "Finish send ", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("chen"," sending,,,");
            } catch (IOException e1) {

                e1.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] objectToByte(java.lang.Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }


}
