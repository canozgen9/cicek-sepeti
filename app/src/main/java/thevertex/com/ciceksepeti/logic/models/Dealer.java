package thevertex.com.ciceksepeti.logic.models;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import thevertex.com.ciceksepeti.R;
import thevertex.com.ciceksepeti.logic.base.Location;

public class Dealer extends Location {
    private String name;
    private int minQuota = 0;
    private int maxQuota = 0;

    public Dealer(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public MarkerOptions getMarkerOptions() {
        switch (name) {
            case "Red":
                return new MarkerOptions().position(getLatLng()).title(getName() + " Dealer").icon(BitmapDescriptorFactory.fromResource(R.drawable.dealer_red)).zIndex(1f).anchor(0.5f, 0.5f);
            case "Green":
                return new MarkerOptions().position(getLatLng()).title(getName() + " Dealer").icon(BitmapDescriptorFactory.fromResource(R.drawable.dealer_green)).zIndex(1f).anchor(0.5f, 0.5f);
            case "Blue":
                return new MarkerOptions().position(getLatLng()).title(getName() + " Dealer").icon(BitmapDescriptorFactory.fromResource(R.drawable.dealer_blue)).zIndex(1f).anchor(0.5f, 0.5f);
            default:
                return new MarkerOptions().position(getLatLng()).title(getName() + " Dealer").zIndex(1f).anchor(0.5f, 0.5f);
        }
    }

    public String getName() {
        return name;
    }

    public Dealer setName(String name) {
        this.name = name;
        return this;
    }

    public int getMinQuota() {
        return minQuota;
    }

    public Dealer setMinQuota(int minQuota) {
        this.minQuota = minQuota;
        return this;
    }

    public int getMaxQuota() {
        return maxQuota;
    }

    public Dealer setMaxQuota(int maxQuota) {
        this.maxQuota = maxQuota;
        return this;
    }
}
