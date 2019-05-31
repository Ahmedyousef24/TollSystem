package DataBaseLayer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Model.UserLocation;

public class UserLocationData {

    private static DatabaseReference mDatabase;
    private static boolean carExists;
    private static  final String TAG = "CARLOG";
    private static FirebaseAuth auth;

    public static void saveUserLocation(UserLocation userLocation){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        try {
            mDatabase.child("userLocation").child(Objects.requireNonNull(auth.getUid())).setValue(userLocation);
        }catch (DatabaseException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
}
