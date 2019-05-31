package tull.application.Controller;


import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Locale;

import Adapters.MapAdapter;
import DataBaseLayer.TripData;
import Model.Trip;
import Utils.HomeFragmentUtils;
import tull.application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaidTripsFragment extends Fragment {


    private TextView greetings, date, textViewPayTrips, textViewBalance, textViewTopUp;
    private RecyclerView mRecyclerView;
    private static ArrayList<Trip> mDataList;
    private static ArrayList<String> mIdList;
    private static MapAdapter mapAdapter;
    private NotificationManagerCompat notificationManager;
    private int counter;
    private static HomeFragmentUtils utils;

    public PaidTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paidtrips, container, false);


        mDataList = new ArrayList<>();
        mIdList = new ArrayList<>();

        counter = 0;


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        utils = new HomeFragmentUtils();


        mapAdapter = new MapAdapter(getContext(), mDataList, "PaidTrips");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mapAdapter);
        updateList();


        return view;
    }


    public static void updateList() {
        // mDataList.clear();
        // utils.getTripList(mDataList);

        TripData.getPaidTrips(new TripData.PaidTripsCallBack() {
            @Override
            public void OnPaidTripCallback(ArrayList<Trip> trips) {
                mDataList.clear();
                mDataList.addAll(trips);
                mapAdapter.notifyDataSetChanged();
            }

        });

    }


    private RecyclerView.RecyclerListener mRecycleListener = new RecyclerView.RecyclerListener() {
        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            MapAdapter.ViewHolder mapHolder = (MapAdapter.ViewHolder) holder;
            if (mapHolder != null && mapHolder.map != null) {
                // Clear the map and free up resources by changing the map type to none.
                // Also reset the map when it gets reattached to layout, so the previous map would
                // not be displayed.
                mapHolder.map.clear();
                mapHolder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
        }
    };
}
