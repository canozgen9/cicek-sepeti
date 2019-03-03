package thevertex.com.ciceksepeti.ui.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import thevertex.com.ciceksepeti.logic.algorithm.Algorithm;
import thevertex.com.ciceksepeti.logic.algorithm.Cluster;
import thevertex.com.ciceksepeti.logic.base.Location;
import thevertex.com.ciceksepeti.logic.models.Dealer;
import thevertex.com.ciceksepeti.logic.models.Order;
import thevertex.com.ciceksepeti.R;
import thevertex.com.ciceksepeti.logic.utils.LocationUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<Dealer> dealers = null;
    private ArrayList<Order> orders = null;
    private GoogleMap mMap;
    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        infoTextView = findViewById(R.id.infoTextView);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Algorithm.init();

        dealers = LocationUtils.getDealers(this);
        orders = LocationUtils.getOrders(this);

        for (Dealer dealer : dealers) {
            Algorithm.addCluster(dealer);
        }

        for (Order order : orders) {
            try {
                Algorithm.addOrder(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Algorithm.optimize();
        Algorithm.info(infoTextView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create ArrayList to keep all LatLang's for centering the map
        ArrayList<LatLng> allLatLangs = new ArrayList<>();

        for (Cluster cluster : Algorithm.getClusters()) {
            Dealer dealer = cluster.getDealer();
            mMap.addMarker(dealer.getMarkerOptions());
            allLatLangs.add(dealer.getLatLng());
            for (Order order : cluster.getOrders()) {
                mMap.addMarker(order.getMarkerOptions(dealer.getName()));
                allLatLangs.add(order.getLatLng());
            }
        }

        drawBoundingReactangle(allLatLangs, "#20000000");

        // Move the camera to the center with the help of LocationUtils's findCenterOf method.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationUtils.findCenterOf(allLatLangs), 11f));
    }

    private void drawBoundingReactangle(ArrayList<LatLng> allLatLangs, String color) {
        double minLat = Double.MAX_VALUE, minLong = Double.MAX_VALUE, maxLat = Double.MIN_VALUE, maxLong = Double.MIN_VALUE;
        for (int i = 0; i < allLatLangs.size(); i++) {
            LatLng current = allLatLangs.get(i);
            if (current.latitude <= minLat) {
                minLat = current.latitude;
            }
            if (current.longitude <= minLong) {
                minLong = current.longitude;
            }

            if (current.latitude >= maxLat) {
                maxLat = current.latitude;
            }

            if (current.longitude >= maxLong) {
                maxLong = current.longitude;
            }
        }

        PolygonOptions rectOptions = new PolygonOptions().add(
                new LatLng(minLat, minLong),
                new LatLng(minLat, maxLong),
                new LatLng(maxLat, maxLong),
                new LatLng(maxLat, minLong)
        );
        rectOptions.strokeColor(Color.parseColor(color));
        rectOptions.zIndex(0.8f);
        rectOptions.strokeWidth(5f);

        mMap.addPolygon(rectOptions);
    }
}
