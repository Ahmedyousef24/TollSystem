package tull.application.Controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import DataBaseLayer.UserData;
import Model.User;
import tull.application.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {


    private TextView textViewFN, textViewLN,textViewPN,textViewAddress,textViewCP,textViewUserName,textViewEmail;
    private UserData data;
    private Button signOutBtn;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        textViewFN = view.findViewById(R.id.textView_FN);
        textViewLN = view.findViewById(R.id.textView_LN);
        textViewPN = view.findViewById(R.id.textView_PN);
        textViewAddress = view.findViewById(R.id.textView_Address);
        textViewCP = view.findViewById(R.id.textView_CarPlate);
        textViewUserName = view.findViewById(R.id.Username);
        textViewEmail = view.findViewById(R.id.UserEmailAddress);
        signOutBtn = view.findViewById(R.id.signOutBtn);

        data = new UserData();


        setProfile();



        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

    private void setProfile(){

        data.getUser(new UserData.UserCallBack() {
            @Override
            public void onUserCallBack(User user, String carPlate) {
                textViewFN.setText(user.getfName());
                textViewLN.setText(user.getlName());
                textViewAddress.setText(user.getAddress());
                textViewPN.setText(user.getPhoneNumber());
                textViewCP.setText(carPlate);
                textViewUserName.setText(user.getfName());
                textViewEmail.setText(user.getEmail());

            }
        });

    }
}
