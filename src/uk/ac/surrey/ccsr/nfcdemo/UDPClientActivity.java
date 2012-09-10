package uk.ac.surrey.ccsr.nfcdemo;

//import UDPClient.myapps.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.util.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;

public class UDPClientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final EditText et = (EditText) findViewById(R.id.edit_message);
        final TextView tv = (TextView) findViewById(R.id.text_echo);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        
        final Button button = (Button) findViewById(R.id.button_echo);
        button.setOnClickListener(new View.OnClickListener() {
			
        	/** This method is called when the button is pressed */
            public void onClick(View v) {
            	/* get the text from the EditText and 
            	 * append in the TextView 
            	 */
            	String msg = et.getText().toString();
            	tv.append(msg + "\n");
            	/* clear the EditText */
				et.setText(null);
				
				try {
		        	sendUDPMessage(msg, 9999);
		        } catch(IOException io) {
		        	
		        }
			}
		});
    }
    
    public void sendUDPMessage(String msg, int port) throws java.io.IOException {
    	DatagramSocket socket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName("192.168.1.193");
        
        byte[] outData = msg.getBytes();
        
        DatagramPacket out = new DatagramPacket(outData, outData.length, serverIP,9999);
       
        Log.d("length", "" + outData.length);
        
        socket.send(out);
        socket.close();
    }
}