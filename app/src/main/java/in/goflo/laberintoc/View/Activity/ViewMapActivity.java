package in.goflo.laberintoc.View.Activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.pwittchen.reactivewifi.AccessRequester;
import com.github.pwittchen.reactivewifi.ReactiveWifi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.goflo.laberintoc.Helper.AuthManager;
import in.goflo.laberintoc.Helper.PermissionManager;
import in.goflo.laberintoc.R;
import in.goflo.laberintoc.View.JavaScriptInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ViewMapActivity extends AppCompatActivity {

    private static final int REQUEST_MULTIPLE_PERMISSIONS = 1;
    private static final String PERMISSION_MSG = "Location Services Permission required for this app";
    private static final String TAG = "ViewMapActivity";

    private JSONArray finalFingerprint;

    private String UID;
    private String location = "xxxxx";
    private String groupLocationID;

    RequestQueue queue;
    final static String url = "http://vendor.maps.goflo.in/trackLocation";

    private Disposable wifiSubscription;

    private Handler handler;
    private Runnable getLocationRunnable;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        groupLocationID = getIntent().getStringExtra(getString(R.string.locationID));
        UID = AuthManager.getUid(this);

        handler = new Handler();
        webView = findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/map.html");
        webView.addJavascriptInterface(new JavaScriptInterface(this, groupLocationID), "Android");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionManager.checkAndRequestPermissions(this)) {
            Log.d(TAG, "On Resume called, reading Wifi fingerprints");
            readFingerprints();
            requestCurrentLocation();
        }
    }

    @Override protected void onPause() {
        super.onPause();
        safelyUnsubscribe(wifiSubscription);
        handler.removeCallbacks(getLocationRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        safelyUnsubscribe(wifiSubscription);
        handler.removeCallbacks(getLocationRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        safelyUnsubscribe(wifiSubscription);
        handler.removeCallbacks(getLocationRunnable);
    }

    private void requestCurrentLocation() {
        getLocationRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Runnable called", Toast.LENGTH_SHORT).show();
                if (finalFingerprint != null) {
                    JSONObject requestJSON = createRequestJson();
                    if (requestJSON != null) {
                        // trackLocation(requestJSON);
                    }
                }
                handler.postDelayed( this , 5000);
                Log.d(TAG, "Inner handler called");
            }
        };
        handler.post( getLocationRunnable );
        Log.d(TAG, "Outer handler called");
    }


    private void readFingerprints(){
        Log.d(TAG, "Read Fingerprints called");
        boolean fineLocationPermissionNotGranted =
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermissionNotGranted =
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (fineLocationPermissionNotGranted && coarseLocationPermissionNotGranted) {
            return;
        }

        if (!AccessRequester.isLocationEnabled(this)) {
            AccessRequester.requestLocationAccess(this);
            return;
        }
        try {
            wifiSubscription = ReactiveWifi.observeWifiAccessPoints(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ScanResult>>() {
                        @Override
                        public void accept(List<ScanResult> results) throws Exception {
                            JSONArray fingerprintArray = new JSONArray();
                            for (ScanResult result : results) {
                                JSONObject temp = new JSONObject();
                                temp.put("mac",result.BSSID);
                                temp.put("rssi",result.level);
                                fingerprintArray.put(temp);
                            }
                            finalFingerprint =  fingerprintArray;
                        }
                    });
        }catch (SecurityException e){
            Log.d(TAG, e.getMessage());
        }
    }


    private JSONObject createRequestJson(){
        JSONObject requestJSON = new JSONObject();
        long timestamp = System.currentTimeMillis();
        try {
            requestJSON.put("group", groupLocationID);
            requestJSON.put("username", UID);
            requestJSON.put("location", location);
            requestJSON.put("time", timestamp);
            requestJSON.put("wifi-fingerprint", finalFingerprint);
            Log.d(TAG, "json "  + requestJSON);
            return requestJSON;
        }
        catch (JSONException e){
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

    private void trackLocation(JSONObject requestJSON) {
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request_json = new JsonObjectRequest(url, requestJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(request_json);
    }

    private void safelyUnsubscribe(Disposable... subscriptions) {
        for (Disposable subscription : subscriptions) {
            if (subscription != null && ! subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        if ( requestCode == REQUEST_MULTIPLE_PERMISSIONS && grantResults.length > 0 ) {
            boolean allGranted = true;
            for (int i = 0; i < permissions.length; i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    allGranted = false;
            }
            if (allGranted) {
                Log.d(TAG, "Location services permission granted");
                readFingerprints();
            }
            else {
                Log.d(TAG, "Some permissions are not granted ask again ");
                /* shouldShowRequestPermissionRationale(). This method returns true if
                the app has requested this permission previously and the user denied the request.
                If the user turned down the permission request in the past and chose the Don't ask again option
                in the permission request system dialog, this method returns false. The method also returns
                false if a device policy prohibits the app from having that permission*/

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION) ) {
                    showDialog(PERMISSION_MSG, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int choice) {
                            switch (choice) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    PermissionManager.checkAndRequestPermissions(ViewMapActivity.this);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    String msg = "Fingerprints cannot be obtained till Location Permissions are enabled";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                    finish();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                    // TODO: Navigate to correct activity
                }
            }
        }
    }

    private void showDialog(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", listener)
                .create()
                .show();
    }


}
