package Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import Model.Tull;
import Services.LocationService;

import static android.support.constraint.Constraints.TAG;

public class HomeActivityUtils {

    private boolean flag = false;
    private Context context;
    private Activity activity;
    private Tull tull;
    private Location location;

    public HomeActivityUtils(Context context, Activity activity) {
        this.activity = activity;
        this.context = context;
    }


    public void getLastKnownLocation(FusedLocationProviderClient fusedLocationProviderClient) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    location = task.getResult();
                    Log.d("homeActivtyUTILS", "" + location.getLatitude());
                  //  UserLocationData.saveUserLocation(new UserLocation(new LatLng(location.getLatitude(), location.getLongitude())));

                    startLocationService();

                }

            }
        });
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(context, LocationService.class);
            activity.startForegroundService(serviceIntent);
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo
                service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("Services.LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");

        return false;
    }

}
