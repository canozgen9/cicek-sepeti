package thevertex.com.ciceksepeti.logic.base;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class Location {
    private double latitude = 0, longitude = 0;
    private LatLng latLng;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        latLng = new LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng getLatLng() {
        return latLng;
    }

}
