package com.github.zimengle.editor;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.github.zimengle.editor.R;
import com.github.zimengle.editor.webview.ToolbarInterface;
import com.github.zimengle.editor.webview.ToolbarInterface.ViewHolder;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity implements OnClickListener {

	private static final int GALLERY_PICTURE = 1;

	private static final int CAMERA_REQUEST = 2;

	private Intent pictureActionIntent = null;

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
		getSupportActionBar().setTitle("发表");
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
		toolbarInterface = new ToolbarInterface(new ViewHolder(getSupportActionBar(), mToolbar), webView,new ToolbarInterface.Callback() {
			
			public void onSubmit(String result) {
				Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
				intent.putExtra("result", result);
				MainActivity.this.startActivity(intent);
			}
		});
		webView.addJavascriptInterface(toolbarInterface, "nativetoolbar");
		webView.loadUrl("file:///android_asset/index.html");

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pic:
			startDialog();
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
		Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.face1)).getBitmap();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		List<String> result = new ArrayList<String>();
		result.add("data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
		Gson gson = new Gson();
		String list = gson.toJson(result);
		Log.d("zzzz", list);
		toolbarInterface.run("nativetoolbarCallbackFace("+list+")");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode  == GALLERY_PICTURE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Uri selectedImageUri = data.getData();
					Gson gson = new Gson();
					try {
						String list = gson.toJson(getBase64List(selectedImageUri));
						toolbarInterface.run("nativetoolbarCallback(" + list + ")");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				

			}
		}else if (requestCode == CAMERA_REQUEST) {
			if (resultCode == RESULT_OK) {
				if (data.hasExtra("data")) {
					Bitmap bitmap = ((Bitmap) data.getExtras().get("data"));
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();  
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object 
					List<String> result = new ArrayList<String>();
					result.add("data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
					Gson gson = new Gson();
					String list = gson.toJson(result);
					Log.d("zzzz", list);
					toolbarInterface.run("nativetoolbarCallback("+list+")");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("提交")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		if("提交".equals(item.getTitle())){
			submit();
		}
		
		return true;
	}

	private void submit() {
		toolbarInterface.run("nativetoolbarSubmit()");
		
	}

	private void startDialog() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle("选择照片");
		myAlertDialog.setMessage("请选择要插入的图片?");

		myAlertDialog.setPositiveButton("相册",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						pictureActionIntent = new Intent(
								Intent.ACTION_GET_CONTENT, null);
						pictureActionIntent.setType("image/*");
						pictureActionIntent.putExtra("return-data", true);
						startActivityForResult(pictureActionIntent,
								GALLERY_PICTURE);
					}
				});

		myAlertDialog.setNegativeButton("拍照",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						pictureActionIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(pictureActionIntent,
								CAMERA_REQUEST);

					}
				});
		myAlertDialog.show();
	}

}
