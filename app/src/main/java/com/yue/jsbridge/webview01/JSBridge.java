package com.yue.jsbridge.webview01;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

/**
 * @ClassName:JSBridge
 * @auther:shimy
 * @date:2017/10/31 0031 下午 3:13
 * @description:
 */
public class JSBridge {

    public static final String BRIDGE_NAME = "JSBridge";

    private static JSBridge INSTANCE = new JSBridge();

    private boolean isEnable=true;

    private ArrayMap<String, Class<? extends IInject>> mClassMap = new ArrayMap<>();

    private JSBridge() {
        //添加一个调用类 注意map的key值 ，正式使用时请删除这儿的注册，在需要的代码里通过addInjectPair方法进行注册方法接口类
        mClassMap.put(BRIDGE_NAME, JSLogical.class);
    }

    public static JSBridge getInstance() {
        return INSTANCE;
    }

    public boolean addInjectPair(String name, Class<? extends IInject> clazz) {
        if (!mClassMap.containsKey(name)) {
            mClassMap.put(name, clazz);
            return true;
        }
        return false;
    }

    public boolean removeInjectPair(String name,Class<? extends IInject> clazz) {
        if (TextUtils.equals(name,BRIDGE_NAME)) {
            return false;
        }
        Class clazzValue=mClassMap.get(name);
        if(null!=clazzValue && (clazzValue == clazz)){
            mClassMap.remove(name);
            return true;
        }
        return false;

    }


    public ArrayMap<String, Class<? extends IInject>> getInjectPair() {
        return mClassMap;
    }
}
