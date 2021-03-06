package com.chen.chennfctest1;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author 瑞泽_Zerui
 * 将数据写入NFC标签
 * 注：写入数据关键是弄清数据的封装方法，即NdefRecord和NdefMessage几个关键类,具体可参见开发文档或网上资料
 */
public class Write2Nfc extends Activity {
	private LinearLayout linearLayout ;
	private EditText editText;
	private EditText editText1;
	private EditText editText2;
	private TextView noteText;
	private Button wButton;
	private IntentFilter[] mWriteTagFilters;
	private NfcAdapter nfcAdapter;
	PendingIntent pendingIntent;
	String[][] mTechLists;
	private Boolean ifWrite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_ifo);
		init();
		displayControl(false);
		System.out.println("0....");
	}

	private void init() {
		// TODO Auto-generated method stub
		ifWrite = false;
		linearLayout = (LinearLayout) findViewById(R.id.linear);
		editText = (EditText) findViewById(R.id.editText);
		editText1 = (EditText) findViewById(R.id.editText1);
		editText2 = (EditText) findViewById(R.id.editText2);
		wButton = (Button) findViewById(R.id.writeBtn);
		noteText=(TextView)findViewById(R.id.noteText);
		wButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ifWrite = true;
				displayControl(true);
			}
		});
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		ndef.addCategory("*/*");
		mWriteTagFilters = new IntentFilter[] { ndef };
		mTechLists = new String[][] { new String[] { NfcA.class.getName() },
				new String[] { NfcF.class.getName() },
				new String[] { NfcB.class.getName() },
				new String[] { NfcV.class.getName() } };
	}
	public void displayControl(Boolean ifWriting){
		if(ifWriting){			
			noteText.setVisibility(View.VISIBLE);
			//editText.setVisibility(View.INVISIBLE);
			linearLayout.setVisibility(View.INVISIBLE);
			wButton.setVisibility(View.INVISIBLE);
			return;
		}
		noteText.setVisibility(View.INVISIBLE);
		//editText.setVisibility(View.VISIBLE);
		linearLayout.setVisibility(View.VISIBLE);
		wButton.setVisibility(View.VISIBLE);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, pendingIntent,
				mWriteTagFilters, mTechLists);
		System.out.println("1....");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		System.out.println("1.5....");
		String text = editText.getText().toString();
		if (text == null) {
			Toast.makeText(getApplicationContext(), "数据不能为空!",
					Toast.LENGTH_SHORT).show();
			System.out.println("2....");

			return;
		}
		if (ifWrite == true) {
			System.out.println("2.5....");
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())||
					NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
				Tag tag =intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Ndef ndef = Ndef.get(tag);
				try {
					//数据的写入过程一定要有连接操作
					ndef.connect();
					TagData tagData = new TagData(editText.getText().toString(),editText2.getText().toString(),editText1.getText().toString());
					//TagData tagData = new TagData("123","16舍","110");
					NdefRecord ndefRecord = NdefRecord.createMime("text/plain", objectToByte(tagData));
					//构建数据包，也就是你要写入标签的数据
//					NdefRecord ndefRecord = new NdefRecord(
//							NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
//							new byte[] {}, text.getBytes());
					NdefRecord[] records = { ndefRecord };
					NdefMessage ndefMessage = new NdefMessage(records);
					ndef.writeNdefMessage(ndefMessage);
					System.out.println("3....");
					Toast.makeText(getApplicationContext(), "数据写入成功!",
							Toast.LENGTH_SHORT).show();
					displayControl(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FormatException e) {
					
				}
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
