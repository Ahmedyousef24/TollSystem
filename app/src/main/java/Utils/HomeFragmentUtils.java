package Utils;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Model.Trip;

public class HomeFragmentUtils {


    public String getDueDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 14);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

        return simpleDateFormat.format(c.getTime());
    }

    public String getGreetings(Calendar c) {

        String greetings = null;
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetings = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetings = "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetings ="Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetings = "Good Night";
        }
        return greetings;
    }

    public String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
        return simpleDateFormat.format(c.getTime());
    }


    // save this to db when creating new trip
    public String getAddress(double lat, double lon, Geocoder geocoder ) {
        List<Address> location = new ArrayList<>();

        String address = "ADDRESS NOT FOUND";
        try {
            location = geocoder.getFromLocation(lat, lon, 1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            address = location.get(0).getAddressLine(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return address;
    }

    public void getTripList(final ArrayList<Trip> list){

   /*     TripData.getTrip(new TripData.TripCallBack() {
            @Override
            public void onTripCallBack(ArrayList<Trip> trips) {
                list.addAll(trips);
            }
        });*/
    }
}
