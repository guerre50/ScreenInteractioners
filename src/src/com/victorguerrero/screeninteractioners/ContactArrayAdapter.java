package com.victorguerrero.screeninteractioners;

import java.util.ArrayList;

import com.victorguerrero.screeninteractioners.URLFetcher.OnFetched;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        noAvatar = activity.getResources().getDrawable( R.drawable.no_thumbnail);
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
        final ImageView thumbnailView = (ImageView) convertView.findViewById(R.id.contact_thumbnail);
        ImageView starredView = (ImageView) convertView.findViewById(R.id.contact_starred);
        
        final Contact contact = list.get(position);
        
        nameView.setText(contact.getName());
        jobView.setText(contact.getJobTitle());
        convertView.setTag(contact);
        
        Bitmap thumbnail = contact.getThumbnail();
        if (thumbnail == null ) {
        	if (contact.getThumbnailURL().equals("")) {
        		thumbnailView.setImageDrawable(noAvatar);
        	} else {
		        new ImageFetcher()
		        	.setOnFetched(new OnFetched() {
						@Override
						public <T> void onFetched(T response) {
							final Bitmap image = (Bitmap)response;
							contact.setThumbnail(image);
							activity.runOnUiThread(new Runnable() {
							    @Override
							    public void run() {
							    	thumbnailView.setImageBitmap(image);
							    	thumbnailView.invalidate();
							    }
							});
						}
					})
		        	.execute(contact.getThumbnailURL());
        	}
        } else {
        	thumbnailView.setImageBitmap(thumbnail);
        }
 
        return convertView;
    }

	@Override
	public void onContactsChange() {
		this.list.clear();
		this.list.addAll(contactsController.get());
		notifyDataSetChanged();
	}
}