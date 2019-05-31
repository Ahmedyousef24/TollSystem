package tull.application.Controller;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import Adapters.ScreenSlidePagerAdapter;
import DataBaseLayer.UserData;
import Utils.HomeFragmentUtils;
import tull.application.R;

public class HomeFragment extends Fragment {


    private TextView greetings, date, textViewPayTrips, textViewTopUp;
    public  static TextView textViewBalance;
    private RecyclerView mRecyclerView;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private NotificationManagerCompat notificationManager;
    private int counter;
    private static HomeFragmentUtils utils;
    public static Button payAllButton;

    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        tabLayout = view.findViewById(R.id.trip_tab);
        mPager = view.findViewById(R.id.trip_frame);
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());

        payAllButton = view.findViewById(R.id.buttonPayTrip);

        mPager.setAdapter(pagerAdapter);

        mPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 1){
                    payAllButton.setVisibility(View.GONE);
                } else {
                    payAllButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        date = view.findViewById(R.id.date);
        greetings = view.findViewById(R.id.greetings);

        utils = new HomeFragmentUtils();

        notificationManager = notificationManager.from(getContext());


        textViewBalance = view.findViewById(R.id.textViewBalance);

        getBalance();

        // listener to the adapter

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar c = Calendar.getInstance();
        greetings.setText(utils.getGreetings(c));
        date.setText(utils.getDate());

    }



    private void addBalance() {
        textViewTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewBalance.setText("90.00 kr");
            }
        });
    }

    public void getBalance(){
        UserData.getBalance(new UserData.BalanceCallBack() {
            @Override
            public void onBalanceCallBack(double balance) {
                textViewBalance.setText(Double.toString(balance));
            }
        });

    }




}
