package com.reactnativegooglecastv3;

import android.content.Context;
import android.content.pm.PackageManager;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

public class GoogleCastPackage implements ReactPackage {
    static final String APP_ID = "com.reactnativegooglecastv3.castAppId";
    static final String NAMESPACE = "com.reactnativegooglecastv3.castNamespace";

    static String metadata(String key, String defaultValue, Context ctx) {
        try {
            return ctx.getPackageManager()
                    .getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString(key, defaultValue);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        return Arrays.<NativeModule>asList(new CastModule(reactApplicationContext));
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Arrays.<ViewManager>asList(new CastButtonManager());
    }
}
