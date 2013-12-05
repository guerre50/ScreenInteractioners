package com.victorguerrero.screeninteractioners;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;



public class URLFetcher<T> extends AsyncTask<String, Void, byte[]> {
	public interface OnFetched {
		public <T>void onFetched(T contacts);
	}
	
	protected OnFetched listener;
	
	public URLFetcher<T> setOnFetched(OnFetched onContactsFetched) {
		this.listener = onContactsFetched;
		
		return this;
	}
	
	@Override
	protected byte[] doInBackground(String... arg0) {
		byte[] result = new byte[0];
		
		try {
			URL url = new URL(arg0[0]);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			InputStream is = connection.getInputStream();
			
			
			byte[] b = new byte[1024];
			ByteArrayOutputStream response = new ByteArrayOutputStream();
			
			while(is.read(b) != -1) {
				response.write(b);
			}
			
			result = response.toByteArray();
		} catch(Exception ex) {
			Log.d("FetcherError", ex.toString());
		}
		
		return result;
	}
}
