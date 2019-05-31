package DataBaseLayer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Car;
import Model.User;

public class CarData {
    private static DatabaseReference mDatabase;
    private static boolean carExists;
    private static  final String TAG = "CARLOG";
    private static FirebaseAuth auth;


    // Creates a car in FireBase
    public static void createNewCar(String carPlate) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        Car car = new Car(carPlate);
        try {
            mDatabase.child("users").child(auth.getCurrentUser().getUid()).child("cars").setValue(car);
        } catch (DatabaseException e) {
            throw e;
        }

    }
}

