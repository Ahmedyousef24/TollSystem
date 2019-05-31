package tull.application.Controller;


import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapters.MapAdapter;
import DataBaseLayer.TripData;
import DataBaseLayer.UserData;
import Model.Trip;
import tull.application.R;

import static Container.Constants.CHANNEL_1_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnPaidTripsFragment extends Fragment {


    private static ArrayList<Trip> mDataList;
    private static ArrayList<String> mIdList;
    private static MapAdapter mapAdapter;
    private RecyclerView mRecyclerView;
    private NotificationManagerCompat notificationManager;


    private static Double userBalance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_unpaidtrips, container, false);

        notificationManager = notificationManager.from(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mapAdapter);

        getBalance();


        HomeFragment.payAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    for (int i = 0; i < mDataList.size() + 1; i++) {

                        if (userBalance >= 30) {

                            userBalance -= 30;
                            TripData.updateTrip(mIdList.get(i));
                            mIdList.remove(i);
                            mDataList.remove(i);
                            mapAdapter.notifyItemRemoved(i);
                            if (userBalance < 30) {
                                notifyOnLowBalance();
                            }
                        } else {
                            notifyOnLowBalance();
                            break;
                        }

                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        mapAdapterListener();

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataList = new ArrayList<>();
        mIdList = new ArrayList<>();
        mapAdapter = new MapAdapter(getContext(), mDataList, "UnPaidTrips");
        updateList();
    }

    public static void updateList() {
        // mDataList.clear();
        // utils.getTripList(mDataList);

        TripData.getUnpaidTrips(new TripData.UnpaidTripsCallBack() {
            @Override
            public void onTripCallBack(ArrayList<Trip> trips, ArrayList<String> idList) {

                Log.d("dbcall","db");
                mDataList.clear();
                mDataList.addAll(trips);
                mIdList.clear();
                mIdList.addAll(idList);
                mapAdapter.notifyDataSetChanged();

            }
        });

    }

    private void getBalance() {
        UserData.getBalance(new UserData.BalanceCallBack() {
            @Override
            public void onBalanceCallBack(double balance) {
                userBalance = balance;
                updateBalance(balance);
            }
        });
    }

    private void updateBalance(double balance) {
        HomeFragment.textViewBalance.setText("$" + balance);
    }

    private void mapAdapterListener() {
        mapAdapter.itemClicked(new MapAdapter.ClickListener() {
            @Override
            public void onTextViewClicked(final int position) {


                if (userBalance >= 30) {

                    userBalance -= 30;
                    UserData.updateBalance(userBalance);
                    updateBalance(userBalance);
                    mDataList.remove(position);
                    TripData.updateTrip(mIdList.get(position));
                    mIdList.remove(position);
                    mapAdapter.notifyItemRemoved(position);
                    if (userBalance < 30) {
                        notifyOnLowBalance();
                    }
                } else {
                    notifyOnLowBalance();
                }



            }
        });
    }

    private void notifyOnLowBalance() {
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_account_balance)
                .setContentTitle("Low Balance")
                .setContentText("Your balance is low.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        notificationManager.notify(1, notification);

    }

    public static Double getUserBalance() {
        return userBalance;
    }


}
