package us.eiyou.demo_camera.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import us.eiyou.demo_camera.R;

public class ShowShareActivity extends Activity {

	@Bind(R.id.p)
	ProgressBar p;
	WebView webView;
	ImageView iv_share;
	EditText et_url;
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("ShowShareActivity","onCreate");
		setContentView(R.layout.activity_show_share);
		ButterKnife.bind(this);

		webView=(WebView)findViewById(R.id.webView);
		Intent intent=getIntent();
		url=intent.getStringExtra("url");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
			try {
				webView.removeJavascriptInterface("searchBoxJavaBridge_");
				webView.removeJavascriptInterface("accessibility");
				webView.removeJavascriptInterface("accessibilityTraversal");
			} catch (Throwable tr) {
				tr.printStackTrace();
			}
		}
		webSettings.setSavePassword(false);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				p.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				p.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				p.setProgress(newProgress);
			}
		});
		webView.loadUrl(url);

		iv_share=(ImageView)findViewById(R.id.iv_share);
		iv_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				shareText(url);
			}
		});
		iv_share.getBackground().setAlpha(222);
		
		et_url=(EditText)findViewById(R.id.et_url);
		et_url.setText(url);
	}

    public void shareText(String s) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, s);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
