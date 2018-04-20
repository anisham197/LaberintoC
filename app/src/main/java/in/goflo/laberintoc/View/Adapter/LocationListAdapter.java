package in.goflo.laberintoc.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.goflo.laberintoc.Model.LocationDetails;
import in.goflo.laberintoc.R;
import in.goflo.laberintoc.View.Activity.ViewMapActivity;
import in.goflo.laberintoc.View.Activity.WebViewActivity;

/**
 * Created by Anisha Mascarenhas on 06-03-2018.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private List<LocationDetails> list;
    private Context context;

    public LocationListAdapter(Context context, List<LocationDetails> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String locationName =  list.get(position).getLocationName();
        holder.name.setText(locationName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;

        public ViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int position = getLayoutPosition();
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(context.getString(R.string.locationID), list.get(position).getLocationID());
            intent.putExtra(context.getString(R.string.locationName), list.get(position).getLocationName());
            context.startActivity(intent);
        }
    }
}
