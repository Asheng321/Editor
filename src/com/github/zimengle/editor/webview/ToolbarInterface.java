package com.github.zimengle.editor.webview;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class ToolbarInterface {

	
	private WebView webView;
	
	private Callback callback;

	public static interface Callback{
		public void onSubmit(String result);
	}
	
	public ToolbarInterface(WebView webView,Callback callback) {
		
		this.webView = webView;
		
		this.callback = callback;
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
	public void submit(String result){
		if(callback != null){
			callback.onSubmit(result);
		}
	}
	
	@JavascriptInterface
	public void openKeyboard(){
		new Handler().post(new Runnable() {
			
			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) webView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			    if (inputMethodManager != null) {
			        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			    }
				
			}
		});
		
	}
	
	public void run(final String scriptSrc){
		webView.post(new Runnable() {
			
			public void run() {
				webView.loadUrl("javascript:"+scriptSrc);
			}
		});
		
	}
	
	

	
}
