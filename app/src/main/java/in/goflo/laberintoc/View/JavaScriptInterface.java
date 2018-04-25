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
    Double latitude, longitude;

    public JavaScriptInterface(Context context, String locationID, Double latitude, Double longitude) {
        this.context = context;
        this.locationID = locationID;
        this.latitude = latitude;
        this.longitude = longitude;
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

    @android.webkit.JavascriptInterface
    public String getLatitude() {
        return latitude.toString();
    }

    @android.webkit.JavascriptInterface
    public String getLongitude() {
        return longitude.toString();
    }

}
