package Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class UserLocation {

   private LatLng location;
   private Date timeStamp;

    public UserLocation(LatLng location) {

        this.location = location;
        this.timeStamp = new Date();
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
