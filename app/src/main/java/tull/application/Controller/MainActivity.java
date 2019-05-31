package tull.application.Controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tull.application.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        View image = findViewById(R.id.mainLogo);
        auth = FirebaseAuth.getInstance();

    }

    // On start check if user is logged In
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            setToLoginFragment();
        } else {
            setToHomeActivity();

        }
    }

    // set to  home activity
    private void setToHomeActivity() {
        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
        finish();
    }

    // set to login activity
    private void setToLoginFragment() {
        Fragment loginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();
    }
}