package in.goflo.laberintoc.View.Activity;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.goflo.laberintoc.Model.LocationDetails;
import in.goflo.laberintoc.R;
import in.goflo.laberintoc.View.Adapter.LocationListAdapter;

import in.goflo.laberintoc.ViewModel.LocationViewModel;


public class MainActivity extends AppCompatActivity {

    static final private String TAG = "MainActivity";
    RecyclerView recyclerView;
    LocationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        LocationViewModel locationViewModel =
                ViewModelProviders.of(this).get(LocationViewModel.class);

        LiveData<List<LocationDetails>> locationLiveData = locationViewModel.getLocationLiveData();
        locationLiveData.observe(this, new Observer<List<LocationDetails>>() {
            @Override
            public void onChanged(@Nullable List<LocationDetails> locationDetails) {
                if(locationDetails != null) {
                    Collections.sort(locationDetails, new LocationComparator());
                    adapter = new LocationListAdapter(getApplicationContext(), locationDetails);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }
        });

    }

    private class LocationComparator implements Comparator<LocationDetails> {

        public int compare(LocationDetails l1, LocationDetails l2) {
            return l1.getLocationName().compareTo(l2.getLocationName());
        }
    }
}
