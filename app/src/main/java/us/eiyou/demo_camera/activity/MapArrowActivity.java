package us.eiyou.demo_camera.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.GetBitmapFromUrl;
import us.eiyou.demo_camera.utils.SP;

public class MapArrowActivity extends AppCompatActivity {

    @Bind(R.id.wv)
    WebView wv;
    @Bind(R.id.ll)
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_arrow);
        ButterKnife.bind(this);

        for (int i = 0; i < getIntent().getStringExtra("photoPaths").split(",").length; i++) {
            ImageView imageView=new ImageView(getApplicationContext());
            try {
                imageView.setImageBitmap(GetBitmapFromUrl.getBitmap(getIntent().getStringExtra("photoPaths").split(",")[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            imageView.setPadding(0,22,0,0);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll.setVisibility(View.GONE);
                    wv.loadUrl(Config.urlHead+"uploadfile/ImagesUploaded/"+SP.getString(getApplicationContext(),"telephone")
                            +"_3/"+getIntent().getStringExtra("para").split("_")[1]+"/vtour/tour_editor.jsp?startscene="+(finalI)
                            +"&path="+getIntent().getStringExtra("para").split("_")[1]+"&telphone="
                            +SP.getString(getApplicationContext(),"telephone"));
                    wv.setVisibility(View.VISIBLE);
                }
            });
            ll.addView(imageView);
        }

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
            try {
                wv.removeJavascriptInterface("searchBoxJavaBridge_");
                wv.removeJavascriptInterface("accessibility");
                wv.removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
        webSettings.setSavePassword(false);
        wv.loadUrl(Config.urlHead+"uploadfile/ImagesUploaded/"
                + SP.getString(getApplicationContext(), "telephone")
                + "_3/" + getIntent().getStringExtra("para").split("_")[1] + "/vtour/tour_editor.jsp?telphone="
                + SP.getString(getApplicationContext(), "telephone") + "&path="
                + getIntent().getStringExtra("para").split("_")[1]);
        Log.d("MapArrowActivity", wv.getUrl());
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(wv.getVisibility()==View.VISIBLE){
            wv.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
        }else {
            finish();
        }
    }
}
