package tull.application.Controller;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.PaymentAdapter;
import DataBaseLayer.TripData;
import DataBaseLayer.UserData;
import Model.Trip;
import tull.application.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {


    private static PaymentAdapter paymentAdapter;
    private static ArrayList<Trip> tripArrayList;
    private RecyclerView recyclerView;
    public static TextView balance;
    private FloatingActionButton floatingActionButton;
    private static double money;
    public static CardView cardView;
    private static Double userBalance;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        floatingActionButton = view.findViewById(R.id.floatButton);
        balance = view.findViewById(R.id.textViewBalance);

        cardView = view.findViewById(R.id.card_view_payment);
        tripArrayList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(tripArrayList, getContext());

        updateList();
        getBalance();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(paymentAdapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoneyPicker picker = new MoneyPicker();
                picker.show(getFragmentManager(), "let");
            }
        });
        setVisible();

        return view;


    }


    private static void setVisible() {
        try {
            if (tripArrayList.isEmpty()) {
                cardView.setVisibility(View.GONE);
            } else {
                cardView.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }




    public static void updateBalance(){

        balance.setText("$"+userBalance);

    }

    public static void updateList() {


        TripData.getPaidTrips(new TripData.PaidTripsCallBack() {
            @Override
            public void OnPaidTripCallback(ArrayList<Trip> trips) {

                tripArrayList.clear();
                tripArrayList.addAll(trips);
                paymentAdapter.notifyDataSetChanged();
                setVisible();
            }

        });

    }

    private void getBalance() {
        UserData.getBalance(new UserData.BalanceCallBack() {
            @Override
            public void onBalanceCallBack(double balance) {
                userBalance = balance;
                updateBalance();
            }
        });
    }

}
