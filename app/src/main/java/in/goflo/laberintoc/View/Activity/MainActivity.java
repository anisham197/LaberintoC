package in.goflo.laberintoc.View.Activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.goflo.laberintoc.Model.LocationDetails;
import in.goflo.laberintoc.R;
import in.goflo.laberintoc.View.Adapter.LocationListAdapter;

public class MainActivity extends AppCompatActivity {

    static final private String TAG = "MainActivity";
    RecyclerView recyclerView;
    LocationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        List<LocationDetails> locationDetails = new ArrayList<>();

        adapter = new LocationListAdapter(getApplicationContext(), locationDetails);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}
