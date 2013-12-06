package com.victorguerrero.screeninteractioners.controllers;

import java.util.ArrayList;

import com.victorguerrero.screeninteractioners.R;
import com.victorguerrero.screeninteractioners.controllers.URLFetcher.OnFetched;
import com.victorguerrero.screeninteractioners.models.Contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ContactArrayAdapter extends ArrayAdapter<Contact> implements ContactsController.OnContactsChangeListener
{   
    private ArrayList<Contact> list;
    private ContactsController contactsController;
    private int contactViewId;
    private Drawable noAvatar;
    private Activity activity;

    public ContactArrayAdapter(Activity activity, int contactViewResourceId, ContactsController contactsController) 
    {
        super(activity, contactViewResourceId, contactsController.get());
        contactsController.setOnContactsChangeListener(this);
        
        this.list = contactsController.get();
        this.contactsController = contactsController;
        this.contactViewId = contactViewResourceId;
        this.activity = activity;
        this.noAvatar = activity.getResources().getDrawable(R.drawable.no_thumbnail);
    }
         
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
        	LayoutInflater inflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);          
        	convertView = inflator.inflate(contactViewId, null);
        }
        
        TextView nameView = (TextView) convertView.findViewById(R.id.contact_name);
        TextView jobView = (TextView) convertView.findViewById(R.id.contact_job_position);
        ImageView starredView = (ImageView) convertView.findViewById(R.id.contact_starred);
        
        Contact contact = contactsController.getContact(position);
        
        convertView.setTag(contact.getName());
        
        nameView.setText(contact.getName());
        jobView.setText(contact.getJobTitle());
        
        final ImageView thumbnailView = (ImageView)convertView.findViewById(R.id.contact_thumbnail);
        
        
        loadThumbnail(contact, thumbnailView);
 
        return convertView;
    }
    
    // TO-DO move thumbnail loading to Contact
    private void loadThumbnail(final Contact contact, final ImageView thumbnailView) {
    	Bitmap thumbnail = contact.getThumbnail();
        final String thumbnailURL = contact.getThumbnailURL();
		thumbnailView.setTag(thumbnailURL);
		
        if (thumbnail == null ) {
        	thumbnailView.setImageDrawable(noAvatar);
        	if (!contact.getThumbnailURL().equals("")) {
		        new ImageFetcher()
		        	.setOnFetched(new OnFetched() {
						@Override
						public <T> void onFetched(T response) {
							final Bitmap image = (Bitmap)response;
							contact.setThumbnail(image);
							
							if (((String)thumbnailView.getTag()).equals(thumbnailURL)) {
								thumbnailView.setImageBitmap(image);
								thumbnailView.invalidate();
				    		}
						}
					})
		        	.execute(thumbnailURL);
        	}
        } else {
        	thumbnailView.setImageBitmap(thumbnail);
        }
    }

	@Override
	public void onContactsChange() {
		this.list.clear();
		this.list.addAll(contactsController.get());
		notifyDataSetChanged();
	}
}