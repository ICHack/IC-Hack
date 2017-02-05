package com.boazfrancis.firsttry;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import java.nio.charset.Charset;

public class NFCActivity extends Activity {
    NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters;
    protected void onCreate(Bundle savedInstanceState) {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent  = PendingIntent.getActivity(this,0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {

        }

        mNdefExchangeFilters = new IntentFilter[]{ndefDetected};

    }

    public NdefRecord[] createRecords(String test) {
        NdefRecord[] records = new NdefRecord[1];
        byte[] payload = test.getBytes(Charset.forName("UTF-8"));

        NdefRecord record = new NdefRecord(
                NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT,
                new byte[0],
                payload);
        records[0] = record;
        return records;
    }

    private void enableNdefExchange() {
        mNfcAdapter.enableForegroundNdefPush(NFCActivity.this,new NdefMessage(createRecords("please work")));
        mNfcAdapter.enableForegroundDispatch(this,mNfcPendingIntent,mNdefExchangeFilters,null);
    }



}
