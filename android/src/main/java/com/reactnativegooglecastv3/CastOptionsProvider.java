package com.reactnativegooglecastv3;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

import static com.reactnativegooglecastv3.GoogleCastPackage.APP_ID;

@SuppressWarnings("unused")
public class CastOptionsProvider implements OptionsProvider {
    @Override
    public CastOptions getCastOptions(Context ctx) {
        String appId = GoogleCastPackage.metadata(APP_ID, null, ctx);
        if (appId == null) return null;
        return new CastOptions.Builder()
                .setReceiverApplicationId(appId)
                // .setSupportedNamespaces(Arrays.asList(ctx.getString(R.string.castNamespace)))
                .build();
    }
    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context ctx) {
        return null;
    }
}
