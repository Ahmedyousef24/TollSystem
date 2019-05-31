package DataBaseLayer;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Model.Trip;
import tull.application.Controller.UnPaidTripsFragment;

public class TripData {

    private static DatabaseReference mDatabase;
    private static  final String TAG = "TRIPLOG";
    private static FirebaseAuth auth;


    public static void saveTrip(Trip trip){
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("trips").child(Objects.requireNonNull(auth.getUid())).push();

        Map<String,Object> updates = new HashMap<String,Object>();
        try{
            updates.put("longitude",trip.getLongitude());
            updates.put("latitude",trip.getLatitude());
            updates.put("ticketPrice",trip.getTicketPrice());
            updates.put("timeStamp",trip.getTimeStamp());
            updates.put("dueDate",trip.getDueDate());
            updates.put("address",trip.getAddress());
            updates.put("isPaid",trip.getPaid());

            mDatabase.setValue(updates);


            UnPaidTripsFragment.updateList();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (DatabaseException e){
            e.printStackTrace();
        }
    }



    public static void getTrip(final TripCallBack callBack){

       mDatabase = FirebaseDatabase.getInstance().getReference();
       auth = FirebaseAuth.getInstance();

       Query query = mDatabase.child("trips").child(Objects.requireNonNull(auth.getUid()));

       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             ArrayList<Trip> tempList = new ArrayList<>();
             ArrayList<String> keyList = new ArrayList<>();
               Log.d("type1", ""+ dataSnapshot.child("longitude").getValue());

               try {

                   for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       String key = snapshot.getKey();
                       double longitude = snapshot.child("longitude").getValue(double.class);
                       double latitude = snapshot.child("latitude").getValue(double.class);
                       Date timeStamp = snapshot.child("timeStamp").getValue(Date.class);
                       String dueDate = snapshot.child("dueDate").getValue(String.class);
                       String ticketPrice = snapshot.child("ticketPrice").getValue(String.class);
                       String address = snapshot.child("address").getValue(String.class);
                       Boolean isPaid = snapshot.child("isPaid").getValue(Boolean.class);

                       keyList.add(key);
                       tempList.add(new Trip(longitude, latitude, ticketPrice, address, dueDate, timeStamp, isPaid));
                   }

               }catch (NullPointerException e){
                   e.printStackTrace();
               }


             callBack.onTripCallBack(tempList,keyList);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

   }

   public static void updateTrip(String id){
       mDatabase = FirebaseDatabase.getInstance().getReference();
       auth = FirebaseAuth.getInstance();
     mDatabase.child("trips").child(Objects.requireNonNull(auth.getUid())).child(id).child("isPaid").setValue(true);


   }

   public static void getPaidTrips (final PaidTripsCallBack callBack){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        Query query = mDatabase.child("trips").child(Objects.requireNonNull(auth.getUid()));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Trip> tempList = new ArrayList<>();

                try {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Boolean isPaid = snapshot.child("isPaid").getValue(Boolean.class);
                    Log.d("ispaid",""+isPaid);

                    if(isPaid) {
                        {
                            double longitude = snapshot.child("longitude").getValue(double.class);
                            double latitude = snapshot.child("latitude").getValue(double.class);
                            Date timeStamp = snapshot.child("timeStamp").getValue(Date.class);
                            String dueDate = snapshot.child("dueDate").getValue(String.class);
                            String ticketPrice = snapshot.child("ticketPrice").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);
                            Boolean paid = snapshot.child("isPaid").getValue(Boolean.class);


                            tempList.add(new Trip(longitude, latitude, ticketPrice, address, dueDate, timeStamp, paid));
                        }
                    }

                }}catch (DatabaseException e){
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                callBack.OnPaidTripCallback(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

   }

    public static void getUnpaidTrips (final UnpaidTripsCallBack callBack){

        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        Query query = mDatabase.child("trips").child(Objects.requireNonNull(auth.getUid()));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Trip> tempList = new ArrayList<>();
                ArrayList<String> keyList = new ArrayList<>();


                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Boolean isPaid = snapshot.child("isPaid").getValue(Boolean.class);
                        Log.d("ispaid", "" + isPaid);

                        if (!isPaid) {

                            String key = snapshot.getKey();
                            double longitude = snapshot.child("longitude").getValue(double.class);
                            double latitude = snapshot.child("latitude").getValue(double.class);
                            Date timeStamp = snapshot.child("timeStamp").getValue(Date.class);
                            String dueDate = snapshot.child("dueDate").getValue(String.class);
                            String ticketPrice = snapshot.child("ticketPrice").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);
                            Boolean paid = snapshot.child("isPaid").getValue(Boolean.class);

                            keyList.add(key);
                            tempList.add(new Trip(longitude, latitude, ticketPrice, address, dueDate, timeStamp, isPaid));
                        }

                    }

                    callBack.onTripCallBack(tempList, keyList);
                }catch (DatabaseException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






    public interface TripCallBack{
        void onTripCallBack(ArrayList<Trip> trips,ArrayList<String>keyList);
    }

    public interface UnpaidTripsCallBack{
        void onTripCallBack(ArrayList<Trip> trips,ArrayList<String>keyList);
    }


    public  interface PaidTripsCallBack{
        void OnPaidTripCallback(ArrayList<Trip>trips);
    }
}
