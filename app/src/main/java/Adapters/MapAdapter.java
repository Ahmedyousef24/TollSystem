package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Model.Trip;
import tull.application.R;


public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Trip> mDataList;
    private String TAG;
    private ClickListener listner;

    public MapAdapter(Context context, ArrayList<Trip> dataList,String tag){
        this.context = context;
        this.mDataList = dataList;
        this.TAG = tag;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       return new ViewHolder( LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tripsdetails,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if( viewHolder == null){
            return;
        }
        viewHolder.bindView(position);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void itemClicked(ClickListener clickListener){
        listner = clickListener;
    }


   public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        public GoogleMap map;
        View layout;
        private MapView mapView;
        private TextView  textViewDate, textViewAddress, textViewTime, textViewDue;
        private Button buttonPayTrip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            mapView = itemView.findViewById(R.id.mapView);
            textViewAddress = itemView.findViewById(R.id.textViewPlace);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDue = itemView.findViewById(R.id.textViewDue);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewDue = itemView.findViewById(R.id.textViewDue);
            buttonPayTrip = itemView.findViewById(R.id.payButton);

            if(mapView != null){
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
                mapView.onEnterAmbient(null);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context.getApplicationContext());
            map = googleMap;
            setMapLocation();

        }

        private void bindView(int position) {
            Trip item = mDataList.get(position);
            // Store a reference of the ViewHolder object in the layout.
            layout.setTag(this);
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.

           LatLng latLng = new LatLng(item.getLatitude(),item.getLongitude());
            mapView.setTag(latLng);
            setMapLocation();
            textViewAddress.setText(item.getAddress());
            SimpleDateFormat dtf = new SimpleDateFormat("d MMM yyyy");
            SimpleDateFormat dtfT = new SimpleDateFormat("hh:mm aaa");
            textViewDate.setText(dtf.format(item.getTimeStamp()));
            textViewTime.setText(dtfT.format(item.getTimeStamp()));
            textViewDue.setText("DUE "+item.getDueDate());

            if(TAG == "PaidTrips"){
                buttonPayTrip.setVisibility(View.GONE);
            }


            buttonPayTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner != null){
                        listner.onTextViewClicked(getAdapterPosition());
                    }
                }
            });


       }
       private void setMapLocation() {
           if (map == null) return;

           LatLng data = (LatLng) mapView.getTag();
           if (data == null) return;

           // Add a marker for this item and set the camera
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(data, 13f));
           map.addMarker(new MarkerOptions().position(data));

           // Set the map type back to normal.
           map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       }



   }
    public interface ClickListener {
        void onTextViewClicked(int position);
    }
}
