package com.github.zimengle.editor.webview;

import com.actionbarsherlock.app.ActionBar;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class ToolbarInterface {

	private WebView webView;

	private Callback callback;

	private ViewHolder holder;

	private Handler handler = new Handler();

	public static interface Callback {
		public void onSubmit(String result);
	}

	public static class ViewHolder {
		private ActionBar actionBar;
		private View toolbar;

		public ViewHolder(ActionBar actionBar, View toolbar) {
			this.actionBar = actionBar;
			this.toolbar = toolbar;
		}

	}

	public ToolbarInterface(ViewHolder holder, WebView webView,
			Callback callback) {

		this.holder = holder;

		this.webView = webView;

		this.callback = callback;
	}

	@JavascriptInterface
	public void fullscreen() {
		handler.post(new Runnable() {

			public void run() {
				holder.actionBar.hide();
				holder.toolbar.setVisibility(View.GONE);

			}
		});

	}

	@JavascriptInterface
	public void quitFullscreen() {
		handler.post(new Runnable() {

			public void run() {
				holder.actionBar.show();
				holder.toolbar.setVisibility(View.VISIBLE);
			}
		});
	}

	@JavascriptInterface
	public void show() {
		// toolbar.setVisibility(View.VISIBLE);
	}

	@JavascriptInterface
	public void hide() {
		// toolbar.setVisibility(View.GONE);
	}

	@JavascriptInterface
	public void submit(String result) {
		if (callback != null) {
			callback.onSubmit(result);
		}
	}

	@JavascriptInterface
	public void openKeyboard() {
		new Handler().post(new Runnable() {

			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) webView
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager != null) {
					inputMethodManager.toggleSoftInput(
							InputMethodManager.SHOW_FORCED, 0);
				}

			}
		});

	}

	public void run(final String scriptSrc) {
		webView.post(new Runnable() {

			public void run() {
				webView.loadUrl("javascript:" + scriptSrc);
			}
		});

	}

}
