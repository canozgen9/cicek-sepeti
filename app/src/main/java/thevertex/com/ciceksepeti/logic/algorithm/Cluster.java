package thevertex.com.ciceksepeti.logic.algorithm;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import thevertex.com.ciceksepeti.logic.base.Location;
import thevertex.com.ciceksepeti.logic.models.Dealer;
import thevertex.com.ciceksepeti.logic.models.Order;
import thevertex.com.ciceksepeti.logic.utils.LocationUtils;

public class Cluster {
    private Dealer dealer;
    private ArrayList<Order> orders;

    public Cluster(Dealer dealer) {
        this.dealer = dealer;
        this.orders = new ArrayList<>();
    }

    public int extras() {
        if (getOrders().size() - getDealer().getMinQuota() > 0) {
            return getOrders().size() - getDealer().getMinQuota();
        }
        return 0;
    }

    public int capacity() {
        if (getDealer().getMaxQuota() - getOrders().size() > 0) {
            return getDealer().getMaxQuota() - getOrders().size();
        }
        return 0;
    }

    public boolean hasCapacity() {
        return capacity() > 0;
    }

    public boolean hasExtras() {
        return extras() > 0;
    }

    public double captureFactor() {
        int interval = getDealer().getMaxQuota() - getDealer().getMinQuota();
        int capacity = capacity();
        return (capacity) / (double) interval;
    }

    public double giveFactor() {
        int interval = getDealer().getMaxQuota() - getDealer().getMinQuota();
        int extras = extras();
        return (extras) / (double) interval;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addLocation(Order order) {
        orders.add(order);
    }

    public ArrayList<LatLng> getLatLngs() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(getDealer().getLatLng());
        for (Order order : orders) {
            latLngs.add(order.getLatLng());
        }
        return latLngs;
    }

    public LatLng getCenter() {
        double centroidX = 0, centroidY = 0;
        centroidX += (getDealer().getLatitude());
        centroidY += (getDealer().getLongitude());

        for (Order order : getOrders()) {
            centroidX += order.getLatitude();
            centroidY += order.getLongitude();
        }
        return new LatLng(centroidX / (orders.size() + 1), centroidY / (orders.size() + 1));
    }

    public double distance(LatLng point) {
        return LocationUtils.distance(point, getCenter());
    }
}
