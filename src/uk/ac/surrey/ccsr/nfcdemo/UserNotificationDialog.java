package uk.ac.surrey.ccsr.nfcdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


public class UserNotificationDialog extends DialogFragment {

	public static UserNotificationDialog newInstance(String title, String message) {
		UserNotificationDialog frag = new UserNotificationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }	
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message =  getArguments().getString("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        return builder
        		.setTitle(title)
        		.setIcon(R.drawable.ic_launcher)
        		.setMessage(message)
        		.setCancelable(false)
        		.setNeutralButton("OK", null)	
        		.create();
	}
	
}
