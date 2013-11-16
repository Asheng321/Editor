package com.github.zimengle.editor;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;

public class DisplayActivity extends SherlockActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("展示");
		WebView webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication",
						cm.message() + " -- From line " + cm.lineNumber()
								+ " of " + cm.sourceId());
				return true;
			}
		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		webView.loadUrl("file:///android_asset/display.html?"+getIntent().getStringExtra("result"));
		addContentView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	        finish(); 

	        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;

	    }

	    return true;
	}
	
	
}
