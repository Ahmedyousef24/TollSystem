package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Model.Trip;
import tull.application.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    public ArrayList<Trip> getListItem() {
        return new ArrayList<>(listItem);
    }

    private ArrayList<Trip> listItem;
    private Context context;


    // constructer for the Adapter, list and context
    public PaymentAdapter(ArrayList<Trip> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;

    }
    // when we create the adapter we get back a view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trips_tobe_paid,viewGroup,false);

        return new ViewHolder(v);
    }

    // binding the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Trip trip = listItem.get(i);
        SimpleDateFormat dtf = new SimpleDateFormat("d MMM yyyy");

        viewHolder.textMoney.setText((trip.getTicketPrice()) + context.getString(R.string.dollar));
        viewHolder.textDate.setText(dtf.format(trip.getTimeStamp()));
        viewHolder.textToll.setText(trip.getAddress());
    }
    // size of the list
    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textToll;
        public TextView textDate;
        public TextView textMoney;

        public ViewHolder(View itemView) {
            super(itemView);

            textToll = (TextView) itemView.findViewById(R.id.Toll_location);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textMoney = (TextView) itemView.findViewById(R.id.cost);
        }
    }
}
