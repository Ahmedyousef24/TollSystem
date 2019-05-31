package Model;

import com.google.android.gms.maps.model.LatLng;

public class Tull {

    private LatLng location;

    public Tull (LatLng location){

        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
