package com.victorguerrero.screeninteractioners;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorguerrero.screeninteractioners.URLFetcher.OnFetched;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFullViewActivity extends Activity implements OnTouchListener {
	private Contact contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_full_view);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				contact = new Contact(new JSONObject(extras.getString("contact")));
				LoadContactData(contact);
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
		ImageView addContact = (ImageView)findViewById(R.id.full_view_add_contact);
		ImageView favoriteContact = (ImageView)findViewById(R.id.full_view_favorite_contact);
		
		email.setOnTouchListener(this);
		telephone.setOnTouchListener(this);
		website.setOnTouchListener(this);
		favoriteContact.setOnTouchListener(this);
		addContact.setOnTouchListener(this);
		
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

	public void setFavoritedImage(ImageView favoritedView) {
		int favoriteImageId;
		if (contact.getFavorited()) {
			favoriteImageId = R.drawable.favorite_contact;
		} else {
			favoriteImageId = R.drawable.favorite_contact_active;
		}
		Drawable favoriteImage = getResources().getDrawable(favoriteImageId);
		favoritedView.setImageDrawable(favoriteImage);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
		    	case R.id.full_view_telephone:
            		call();
            		return true;
		    	case R.id.full_view_email:
            		sendEmail();
            		return true;
		    	case R.id.full_view_website:
            		openWebpage();
            		return true;
				case R.id.full_view_favorite_contact:
	        		favoriteContact();
	        		return true;
				case R.id.full_view_add_contact:
		    		addContact();
		    		return true;
			}
		}
		
		return false;
	}
	
	public void openWebpage() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(contact.getWebpage()));
		startActivity(i);
	}
	
	public void call() {
		String uri = "tel:" + contact.getPhone();
		Intent i = new Intent(Intent.ACTION_CALL);
		i.setData(Uri.parse(uri));
		
		startActivity(i);
	}
	
	public void sendEmail() {
		String email = contact.getEmail();
        String subject = "";
        String message = "";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, message);
        i.setType("message/rfc822");

        startActivity(Intent.createChooser(i, getText(R.string.choose_email_client)));
	}
	
	public void addContact() {
		Intent i = new Intent(Intent.ACTION_INSERT,
							ContactsContract.Contacts.CONTENT_URI);
		i.setType(ContactsContract.Contacts.CONTENT_TYPE);
		i.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
		i.putExtra(ContactsContract.Intents.Insert.EMAIL, contact.getEmail());
	    i.putExtra(ContactsContract.Intents.Insert.NAME, contact.getName());
	    i.putExtra(ContactsContract.Intents.Insert.PHONE, contact.getPhone());
	    i.putExtra(ContactsContract.Intents.Insert.COMPANY, getText(R.string.company));
	    i.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.getJobTitle());
	    
		startActivity(i);
	}
	
	public void favoriteContact() {
		
	}

}
