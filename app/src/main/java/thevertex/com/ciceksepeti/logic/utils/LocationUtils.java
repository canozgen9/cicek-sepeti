package thevertex.com.ciceksepeti.logic.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import thevertex.com.ciceksepeti.logic.algorithm.Cluster;
import thevertex.com.ciceksepeti.logic.models.Dealer;
import thevertex.com.ciceksepeti.logic.models.Order;
import thevertex.com.ciceksepeti.R;

public class LocationUtils {
    public static ArrayList<Dealer> getDealers(Context context) {
        ArrayList<Dealer> dealers = new ArrayList<>();
        try {
            InputStream fis = context.getResources().openRawResource(R.raw.dealers);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] exploded = line.split("\\s+");
                String name = exploded[0];
                double latitude = Double.parseDouble(exploded[1]);
                double longitude = Double.parseDouble(exploded[2]);
                int minQuota = Integer.parseInt(exploded[3]);
                int maxQuota = Integer.parseInt(exploded[4]);
                dealers.add(new Dealer(latitude, longitude).setName(name).setMinQuota(minQuota).setMaxQuota(maxQuota));
                Log.d(LocationUtils.class.getSimpleName(), "dealer:" + name + ", coordinate:" + latitude + ":" + longitude);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dealers;
    }

    public static ArrayList<Order> getOrders(Context context) {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            InputStream fis = context.getResources().openRawResource(R.raw.orders);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] exploded = line.split("\\s+");
                int id = Integer.parseInt(exploded[0]);
                double latitude = Double.parseDouble(exploded[1]);
                double longitude = Double.parseDouble(exploded[2]);
                orders.add(new Order(latitude, longitude).setId(id));
                Log.d(LocationUtils.class.getSimpleName(), "order:" + id + ", coordinate:" + latitude + ":" + longitude);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static LatLng findCenterOf(ArrayList<LatLng> pts) {
        double centroidX = 0, centroidY = 0;

        for (LatLng knot : pts) {
            centroidX += knot.latitude;
            centroidY += knot.longitude;
        }
        return new LatLng(centroidX / pts.size(), centroidY / pts.size());
    }

    public static double distance(LatLng l1, LatLng l2) {
        double x1 = l1.latitude;
        double x2 = l2.latitude;
        double y1 = l1.longitude;
        double y2 = l2.longitude;

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
