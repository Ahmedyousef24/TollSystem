package Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import DataBaseLayer.TripData;
import DataBaseLayer.TullData;
import Model.Trip;
import Model.Tull;
import Utils.HomeFragmentUtils;
import tull.application.R;

import static Container.Constants.CHANNEL_1_ID;
import static Container.Constants.DISTANCE_TO_TULL;
import static Container.Constants.GEO_RADIUS;

public class LocationService extends Service {

    private static final String TAG = "LocationService";

    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 1000;  /* 1 secs */
    private final static long FASTEST_INTERVAL = 1000; /* 2 sec */
    private int meter = 5;

    private ArrayList<Tull> tullArrayList;
    private HomeFragmentUtils utils;
    private Boolean alreadyBeenInside = false;
    private Boolean alreadyPassed = false;
    private LatLng latLng;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        tullArrayList = new ArrayList<>();

        utils = new HomeFragmentUtils();

        TullData.getTull(new TullData.TullCallBack() {
            @Override
            public void onTullCallBack(Tull tull) {
                tullArrayList.add(tull);
            }
        });

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_LOW);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called.");
        getLocation();
        return START_NOT_STICKY;
    }

    private void getLocation() {

        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {


                        Location location = locationResult.getLastLocation();

                        if (location != null) {
                            //  UserLocation userLocation = new UserLocation( new LatLng(location.getLatitude(),location.getLongitude()));
                            //  UserLocationData.saveUserLocation(userLocation);
                            //  Log.d(TAG, "onLocationResult: got location result."+ locationResult.getLastLocation());

                            checkIfPassedTull(location);
                            // here get location of the tull and compare it to the userLocation

                        }
                    }
                },
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed */
    }


    private void checkIfPassedTull(final Location userLocation) {

        // double distance= distance(userLocation.getLatitude(),userLocation.getLongitude(),tull.getLocation().latitude,tull.getLocation().longitude);
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        //   Log.d("distance",""+distance);


        final float[] distance = new float[2];
        final float[] distance2 = new float[2];


        try {
            TullData.getTull(new TullData.TullCallBack() {
                @Override
                public void onTullCallBack(Tull tull) {

                    CircleOptions circle = new CircleOptions().center(tull.getLocation()).radius(GEO_RADIUS);


                    Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), tull.getLocation().latitude, tull.getLocation().longitude, distance2);
                    Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), circle.getCenter().latitude, circle.getCenter().longitude, distance);


                    if (alreadyPassed && alreadyBeenInside && distance[0] > circle.getRadius()) {

                        TripData.saveTrip(new Trip(userLocation.getLongitude(), userLocation.getLatitude(), "40", utils.getAddress(userLocation.getLatitude(), userLocation.getLongitude(), geocoder), utils.getDueDate(), new Date(), false));
                        notifyOnTollPassed();
                        // passed a tull
                        Log.d("GEo", "radius" + distance[0] + "distance" + distance2[0]);

                        alreadyBeenInside = false;
                        alreadyPassed = false;
                    }
                    Log.d("GEo", "" + distance[0]);
                    if (distance[0] < circle.getRadius()) { // 20 METER
                        Log.d("SAME", "SAME LOCATIOM");
                        alreadyBeenInside = true;

                    }
                    if (distance2[0] < DISTANCE_TO_TULL) {
                        Log.d("SAME", "SAME AS TULL");
                        alreadyPassed = true;
                    }

                }
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    private void notifyOnTollPassed() {


        NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID,
                "My Channel",
                NotificationManager.IMPORTANCE_HIGH);


        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_account_balance)
                .setContentTitle("Toll Passed")
                .setContentText("You've passed a toll.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();

        startForeground(1, notification);

    }


}
