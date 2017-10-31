package com.yue.jsbridge.webview;

import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * @ClassName:InjectedWebChromClient
 * @auther:shimy
 * @date:2017/10/31 0031 上午 10:11
 * @description: 利用反射执行本地方法 并result.confirm给html返回结果
 * yue://class:callback/method?jsondata
 */
public class InjectedWebChromeClient extends WebChromeClient {


    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        //执行反射调用本地方法
        result.confirm("");
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

}
