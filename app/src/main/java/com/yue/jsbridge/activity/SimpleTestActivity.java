package com.yue.jsbridge.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.yue.jsbridge.R;

/**
 * @ClassName:SimpleTestActivity
 * @auther:shimy
 * @date:2017/10/30 0030 下午 5:10
 * @description: 测试一下 原理研究
 * <p>
 * 交互逻辑 核心：解析 由html发给onJsPrompt方法的参数，通过java反射调用本地方法，回调html亦是如此
 *
 * html执行js 的prompt方法--->调起WebChromeClient的onJsPrompt方法(由js传的参数再方法内)--->
 * 执行JsPromptResult.confirm()返回给html的prompt方法结果
 */
public class SimpleTestActivity extends AppCompatActivity {

    private WebView mWebView;
    private TextView mTvShow;
    private Button mBtnACallJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_test);
        initView();
    }

    private void initView() {
        mWebView = findViewById(R.id.webview_simple_test);
        mWebView.setWebChromeClient(new TestWebClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/wwconjstest.html");

        mTvShow = findViewById(R.id.tv_simple_show);
        findViewById(R.id.btn_simple_acalljs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:callJS()");
            }
        });
    }


    class TestWebClient extends com.tencent.smtt.sdk.WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast.makeText(view.getContext(), "android onJsAlert", Toast.LENGTH_SHORT).show();
            mTvShow.setText(url + message);
            result.cancel();
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Toast.makeText(view.getContext(), "android onJsConfirm", Toast.LENGTH_SHORT).show();
            mTvShow.setText(url + message);
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            int i = 0;
            i++;
            Toast.makeText(view.getContext(), "android onjsprompt", Toast.LENGTH_SHORT).show();
            mTvShow.setText("url" + url + "\nmessage:" + message + "\ndefaultvalue:" + defaultValue + i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 利用mJsCallJava反射执行本地方法并执行result.confirm回调html返回给html界面结果
                     * html中prompt的返回结果，这样进行返回
                     */
                    result.confirm("滚滚长江东逝水");
                }
            }, 2000);
            return true;
        }
    }
}



