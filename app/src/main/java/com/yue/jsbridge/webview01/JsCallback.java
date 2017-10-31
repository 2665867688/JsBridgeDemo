package com.yue.jsbridge.webview01;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tencent.smtt.sdk.WebView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * @ClassName:JsCallback
 * @date:2017/10/31 0031 下午 3:17
 * @description: 回调
 */

public class JsCallback {

    private static final String CALLBACK_JS_FORMAT = "javascript:JsBridge.onComplete('%s', %s);";
    private boolean mCouldGoOn;
    private WeakReference<WebView> mWebViewRef;
    private Handler mHandler;
    private String mSid;

    public JsCallback (WebView view, String mSid) {
        mCouldGoOn = true;
        mWebViewRef = new WeakReference<WebView>(view);
        mHandler = new Handler(Looper.getMainLooper());
        this.mSid = mSid;
    }


    public void apply(boolean isSuccess, String message, JSONObject object) throws JsCallbackException {
        if (mWebViewRef.get() == null) {
            throw new JsCallbackException("the WebView related to the JsCallback has been recycled");
        }
        if (!mCouldGoOn) {
            throw new JsCallbackException("the JsCallback isn't permanent,cannot be called more than once");
        }
        JSONObject result = new JSONObject();

        try {
            JSONObject code=new JSONObject();
            code.put("code", isSuccess ? 0 : 1);
            if(!isSuccess && !TextUtils.isEmpty(message)){
                code.putOpt("msg",message);
            }
            if(isSuccess){
                code.putOpt("msg", TextUtils.isEmpty(message)?"SUCCESS":message);
            }
            result.putOpt("status", code);
            if(null!=object){
                result.putOpt("data",object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String jsFunc = String.format(CALLBACK_JS_FORMAT, mSid, String.valueOf(result));

        if (mWebViewRef != null && mWebViewRef.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebViewRef.get().loadUrl(jsFunc);
                }
            });

        }
    }

    public static class JsCallbackException extends Exception {
        public JsCallbackException (String msg) {
            super(msg);
        }
    }
}
