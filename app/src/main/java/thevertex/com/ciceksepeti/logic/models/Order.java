package thevertex.com.ciceksepeti.logic.models;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import thevertex.com.ciceksepeti.R;
import thevertex.com.ciceksepeti.logic.base.Location;

public class Order extends Location {
    private int id;

    public MarkerOptions getMarkerOptions(String name) {
        switch (name) {
            case "Red":
                return new MarkerOptions().position(getLatLng()).title(getId() + " Order").icon(BitmapDescriptorFactory.fromResource(R.drawable.red_point)).zIndex(0.99f).anchor(0.5f, 0.5f);
            case "Green":
                return new MarkerOptions().position(getLatLng()).title(getId() + " Order").icon(BitmapDescriptorFactory.fromResource(R.drawable.green_point)).zIndex(0.99f).anchor(0.5f, 0.5f);
            case "Blue":
                return new MarkerOptions().position(getLatLng()).title(getId() + " Order").icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_point)).zIndex(0.99f).anchor(0.5f, 0.5f);
            default:
                return new MarkerOptions().position(getLatLng()).title(getId() + " Order").zIndex(1f).anchor(0.5f, 0.5f);
        }
    }

    public Order(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public int getId() {
        return id;
    }

    public Order setId(int id) {
        this.id = id;
        return this;
    }
}
