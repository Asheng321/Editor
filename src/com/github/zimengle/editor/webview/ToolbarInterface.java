package com.github.zimengle.editor.webview;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class ToolbarInterface {

	private View toolbar;
	
	private WebView webView;

	public ToolbarInterface(View toolbar,WebView webView) {
		this.toolbar = toolbar;
		this.webView = webView;
	}
	
	@JavascriptInterface
	public void show(){
//		toolbar.setVisibility(View.VISIBLE);
	}
	
	
	@JavascriptInterface
	public void hide(){
//		toolbar.setVisibility(View.GONE);
	}
	
	@JavascriptInterface
	public void openKeyboard(){
		InputMethodManager inputMethodManager = (InputMethodManager) toolbar.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	    if (inputMethodManager != null) {
	        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	    }
	}
	
	public void run(final String scriptSrc){
		webView.post(new Runnable() {
			
			public void run() {
				webView.loadUrl("javascript:"+scriptSrc);
			}
		});
		
	}
	
	

	
}
