package com.victorguerrero.screeninteractioners;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class MainActivity extends Activity implements OnTouchListener {
	final private ContactsController contacts = ContactsController.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
		
		// List setup
	    ListView list = (ListView)findViewById(R.id.list);
	    ContactArrayAdapter dataAdapter = new ContactArrayAdapter(this, R.layout.contact_row_view, contacts);
	    
	    list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LoadContactFullView(contacts.getContact(arg2));
			}
	    	
	    });
	    list.setEmptyView(findViewById(R.id.contacts_loading_view));
	    list.setAdapter(dataAdapter);
	    
	    //Action bar setup
	    ImageButton orderingBtn = (ImageButton)findViewById(R.id.contact_ordering);
	    orderingBtn.setOnTouchListener(this);
	    
	}
	
	public void openSortingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.select_sorting)
	           .setItems(R.array.ordering_options, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   
	            	   contacts.setOrdering(which);
	           }
	    });
	           
	    builder.create().show();
	}
	
	public void LoadContactFullView(Contact contact) {
		Intent intent = new Intent(this, ContactFullViewActivity.class);
		intent.putExtra("contact", contact.toString());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
	        	case R.id.contact_ordering:
            		openSortingDialog();
            		return true;
			}
		}
        
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_ordering:
	        	openSortingDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
