package DataBaseLayer;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Model.Tull;

public class TullData {

    private static DatabaseReference mDatabase;
    private static FirebaseAuth mAuth;

    public static void getTull(final TullCallBack callBack){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query query = mDatabase.child("sensor");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  try {

                      //for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                      callBack.onTullCallBack(toTull(dataSnapshot.child("latitude").getValue(String.class),dataSnapshot.child("longitude").getValue(String.class)));
                      //}
                  }catch (NullPointerException e){
                      e.printStackTrace();
                  }catch (DatabaseException e){
                      e.printStackTrace();
                  }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static Tull toTull(String lat, String lon){




        double longitude =  Double.parseDouble(lon);
        double latitude = Double.parseDouble(lat);

        return new Tull(new LatLng(latitude,longitude));

    }


    public interface TullCallBack {
        void onTullCallBack(Tull tull);
    }

}
