package com.chen.chennfctest1;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String readResult;
    private Button btn_send;
    private EditText et1;
    private TextView tv;
    private TextView tv2;
    private String tvData;
    private TagData tagData;
    private TagData tagDataDetect;
    static private ArrayList <ListData> arrayList;
    static private TagDataAdapter arrayAdapter ;
    static private ListView listView;
    private ListData listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //先检测手机是否支持
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast toast = Toast.makeText(this, "此手机型号不支持NFC", Toast.LENGTH_SHORT);
            toast.show();
            finish();
            return;
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            Toast toast = Toast.makeText(this, "请在系统设置中先启用NFC功能", Toast.LENGTH_SHORT);
            toast.show();
        }

        btn_send = (Button) findViewById(R.id.btn_write);
        et1 = (EditText) findViewById(R.id.et1);
        tv = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        listView = (ListView) findViewById(R.id.lv);

        arrayList = new ArrayList<ListData>();
        arrayAdapter = new TagDataAdapter (MainActivity.this,R.layout.list_layout,arrayList);
        listView.setAdapter(arrayAdapter);

        //添加ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData clickItem = arrayList.get(position);
                Toast.makeText(MainActivity.this,clickItem.getTagData().getId(),Toast.LENGTH_SHORT).show();
                Log.d("chen","have dianji,,,");
                Intent intentdata = new Intent(MainActivity.this,Main2Activity.class);
                intentdata.putExtra("ItemData",clickItem);
                startActivity(intentdata);
            }
        });


        tagData = new TagData("123","教十一A座3楼","001");



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("chen","onNewIntent......");
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setIntent(new Intent());
        Log.d("chen","onStart......");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("chen","onResume......");


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.d("chen","action detect......");
            readFromTag(getIntent());//得到检测数据

            tv2.setText(tagDataDetect.getIDItself() + " " +tagDataDetect.getId() + " "+ tagDataDetect.getAddress());

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sDateFormat2 = new SimpleDateFormat("hh:mm");
            String date = sDateFormat.format(new java.util.Date());
            String time = sDateFormat2.format(new java.util.Date());
            tv.setText(date+" "+time);

            listData = new ListData(tagDataDetect,date,time);
            //arrayList.add(listData);
            arrayList.add(0,listData);//0代表每添加数据直接放在list的第一位
            arrayAdapter.notifyDataSetChanged();

        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvData = et1.getText().toString();
                sendDataToTag();
            }
        });
    }

    //从标签中读取Tag的内容
    private Boolean readFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if (mNdefRecord != null) {
                readResult = new String(mNdefRecord.getPayload(), "UTF-8");
//                tagDataDetect= (TagData) ByteToObject(readResult.getBytes());

                tagDataDetect= (TagData) ByteToObject(mNdefRecord.getPayload());
                Log.d("chen","reading..."+tagDataDetect.getId());
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        return false;
    }

    private void sendDataToTag() {
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

    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
}
