package com.chen.chennfctest1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.Menu;
import android.view.MenuItem;
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
    private Button btnsend;
    private TextView noteText;
    private TagData tagData;
    private TagData tagDataDetect;
    static private ArrayList <ListData> arrayList;
    static private TagDataAdapter arrayAdapter ;
    static private ListView listView;
    private ListData listData;

    private Button btn_send;
    private TextView textViewNoteInstructions;

    private DictionaryOpenHelper dictionaryOpenHelper;
    private SQLiteDatabase wsqLiteDatabase;
    private SQLiteDatabase rsqLiteDatabase;
    private static final String DICTIONARY_TABLE_NAME = "dictionary2";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // tv= (TextView) findViewById(R.id.tv);
        dictionaryOpenHelper = new DictionaryOpenHelper(this,"dic2.db",null,1);
        wsqLiteDatabase = dictionaryOpenHelper.getWritableDatabase();



//        ContentValues value = new ContentValues();
//        value.put("id","001");
//        value.put("address","jiaodj");
//        value.put("IDItself","dsafadfjh");
//        value.put("time","dsafsdf");
//        value.put("date","001adfa");
//        wsqLiteDatabase.insert(DICTIONARY_TABLE_NAME,null,value);
//        value.clear();
//        value.put("id","002");
//        value.put("address","gfsd");
//        value.put("IDItself","dsafadfjh");
//        value.put("time","dsafsdf");
//        value.put("date","adfa");
//        wsqLiteDatabase.insert(DICTIONARY_TABLE_NAME,null,value);
//        wsqLiteDatabase.close();
//
//
//        rsqLiteDatabase=dictionaryOpenHelper.getReadableDatabase();
//        Cursor cursor = rsqLiteDatabase.query(DICTIONARY_TABLE_NAME,null,null,null,null,null,null);
//        StringBuilder  all= new StringBuilder();
//        if(cursor.moveToFirst()){
//            do{
//                //遍历游标
//                String id = cursor.getString(cursor.getColumnIndex("id"));
//                String address = cursor.getString(cursor.getColumnIndex("address"));
//                String IDItself = cursor.getString(cursor.getColumnIndex("IDItself"));
//                String time = cursor.getString(cursor.getColumnIndex("time"));
//                String date = cursor.getString(cursor.getColumnIndex("date"));
//                all.append(id+"  "+address+"  "+IDItself+"  "+time+"  "+date+  "\n");
//            }while (cursor.moveToNext());
//        }
//        tv.setText(all);
//        cursor.close();
//        rsqLiteDatabase.close();


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

        btnsend = (Button) findViewById(R.id.btnsend);
        listView = (ListView) findViewById(R.id.lv);
        noteText=(TextView)findViewById(R.id.noteText);
        textViewNoteInstructions=(TextView)findViewById(R.id.textViewNoteInstructions);
        textViewNoteInstructions.setVisibility(View.INVISIBLE);
        arrayList = new ArrayList<ListData>();
        arrayAdapter = new TagDataAdapter (MainActivity.this,R.layout.list_layout,arrayList);
        listView.setAdapter(arrayAdapter);

        //添加ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData clickItem = arrayList.get(position);
                //Toast.makeText(MainActivity.this,clickItem.getTagData().getId(),Toast.LENGTH_SHORT).show();
                Log.d("chen","have dianji,,,");
                Intent intentdata = new Intent(MainActivity.this,Main2Activity.class);
                intentdata.putExtra("ItemData",clickItem);
                startActivity(intentdata);
            }
        });


        tagData = new TagData("123","教十一A座3楼","001");



    }

    private void insertsql(String id,String address,String IDItself,String time,String date){
        SQLiteDatabase wsqLiteDatabase = dictionaryOpenHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("id",id);
        value.put("address",address);
        value.put("IDItself",IDItself);
        value.put("time",time);
        value.put("date",date);
        wsqLiteDatabase.insert(DICTIONARY_TABLE_NAME,null,value);
        wsqLiteDatabase.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"历史记录");
        menu.add(0,1,0,"清除记录");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 0:
                Toast.makeText(this,"显示历史记录",Toast.LENGTH_SHORT).show();
                arrayList.clear();
                rsqLiteDatabase=dictionaryOpenHelper.getReadableDatabase();
                Cursor cursor = rsqLiteDatabase.query(DICTIONARY_TABLE_NAME,null,null,null,null,null,null);

                if(cursor.moveToFirst()){
                    do{
                        //遍历游标
                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        String IDItself = cursor.getString(cursor.getColumnIndex("IDItself"));
                        String time = cursor.getString(cursor.getColumnIndex("time"));
                        String date = cursor.getString(cursor.getColumnIndex("date"));

                        //从游标中取出对象
                        ListData cursorListData=new ListData(IDItself,address,id,date,time);
                        arrayList.add(0,cursorListData);
                    }while (cursor.moveToNext());
                }
                arrayAdapter.notifyDataSetChanged();
                cursor.close();
                rsqLiteDatabase.close();
                noteText.setVisibility(View.INVISIBLE);
                textViewNoteInstructions.setVisibility(View.VISIBLE);
                break;
            case 1:
                String sql = "delete from dictionary2";
                SQLiteDatabase wsqLiteDatabase = dictionaryOpenHelper.getWritableDatabase();
                wsqLiteDatabase.execSQL(sql);
                wsqLiteDatabase.close();
                arrayList.clear();
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(this,"历史记录已清除",Toast.LENGTH_SHORT).show();
                noteText.setVisibility(View.VISIBLE);
                textViewNoteInstructions.setVisibility(View.INVISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        Log.d("chen","onStart......");
    }

    @Override
    protected void onPause() {
        super.onPause();
           setIntent(new Intent());
        Log.d("chen","onPause......");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("chen","onResume......");


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.d("chen","action detect......");
            readFromTag(getIntent());//得到检测数据



            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sDateFormat2 = new SimpleDateFormat("HH:mm");



            String date = sDateFormat.format(new java.util.Date());
            String time = sDateFormat2.format(new java.util.Date());


            listData = new ListData(tagDataDetect,date,time);
            insertsql(tagDataDetect.getId(),tagDataDetect.getAddress(),tagDataDetect.getIDItself(),time,date);

            //arrayList.add(listData);
            arrayList.add(0,listData);//0代表每添加数据直接放在list的第一位
            arrayAdapter.notifyDataSetChanged();
            noteText.setVisibility(View.INVISIBLE);
            textViewNoteInstructions.setVisibility(View.VISIBLE);

        }



        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentbtn = new Intent(MainActivity.this,Write2Nfc.class);
                startActivity(intentbtn);
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
                //开始个性化对象的输入
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
