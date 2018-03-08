package in.goflo.laberintoc.View.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import in.goflo.laberintoc.Helper.PermissionManager;
import in.goflo.laberintoc.Model.RoomDetails;
import in.goflo.laberintoc.R;
import in.goflo.laberintoc.ViewModel.BuildingViewModel;
import in.goflo.laberintoc.ViewModel.RoomViewModel;
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

    private JSONArray fingerprintArray;
    private JSONObject requestJSON;

    private TextView locationTextView, responseTextView;
    private Button trackButton;

    private String UID = "xxxx";
    private String location = "xxxxx";
    private String group;

    private String buildingID, buildingName, roomID, roomName;

    RequestQueue queue;
    final static String url = "http://maps.goflo.in/track";

    private Disposable wifiSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        locationTextView = (TextView) findViewById(R.id.textview_location);
        responseTextView = (TextView) findViewById(R.id.textView_response);
        trackButton = (Button)findViewById(R.id.button_track);

        fingerprintArray = new JSONArray();
        group = getIntent().getStringExtra(getString(R.string.locationID));

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackLocation();
            }
        });
    }

    private void trackLocation() {
        locationTextView.setText("Location");
        responseTextView.setText("Response");
        if(PermissionManager.checkAndRequestPermissions(this)) {
            readFingerprints();
        }
    }

    private void readFingerprints(){
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
                            Log.d(TAG, "results " + results);
                            safelyUnsubscribe(wifiSubscription);
                            for (ScanResult result : results) {
                                JSONObject temp = new JSONObject();
                                temp.put("mac",result.BSSID);
                                temp.put("rssi",result.level);
                                fingerprintArray.put(temp);
                            }
                            createRequestJson();
                            getResult();
                        }
                    });
        }catch (SecurityException e){
            Log.d(TAG, e.getMessage());
        }
    }

    private void createRequestJson() throws Exception{
        requestJSON = new JSONObject();
        long timestamp = System.currentTimeMillis();

        requestJSON.put("group", group);
        requestJSON.put("username", UID);
        requestJSON.put("location", location);
        requestJSON.put("time", timestamp);
        requestJSON.put("wifi-fingerprint", fingerprintArray);
        Log.d(TAG, "json "  + requestJSON);
    }

    private void getResult() {
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request_json = new JsonObjectRequest(url, requestJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,response.toString());
                        responseTextView.setText(response.toString());
                        try {
                            roomID = response.get("location").toString();
                            getRoomData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(request_json);
    }

    private void getRoomData() {
        RoomViewModel roomViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        roomViewModel.setRoomID(roomID);
        LiveData<RoomDetails> roomLiveData = roomViewModel.getRoomLiveData();
        roomLiveData.observe(this, new Observer<RoomDetails>() {
            @Override
            public void onChanged(@Nullable RoomDetails roomDetails) {
                if(roomDetails != null) {
                    roomName = roomDetails.getRoomName();
                    buildingID = roomDetails.getBuildingID();
                    getBuildingData();
                }
            }
        });
    }

    private void getBuildingData() {
        BuildingViewModel buildingViewModel = ViewModelProviders.of(this).get(BuildingViewModel.class);
        buildingViewModel.setBuildingID(buildingID);
        LiveData<String> buildingLiveData = buildingViewModel.getBuildingLiveData();
        buildingLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                buildingName = s;
                Log.d(TAG, "building name "  + buildingName);
                locationTextView.setText("You are in " + roomName + " in " + buildingName);
            }
        });

    }

    private void safelyUnsubscribe(Disposable... subscriptions) {
        for (Disposable subscription : subscriptions) {
            if (subscription != null && ! subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override protected void onPause() {
        super.onPause();
        safelyUnsubscribe(wifiSubscription);
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
