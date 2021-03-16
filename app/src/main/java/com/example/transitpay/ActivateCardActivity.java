package com.example.transitpay;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class ActivateCardActivity extends AppCompatActivity {
    public static final String Error_Detected="No NFC tag Detected";
    public static final String Write_success="Text Written Successfully";
    public static final String Write_Error="Error during writing, Try Again";
    private static final String TAG = "MyTAG NFC";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] writingTagFilters;
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView phonenumberEditText;
    TextView nfcContents;
    Button activateButton;
    FirebaseDatabase mDatabase;
    DatabaseReference mDbRef;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activate_card);

        mDatabase = FirebaseDatabase.getInstance();
        mDbRef=mDatabase.getReference("user");
        ref=FirebaseDatabase.getInstance().getReference("user");


        setupUI();
        nfcAdapter= NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "This device does not support NFC",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        readfromIntent(getIntent());

        pendingIntent= PendingIntent.getActivity(this,
                0, new Intent(this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected= new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);

        writingTagFilters=new IntentFilter[]{ tagDetected };
    }



    private void setupUI()
    {
        phonenumberEditText=findViewById(R.id.phonenumberEditText);
        nfcContents=findViewById(R.id.nfcTextView);
        activateButton=findViewById(R.id.activatetagButton);
        context=this;
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCardAvailable();
            }
        });
    }


    private void readfromIntent(Intent intent)
    {
        String action = intent.getAction();

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            Parcelable[] rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] message= null;

            if(rawMessage != null)
            {
                message= new NdefMessage[rawMessage.length];
                for(int i=0; i< rawMessage.length; i++)
                {
                    message[i]=(NdefMessage) rawMessage[i];
                }
            }
            buildTagview(message); //reads message
        }
    }
    //Function to check if the card has already been activated

    private void isCardAvailable(){
        Query query= ref
                .orderByChild("NFC TAG").equalTo(bin2hex(myTag.getId()));
        query.addListenerForSingleValueEvent(new ValueEventListener() { //addValueEventListener
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "This card is linked " +
                                    "to another account. Please try with a different card",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    linkCard(phonenumberEditText.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //function to link the nfc tag to an existing account.
    private void linkCard(String phoneNumber){
        //orders the data by NFC TAG
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot snapshot) {

                 if (snapshot.child(phoneNumber).exists() ) {

                     mDbRef.child(phoneNumber).child("Valid card").setValue("TRUE");
                     mDbRef.child(phoneNumber).child("NFC TAG").setValue( bin2hex(myTag.getId()));
                     try
                     {
                         if(myTag==null)
                         {
                             Toast.makeText(context, Error_Detected, Toast.LENGTH_LONG).show();

                         }else{

                             write("Phone number: "+ phonenumberEditText.getText().toString(),
                                     myTag);
                         }
                     }
                     catch (IOException e) {

                         Toast.makeText(context, Write_Error, Toast.LENGTH_LONG).show();
                         e.printStackTrace();

                     } catch (FormatException e){

                         Toast.makeText(context, Write_Error, Toast.LENGTH_LONG).show();
                         e.printStackTrace();
                     }
                 }else{
                     Toast.makeText(ActivateCardActivity.
                                     this, "Phone number is not linked to an account",
                                        Toast.LENGTH_LONG).show();
                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }

    //function to add the contents of the NFC
    private void buildTagview(NdefMessage[] message)
    {
        if(message == null || message.length==0)
            return;

        String text="";
        byte[] payload=message[0].getRecords()[0].getPayload();
        String textEncode=((payload[0] & 128) == 0) ? "UTF-8" :"UTF-16"; //get the text
        int LanguageCodeLength= payload[0] & 0063;

        try
        {
            text= new String(payload, LanguageCodeLength+1 , payload.length
                    - LanguageCodeLength - 1, textEncode);

            Toast.makeText(ActivateCardActivity.this, text, Toast.LENGTH_LONG).show();

        }catch(UnsupportedEncodingException e){
            Log.e("UnsupportedEncoding", e.toString());

        }
        nfcContents.setText("NFC Content:"+ text);

    }


    //function to connect and write to the NFC tag
    private void write(String text, Tag tag) throws IOException, FormatException
    {
        NdefRecord[] record={ createRecord(text) };
        NdefMessage message= new NdefMessage(record);
        //Get an instance
        Ndef ndef=Ndef.get(tag); //throws null with unformatted card
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

    //Function to create the record that is going to be added in the NFC tag.
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException{

        String lang ="num"; //en
        byte[] textBytes =text.getBytes();
        byte[] langBytes= lang.getBytes("US-ASCII");
        int langlength= langBytes.length;
        int textlength= textBytes.length;
        byte[] payload= new byte[1 +langlength + textlength];

        //set status byte (see NDEF spec for actual bits)
        payload[0]=(byte) langlength;

        //copy into payload
        System.arraycopy(langBytes,0, payload, 1,langlength);
        System.arraycopy(textBytes,0, payload, 1+langlength, textlength);


        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readfromIntent(intent);

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
        {
            myTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); //here i get the UID of the NFC tag
            Log.d(TAG, "tag ID= " + myTag.getId().toString());
//            Toast.makeText(this,"tag ID= " + bin2hex(myTag.getId()),
//                    Toast.LENGTH_LONG).show();
        }
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeModeOff();
    }

    @Override
    protected void onResume() {
        super.onResume();
        writeModeOn();
    }

    //enable writing
    private void writeModeOn()
    {
        writeMode=true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }

    //disable writing
    private void writeModeOff()
    {
        writeMode=false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}
