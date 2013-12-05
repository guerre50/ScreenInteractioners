package com.victorguerrero.screeninteractioners;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorguerrero.screeninteractioners.URLFetcher.OnFetched;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFullViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_full_view);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				Contact c = new Contact(new JSONObject(extras.getString("contact")));
				LoadContactData(c);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contact_full_view, menu);
		return true;
	}
	
	public void LoadContactData(final Contact contact) {
		TextView name = (TextView)findViewById(R.id.full_view_name);
		TextView jobTitle = (TextView)findViewById(R.id.full_view_job_title);
		TextView email = (TextView)findViewById(R.id.full_view_email);
		TextView telephone = (TextView)findViewById(R.id.full_view_telephone);
		TextView website = (TextView)findViewById(R.id.full_view_website);
		
		name.setText(contact.getName());
		jobTitle.setText(contact.getJobTitle());
		email.setText(contact.getEmail());
		telephone.setText(contact.getPhone());
		website.setText(contact.getWebpage());
		
		
		String pictureURL = contact.getPictureURL();
		if (!pictureURL.equals("")) {
			final ImageView pictureView = (ImageView)findViewById(R.id.full_view_picture);
			Bitmap picture = contact.getPicture();
			
			if (picture == null) {
				new ImageFetcher()
		        	.setOnFetched(new OnFetched() {
						@Override
						public <T> void onFetched(T response) {
							if (response != null) {
								final Bitmap image = (Bitmap)response;
								contact.setPicture(image);
								runOnUiThread(new Runnable() {
								    @Override
								    public void run() {
								    	pictureView.setImageBitmap(image);
								    	pictureView.invalidate();
								    }
								});
							}
						}
					})
		        	.execute(pictureURL);
			} else {
				
			}
			
		}
	}

}
