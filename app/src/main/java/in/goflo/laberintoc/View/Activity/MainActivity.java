package in.goflo.laberintoc.View.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import in.goflo.laberintoc.R;
import in.goflo.laberintoc.ViewModel.LocationViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationViewModel locationViewModel =
                ViewModelProviders.of(this).get(LocationViewModel.class);

        LiveData<List<LocationDetails>> locationLiveData = locationViewModel.getLocationLiveData();
        locationLiveData.observe(this, new Observer<List<LocationDetails>>() {
            @Override
            public void onChanged(@Nullable List<LocationDetails> locationDetails) {
                if(locationDetails != null) {
                }
            }
        });

    }
}
