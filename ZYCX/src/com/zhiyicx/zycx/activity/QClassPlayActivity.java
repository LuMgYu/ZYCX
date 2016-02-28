package com.zhiyicx.zycx.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import qcjlibrary.util.L;

import com.zhiyicx.zycx.util.Utils;

/**
 * Created by Administrator on 2015/1/2.
 */
public class QClassPlayActivity extends Activity {

    private WebView mPlayView = null;

    final static String mHtmlData =
            "<div id=\"youkuplayer\" style=\"width:%s;height:%s\"></div>\n" +
            "        <script type=\"text/javascript\" src=\"http://player.youku.com/jsapi\">\n" +
            "        player = new YKU.Player('youkuplayer',{\n" +
            "        styleid: '1',\n" +
            "        client_id: 'bf20bc0aca083ec6',\n" +
            "        vid: '%s',\n" +
            "        autoplay: true,\n" +
            "        show_related: true,\n" +
            "        embsig: '%s'\n" +
            "        });\n" +
            "        </script>\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPlayView = new WebView(this);
        setContentView(mPlayView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mPlayView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mPlayView.setPadding(0,0,0,0);
        mPlayView.setWebChromeClient(new WebChromeClient());
        WebSettings setting = mPlayView.getSettings();
        setting.setPluginState(PluginState.ON);//支持插件
        setting.setLoadWithOverviewMode(true); //自适应屏幕
        setting.getUseWideViewPort();//可任意比例缩放
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);//自动打开窗口 
        /*String vid = getIntent().getStringExtra("vid");
        String stamp = String.valueOf(System.currentTimeMillis() / 1000);
        String secert = "d34ce7d56040bc1b0cfc2cc153901cb0";
        String SIGNATURE = "";
        try {
            SIGNATURE = MD5.encryptMD5(vid + '_' + stamp + '_' + secert);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        String embsig = "1_" + stamp + '_' + SIGNATURE;

        if(TextUtils.isEmpty(vid))
            finish();
        String data = String.format(mHtmlData,"100%","100%", vid, embsig);*/
        String url = getIntent().getStringExtra("vurl");
        mPlayView.loadUrl(url + Utils.getTokenString(this));
    }

    @Override
    protected void onResume() {
    	// TODO 
    	super.onResume();
    	if(mPlayView != null){
			try {
				mPlayView.getClass().getMethod("onResume").invoke(mPlayView,(Object[])null);
			} catch (Exception e) {
				L.d("继续播放" + e.toString());
			} 
		}
    }
    
    @Override
    protected void onPause() {
    	// TODO 当退出页面时停止播放
    	super.onPause();
    	if(mPlayView != null){
    		try {
        		mPlayView.getClass().getMethod("onPause").invoke(mPlayView, (Object[]) null);
        		mPlayView.stopLoading();
    		} catch (Exception e) {
    			L.d("停止播放" + e.toString());
    		}
		}
    	
    }
}
