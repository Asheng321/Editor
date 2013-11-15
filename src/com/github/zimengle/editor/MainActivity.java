package com.github.zimengle.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.github.zimengle.editor.R;
import com.github.zimengle.editor.webview.KeyboardLinearLayout;
import com.github.zimengle.editor.webview.KeyboardLinearLayout.OnSoftKeyboardListener;
import com.github.zimengle.editor.webview.ToolbarInterface;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int SELECT_PICTURE = 1;

	private View mToolbar;

	private View mPicBtn;

	private View mFaceBtn;

	private View mVoiceBtn;

	private View mAtBtn;

	private ToolbarInterface toolbarInterface;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mToolbar = findViewById(R.id.toolbar);
		mPicBtn = findViewById(R.id.pic);
		mPicBtn.setOnClickListener(this);
		mFaceBtn = findViewById(R.id.face);
		mFaceBtn.setOnClickListener(this);
		mVoiceBtn = findViewById(R.id.voice);
		mVoiceBtn.setOnClickListener(this);
		mAtBtn = findViewById(R.id.at);
		mAtBtn.setOnClickListener(this);
		// toolbar.setVisibility(View.VISIBLE);

		WebView webView = (WebView) findViewById(R.id.webview);

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
		toolbarInterface = new ToolbarInterface(mToolbar, webView);
		webView.addJavascriptInterface(toolbarInterface, "nativetoolbar");
		webView.loadUrl("file:///android_asset/index.html");

		((KeyboardLinearLayout) findViewById(R.id.root)).setOnSoftKeyboardListener(new OnSoftKeyboardListener() {
			
			public void onShown() {
				Log.d("zzzz", "show");
				mToolbar.setVisibility(View.VISIBLE);
				
			}
			
			public void onHidden() {
				Log.d("zzzz", "hide");
				mToolbar.setVisibility(View.GONE);
				
			}
		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pic:
			onPicBtnClick();
			break;
		case R.id.face:
			onFaceBtnClick();
			break;
		case R.id.voice:
			onVoiceBtnClick();
			break;
		case R.id.at:
			onAtBtnClick();
		default:
			break;
		}

	}

	private void onAtBtnClick() {
		// TODO Auto-generated method stub

	}

	private void onVoiceBtnClick() {
		// TODO Auto-generated method stub

	}

	private void onFaceBtnClick() {
		// TODO Auto-generated method stub

	}

	private void onPicBtnClick() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				Gson gson = new Gson();
				try {
					String list = gson.toJson(getBase64List(selectedImageUri));
					// toolbarInterface.run("insertImage("+list+")");
					toolbarInterface.run("nativetoolbarCallback(" + list + ")");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static class Image {
		private String path;
		private String mime;
		private String base64;

		public Image(String path, String mime) {
			super();
			this.path = path;
			this.mime = mime;
			try {
				base64 = new String(Base64.encode(
						IOUtils.toByteArray(new FileInputStream(path)),
						Base64.DEFAULT));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String getURIData() {
			return "data:" + mime + ";base64," + base64;
		}

	}

	private List<String> getBase64List(Uri uri) {
		List<String> list = new ArrayList<String>();
		String[] projection = { MediaStore.Images.Media.DATA,
				Images.Media.MIME_TYPE };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);

		while (cursor.moveToNext()) {
			String path = cursor.getString(0);
			String mime = cursor.getString(1);
			list.add(new Image(path, mime).getURIData());
		}
		return list;
	}

}
