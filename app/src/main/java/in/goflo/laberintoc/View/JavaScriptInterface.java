package in.goflo.laberintoc.View;

import android.content.Context;
import android.util.Log;

import in.goflo.laberintoc.Helper.AuthManager;

/**
 * Created by Anisha Mascarenhas on 20-04-2018.
 */

public class JavaScriptInterface {

    Context context;
    String locationID;

    public JavaScriptInterface(Context context, String locationID) {
        this.context = context;
        this.locationID = locationID;
    }


    @android.webkit.JavascriptInterface
    public String getLocationId() {
        return locationID;
    }

    @android.webkit.JavascriptInterface
    public String getUserId() {
        Log.d("JavaScriptInterface", AuthManager.getUid(context));
        return AuthManager.getUid(context);
    }

    //TODO: define lat lng of location
    @android.webkit.JavascriptInterface
    public void getLocationLatLng() {
        return;
    }

}
