package DataBaseLayer;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import Model.User;

public class UserData {

    private static DatabaseReference mDatabase;
    private static boolean userExists;
    private static  final String TAG = "USERLOG";
    private static FirebaseAuth auth;


    // Creates an account in FireBase
    public static void createNewUser(String fName, String lName, String email, String phoneNumber, String address, String city, String country, String zipCode) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        User user = new User(fName, lName, email, phoneNumber, address,city,  country, zipCode);
        try {
            mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
        } catch (DatabaseException e) {
            throw e;
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    public static void updateBalance(final double balance) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        mDatabase.child("users").child(Objects.requireNonNull(auth.getUid())).child("balance").setValue(balance);

      /*  M.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



               // PaymentFragment.updateBalance();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }


    public void getUser(final UserCallBack callBack){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        Query query = mDatabase.child("users").child(Objects.requireNonNull(auth.getUid()));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String carPlate = null;

                       carPlate =  dataSnapshot.child("cars").child("numberPlate").getValue(String.class);


                    callBack.onUserCallBack(new User(dataSnapshot.child("fName").getValue(String.class),
                            dataSnapshot.child("lName").getValue(String.class),
                            dataSnapshot.child("email").getValue(String.class),
                            dataSnapshot.child("phoneNumber").getValue(String.class),
                            dataSnapshot.child("address").getValue(String.class),
                            dataSnapshot.child("city").getValue(String.class),
                            dataSnapshot.child("country").getValue(String.class),
                            dataSnapshot.child("zipCode").getValue(String.class)),carPlate);
                }catch (DatabaseException e){
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    // Gets the users email
    public static void getEmail(final EmailCallBack callBack){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
  
        Query query = mDatabase.child("users").child(auth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String email = null;
                    if(dataSnapshot.child("email").getValue(String.class) != null){
                        email= dataSnapshot.child("email").getValue(String.class);
                    }

                callBack.onEmailCallBack(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Gets users first Name
    public static void getFName(final FNameCallBack callBack){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        Query query = mDatabase.child("users").child(auth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fName = null;
                if(dataSnapshot.child("fName").getValue(String.class) != null){
                    fName= dataSnapshot.child("fName").getValue(String.class);
                }

                callBack.onFNameCallBack(fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getBalance(final BalanceCallBack callBack){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        Query query = mDatabase.child("users").child(auth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    try {
                        callBack.onBalanceCallBack(dataSnapshot.child("balance").getValue(double.class));
                    }catch (DatabaseException e){
                        e.printStackTrace();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public interface UserCallBack{
        void onUserCallBack(User user,String carPlate);
    }

    public interface EmailCallBack{
        void onEmailCallBack(String email);
    }

    public interface BalanceCallBack{
        void onBalanceCallBack(double balance);
    }

    public interface FNameCallBack{
        void onFNameCallBack(String fName);
    }
}
