package com.chen.chennfctest1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by chenc on 2017/5/27.
 */

public class ChenNfcHelper {
    private TagData readFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        TagData tagDataDetect;
        try {
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");

                tagDataDetect= (TagData) ByteToObject(mNdefRecord.getPayload());
                Log.d("chen","reading..."+tagDataDetect.getId());
                return tagDataDetect;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        return null;
    }

    public void sendDataToTag(TagData tagData, Activity activity) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(activity.getIntent().getAction())) {
            Tag tag = activity.getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Ndef ndef = Ndef.get(tag);
            try {
                ndef.connect();
                NdefRecord ndefRecord = NdefRecord.createMime("text/plain", objectToByte(tagData));
                //NdefRecord ndefRecord = NdefRecord.createMime("text/plain", tvData.getBytes() );
                NdefRecord[] records = {ndefRecord};
                NdefMessage ndefMessage = new NdefMessage(records);
                ndef.writeNdefMessage(ndefMessage);
                Toast toast = Toast.makeText(activity, "Finish send ", Toast.LENGTH_SHORT);
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
