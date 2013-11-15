package com.github.zimengle.editor.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class KeyboardLinearLayout extends LinearLayout {

	public KeyboardLinearLayout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public KeyboardLinearLayout(Context context) {
		super(context);
	}

	private OnSoftKeyboardListener onSoftKeyboardListener;

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		if (onSoftKeyboardListener != null) {
			final int newSpec = MeasureSpec.getSize(heightMeasureSpec);
			final int oldSpec = getMeasuredHeight();
			// If layout became smaller, that means something forced it to
			// resize. Probably soft keyboard :)
			Log.d("zzzz", "old:"+oldSpec+",new:"+newSpec);
			if(oldSpec != newSpec){
				if (oldSpec > newSpec) {
					onSoftKeyboardListener.onShown();
				} else {
					onSoftKeyboardListener.onHidden();
				}
			}
			
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public final void setOnSoftKeyboardListener(
			final OnSoftKeyboardListener listener) {
		this.onSoftKeyboardListener = listener;
	}
	
	

	// Simplest possible listener :)
	public interface OnSoftKeyboardListener {
		public void onShown();

		public void onHidden();
	}

}
