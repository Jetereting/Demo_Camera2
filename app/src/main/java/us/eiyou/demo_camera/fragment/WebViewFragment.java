package us.eiyou.demo_camera.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import butterknife.Bind;
import butterknife.ButterKnife;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.SP;

//已经没用
public class WebViewFragment extends Fragment {


    WebView webView;
    @Bind(R.id.b_share)
    BootstrapCircleThumbnail bShare;

    private DisplayMetrics displayMetrics;
    private float lastX=0;
    private float lastY=0;
    private int screenWidth=0;
    private int screenHeight=0;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private boolean isFirst=true;
    private boolean isMove=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) view.findViewById(R.id.wv);
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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.loadUrl("http://115.159.41.95/VR/index.html?telephone=" + SP.getString(getActivity(), "telephone"));
        view.findViewById(R.id.b_share).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isFirst) {
                    // 得到屏幕的宽
                    displayMetrics = getResources().getDisplayMetrics();
                    screenWidth = displayMetrics.widthPixels;
                    // 得到标题栏和状态栏的高度
                    Rect rect = new Rect();
                    Window window = getActivity().getWindow();
                    view.findViewById(R.id.b_share).getWindowVisibleDisplayFrame(rect);
                    int statusBarHeight = rect.top;
                    int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                    int titleBarHeight = contentViewTop - statusBarHeight;
                    // 得到屏幕的高
                    screenHeight = displayMetrics.heightPixels- (statusBarHeight + titleBarHeight);
                    isFirst=false;
                }
                int action=event.getAction();
                switch (action) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        //按下处坐标
                        lastX=event.getRawX();
                        lastY=event.getRawY();
                        isMove=false;
                        break;
                    //移动
                    case MotionEvent.ACTION_MOVE:
                        //移动的距离
                        float distanceX=event.getRawX()-lastX;
                        float distanceY=event.getRawY()-lastY;
                        Log.d("WebViewFragment", "distanceY:" + distanceY);
                        Log.d("WebViewFragment", "distanceX:" + distanceX);
                        if(0.7<-distanceY||distanceY>0.7||0.7<-distanceX||distanceX>0.7){
                            isMove=true;
                        }
                        //移动后控件的坐标
                        left=(int)(v.getLeft()+distanceX);
                        top=(int)(v.getTop()+distanceY);
                        right=(int)(v.getRight()+distanceX);
                        bottom=(int)(v.getBottom()+distanceY);
                        //处理拖出屏幕的情况
                        if (left<0) {
                            left=0;
                            right=v.getWidth();
                        }
                        if (right>screenWidth) {
                            right=screenWidth;
                            left=screenWidth-v.getWidth();
                        }
                        if (top<0) {
                            top=0;
                            bottom=v.getHeight();
                        }
                        if (bottom>screenHeight) {
                            bottom=screenHeight;
                            top=screenHeight-v.getHeight();
                        }
                        //显示图片
                        v.layout(left, top, right, bottom);
                        lastX=event.getRawX();
                        lastY=event.getRawY();
                        break;
                    //抬起
                    case MotionEvent.ACTION_UP:
                        if(!isMove) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, Config.urlHeadDomainName+"VR/index.html?telephone=" + SP.getString(getActivity(), "telephone"));
                            shareIntent.setType("text/plain");
                            startActivity(Intent.createChooser(shareIntent, "分享到"));
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
//
//    @OnClick(R.id.b_share)
//    public void onClick() {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://115.159.41.95/VR/index.html?telephone=" + SP.getString(getActivity(), "telephone"));
//        shareIntent.setType("text/plain");
//        startActivity(Intent.createChooser(shareIntent, "分享到"));
//    }
}
