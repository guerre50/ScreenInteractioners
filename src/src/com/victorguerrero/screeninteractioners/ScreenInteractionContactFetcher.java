package com.victorguerrero.screeninteractioners;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Debug;
import android.util.Log;



public class ScreenInteractionContactFetcher extends URLFetcher<ArrayList<Contact>> {
	
	@Override
	protected void onPostExecute(byte[] response) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		String jsonResponse = new String(response);
		try {
			JSONArray arr = new JSONArray(jsonResponse);
			
			for (int i = 0; i < arr.length(); ++i) {
				contacts.add(new Contact(arr.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (listener != null) {
			listener.onFetched(contacts);
		}
	}
}
