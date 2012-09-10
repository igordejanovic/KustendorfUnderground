package uk.ac.surrey.ccsr.nfcdemo;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import android.R;

public class NFCLauncherActivty extends Activity {

	private static final String TAG = NFCLauncherActivty.class.getSimpleName();
	private UserNotificationDialog notifyDlg;
	
	private final Button.OnClickListener readQRCode = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
          IntentIntegrator integrator = new IntentIntegrator(NFCLauncherActivty.this);
          integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        }
      };
	
      private final Button.OnClickListener readNFCTag = new Button.OnClickListener() {
          @Override
          public void onClick(View v) {
            //IntentIntegrator integrator = new IntentIntegrator(NFCLauncherActivty.this);
            //integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
          }
        };
        
    private final Button.OnClickListener showMap = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(NFCLauncherActivty.this, Touch.class);
            startActivity(intent);      
            //finish();		
        }
    	
    };
      
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfclauncher_activty);
        findViewById(R.id.buttonQR).setOnClickListener(readQRCode);
        findViewById(R.id.buttonNFC).setOnClickListener(readNFCTag);
        findViewById(R.id.showMap).setOnClickListener(showMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nfclauncher_activty, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
      if (result != null) {
    	  String contents = result.getContents();
        
    	  if (contents != null) 
    	  {
    		  Log.d(TAG, contents);
    		  Log.d(TAG, result.toString());
        	  String nextClue = null;
        	  if((nextClue = simpleDecrypt(contents, 3)) != null) {	
        			System.out.println("Your next clue is : " + nextClue);}
        	  else
        	  {nextClue = "Hello!";}

    		  displayNFCData("QR Code Scan Results", nextClue);
    		  EditText text1 = (EditText) findViewById(R.id.editText1);
    		  Log.d("text1", new Boolean(text1==null).toString());
//    		  if(nextClue.startsWith("Unfortunately")){
//	    		  startActivity(new Intent(this, UDPClientActivity.class));
//    		  }
    		  text1.setText(nextClue);

    	  }else
    	  {
    		  Log.d(TAG, "Read attempt failed\n");
    		  displayNFCData("QR Code Scan Results", "Read attempt failed.");
    	  }
      }
    };
    
    
    @Override
    protected void onResume() {
    	Log.d(TAG, "OnResume()");
    	super.onResume();
    	Intent intent = getIntent();
    	// check if it an NFC based intent
    	if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
    		
    		//Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
    		NdefMessage[] msgs = parseRawNDEF(rawMsgs);
    		Log.d(TAG,"Read the following number of NDEF messages from Tag: " + String.valueOf(msgs.length));
    		// get first record of first message element
    		for (NdefMessage msg : msgs)
    		{
    			NdefRecord[] recs = msg.getRecords();
    			Log.d(TAG, "No of records: " + String.valueOf(recs.length));
	    		
	    		if (UriRecord.isUri(recs[0])){
	    			UriRecord ur = UriRecord.parse(recs[0]);
	    			displayNFCData("NFC scan results: ", ur.getUri().toString());
	    		}
    		}    		
    	}
    }
    
    
    NdefMessage[] parseRawNDEF (Parcelable [] rawMsgs)
    {
    	NdefMessage[] ndefMsgs;
    	if (rawMsgs != null) {
    		ndefMsgs = new NdefMessage[rawMsgs.length];
	        for (int i = 0; i < rawMsgs.length; i++) {
	        	ndefMsgs[i] = (NdefMessage) rawMsgs[i];
	        }
	    }
    	else {
            // Unknown tag type
            byte[] empty = new byte[] {};
            NdefRecord record =
                new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
            NdefMessage msg = new NdefMessage(new NdefRecord[] {
                record
            });
            ndefMsgs = new NdefMessage[] {
                msg
            };
    	}
    	return ndefMsgs;
    }

    String simpleDecrypt(String encryptedText, int expectedSeq) {
  	  String decryptedText = "";
  	  int textLen = encryptedText.length();

  	  for(int i=0; i<textLen; i+=2) {

  	  String output = encryptedText.substring(i, (i + 2));
  	            int decimal = Integer.parseInt(output, 16);
  	  decryptedText = decryptedText + (char)decimal;
  	  }

  	  int index = decryptedText.indexOf('.');
  	  String qrSeqNum = decryptedText.substring(0, index);
  	  String qrText = decryptedText.substring(index+1);
  /*	  if(expectedSeq == Integer.parseInt(qrSeqNum))
  	  return qrText;
  	  else 
  	  return null;*/
  	  return qrText;
  	  }
    void displayNFCData(String title, String text){
    	notifyDlg =  UserNotificationDialog.newInstance(title, text);
    	notifyDlg.show(getFragmentManager(), "dialog");
    }
    
      
}
