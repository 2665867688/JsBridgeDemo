package com.yue.jsbridge.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.yue.jsbridge.R;
import com.yue.jsbridge.webview01.JSBridge;
import com.yue.jsbridge.webview01.JSLogical;
import com.yue.jsbridge.webview01.JsCallJava;

/**
 * @ClassName:SimpleTest01Activity
 * @auther:shimy
 * @date:2017/10/31 0031 上午 10:16
 * @description: 开始封装 （java反射 依赖注入）
 * 本次封装html js和navite的交互协议是
 * hybrid://JSBridge:1538351/method?{“message”:”msg”}
 */
public class SimpleTest01Activity extends AppCompatActivity {

    private WebView mWebView;
    private TextView mTvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_test01);
        initView();
    }

    private void initView() {
        //添加时请在JsCallJava前声明
        JSBridge.getInstance();
        mWebView = findViewById(R.id.webview_simple01_test);
        mWebView.setWebChromeClient(new CustomWebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/index01.html");

        mTvShow = findViewById(R.id.tv_simple01_show);
    }


    class CustomWebChromeClient extends WebChromeClient {

        private JsCallJava mJsCallJava;
        private boolean mIsInjectedJS;

        public CustomWebChromeClient() {
            mJsCallJava = new JsCallJava();
        }


        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            //为什么要在这里注入JS
            //1 OnPageStarted中注入有可能全局注入不成功，导致页面脚本上所有接口任何时候都不可用
            //2 OnPageFinished中注入，虽然最后都会全局注入成功，但是完成时间有可能太晚，当页面在初始化调用接口函数时会等待时间过长
            //3 在进度变化时注入，刚好可以在上面两个问题中得到一个折中处理
            //为什么是进度大于25%才进行注入，因为从测试看来只有进度大于这个数字页面才真正得到框架刷新加载，保证100%注入成功
//            if (newProgress <= 25) {
//                mIsInjectedJS = false;
//            } else if (!mIsInjectedJS) {
//                webView.loadUrl(mJsCallJava.getPreloadInterfaceJS());
//                mIsInjectedJS = true;
//
//            }
            super.onProgressChanged(webView, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            mTvShow.setText(url + message);
            return false;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Toast.makeText(view.getContext(), "android onJsConfirm", Toast.LENGTH_SHORT).show();
            mTvShow.setText(url + message);
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            mTvShow.setText("url:" + url + "\nmessage：" + message + "\ndefaultValue:" + defaultValue);
            result.confirm(mJsCallJava.call(view, message));
            mTvShow.setText(mJsCallJava.call(view, message));
            return true;
        }
    }
}
