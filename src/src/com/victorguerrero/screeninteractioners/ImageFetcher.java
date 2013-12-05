package com.victorguerrero.screeninteractioners;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageFetcher extends URLFetcher<Bitmap> {
	private  Bitmap bitmap;
	
	@Override
	protected void onPostExecute(byte[] response) {
		if (this.listener != null) {
			if (bitmap == null) {
				Log.d("FASDFAs", "DFAS");
			}
			listener.onFetched(bitmap);
		}
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
			
			bitmap = BitmapFactory.decodeStream(is);
		} catch(Exception ex) {
			Log.d("FetcherError", ex.toString());
		}
		
		return result;
	}
}
