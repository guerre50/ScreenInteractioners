package com.victorguerrero.screeninteractioners.views;

import com.victorguerrero.screeninteractioners.R;

import android.content.Context;
import android.widget.ImageView;

public class ActionDividerView extends ImageView {
	public ActionDividerView (Context context) {
		super(context);
		
		setImageDrawable(context.getResources().getDrawable(R.drawable.action_bar_divider));
	}
}
