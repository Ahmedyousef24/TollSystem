package tull.application.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import DataBaseLayer.UserData;
import Utils.HomeActivityUtils;
import tull.application.R;

import static Container.Constants.ERROR_DIALOG_REQUEST;
import static Container.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static Container.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private Toolbar toolbar;
    private TextView nameTextView, emailTextView;
    private String tempEmail, tempFName;
    private String TAG = "MAP";
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private HomeActivityUtils utils;
    private MapViewFragment mapViewFragment;
    private  PaymentFragment paymentFragment;
    private UserProfileFragment profileFragment;
    private BottomNavigationView navView;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        navView = findViewById(R.id.bottomNavView);

        homeFragment = new HomeFragment();
        mapViewFragment = new MapViewFragment();
        paymentFragment = new PaymentFragment();
        profileFragment = new UserProfileFragment();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        utils = new HomeActivityUtils(getApplicationContext(), this);

        SetFragment(homeFragment, mapViewFragment, profileFragment,paymentFragment);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        SetFragment(homeFragment, mapViewFragment, profileFragment, paymentFragment);
                        return true;
                    case R.id.profile:
                        SetFragment(profileFragment,  paymentFragment, homeFragment ,mapViewFragment);
                        return true;
                    case R.id.payment:
                        SetFragment(paymentFragment,  profileFragment, homeFragment ,mapViewFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }



    //Dialog to enable gps
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Check if gps is enabled on device
    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {

   /*      Request location permission, so that we can get the location of the
          device. The result of the permission request is handled by a callback,
          onRequestPermissionsResult.*/

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            utils.getLastKnownLocation(fusedLocationProviderClient);
            return;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    break;
                } else {
                    getLocationPermission();
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                // start to get the location
                utils.getLastKnownLocation(fusedLocationProviderClient);
                return;
            } else {
                getLocationPermission();
            }
        }
    }

    private void SetFragment(Fragment fragmentToShow, Fragment fragmentToHide1, Fragment fragmentToHide2, Fragment fragmentToHide3) {


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(fragmentToShow);

        if (!fragmentToShow.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, fragmentToShow);
            fragmentTransaction.hide(fragmentToHide1);
            fragmentTransaction.hide(fragmentToHide2);
            fragmentTransaction.hide(fragmentToHide3);


            fragmentTransaction.commit();
            return;
        }
        else{
            fragmentTransaction.show(fragmentToShow);
            fragmentTransaction.hide(fragmentToHide1);
            fragmentTransaction.hide(fragmentToHide2);
            fragmentTransaction.hide(fragmentToHide3);
            fragmentTransaction.commit();
        }

    }


    private void getFName() {
        UserData.getFName(new UserData.FNameCallBack() {
            @Override
            public void onFNameCallBack(String fName) {
                tempFName = fName;
            }
        });
    }

    private void getEmail() {
        UserData.getEmail(new UserData.EmailCallBack() {
            @Override
            public void onEmailCallBack(String email) {
                tempEmail = email;
            }
        });

    }
}
