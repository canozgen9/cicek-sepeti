package thevertex.com.ciceksepeti.logic.algorithm;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import thevertex.com.ciceksepeti.logic.models.Dealer;
import thevertex.com.ciceksepeti.logic.models.Order;

public class Algorithm {
    // Singleton
    private static Algorithm algorithm;
    private double scaleFactor = 1;

    public Algorithm() {
        this.clusters = new ArrayList<>();
    }

    public static void init() {
        if (algorithm == null) {
            algorithm = new Algorithm();
        }
    }

    // Class Variables
    private ArrayList<Cluster> clusters;

    public static void setScaleFactor(double factor) {
        algorithm.scaleFactor = factor;
    }

    public static void addCluster(Dealer dealer) {
        algorithm.clusters.add(new Cluster(dealer));
    }

    public static ArrayList<Cluster> getClusters() {
        return algorithm.clusters;
    }

    public static void addOrder(Order order) throws Exception {
        Cluster optimalCluster = null;

        double minDistance = Double.MAX_VALUE;
        for (Cluster cluster : algorithm.clusters) {
            double distance = cluster.distance(order.getLatLng());
            if (Double.isNaN(distance)) {
                Log.d(Algorithm.class.getSimpleName(), String.format("Order %d(%f,%f) distance from %s(%f,%f) is %f", order.getId(), order.getLatitude(), order.getLongitude(), cluster.getDealer().getName(), cluster.getDealer().getLatitude(), cluster.getDealer().getLongitude(), distance));
            }

            if (distance <= minDistance) {
                minDistance = distance;
                optimalCluster = cluster;
            }
        }


        if (optimalCluster != null) {
            optimalCluster.addLocation(order);
        } else {
            throw new Exception("No optimal cluster found.");
        }
    }

    public static void optimize() {
        for (Cluster currentCluster : getClusters()) {
            int currentBalance = getBalance(currentCluster);
            Log.d("ALGORITHM_LOG", String.format("Exchange starting for %s with balance %d", currentCluster.getDealer().getName(), currentBalance));
            while (currentBalance < 0) {
                captureOptimalOrder(currentCluster);
                currentBalance = getBalance(currentCluster);
            }
            while (currentBalance > 0) {
                giveOptimalOrder(currentCluster);
                currentBalance = getBalance(currentCluster);
            }
            Log.d("ALGORITHM_LOG", String.format("Exchange ended for %s with balance %d", currentCluster.getDealer().getName(), currentBalance));
        }
    }

    public static void giveOptimalOrder(Cluster base) {
        Log.d("ALGORITHM_LOG", "giveOptimalOrder(" + base.getDealer().getName() + ");");
        ArrayList<Cluster> others = new ArrayList<>();
        for (Cluster cluster : getClusters()) {
            if (cluster != base) {
                if (cluster.hasCapacity()) {
                    others.add(cluster);
                }
            }
        }

        double minDistance = Double.MAX_VALUE;

        Cluster selectedCluster = null;
        Order selectedOrder = null;

        for (Order order : base.getOrders()) {
            for (Cluster cluster : others) {
                double distance = cluster.distance(order.getLatLng());
                if (distance <= minDistance) {
                    minDistance = distance;
                    selectedCluster = cluster;
                    selectedOrder = order;
                }
            }
        }

        if (selectedCluster != null) {
            base.getOrders().remove(selectedOrder);
            selectedCluster.getOrders().add(selectedOrder);
        } else {
            throw new Error("No closest order found.");
        }

    }

    public static void captureOptimalOrder(Cluster base) {
        Log.d("ALGORITHM_LOG", "captureOptimalOrder(" + base.getDealer().getName() + ");");
        ArrayList<Cluster> others = new ArrayList<>();
        for (Cluster cluster : getClusters()) {
            if (cluster != base) {
                if (cluster.hasExtras()) {
                    others.add(cluster);
                }
            }
        }

        double minDistance = Double.MAX_VALUE;

        Cluster selectedCluster = null;
        Order selectedOrder = null;

        for (Cluster cluster : others) {
            for (Order order : cluster.getOrders()) {
                double distance = base.distance(order.getLatLng());
                if (distance <= minDistance) {
                    minDistance = distance;
                    selectedCluster = cluster;
                    selectedOrder = order;
                }
            }
        }

        if (selectedCluster != null) {
            selectedCluster.getOrders().remove(selectedOrder);
            base.getOrders().add(selectedOrder);
        } else {
            throw new Error("No closest order found.");
        }
    }

    public static boolean isOptimized() {
        for (Cluster cluster : getClusters()) {
            if (getBalance(cluster) != 0) {
                return false;
            }
        }
        return true;
    }

    public static int getBalance(Cluster cluster) {
        if (cluster.getOrders().size() < cluster.getDealer().getMinQuota()) {
            return cluster.getOrders().size() - cluster.getDealer().getMinQuota();
        } else if (cluster.getOrders().size() > cluster.getDealer().getMaxQuota()) {
            return cluster.getOrders().size() - cluster.getDealer().getMaxQuota();
        } else {
            return 0;
        }
    }

    @SuppressLint("DefaultLocale") public static void info(TextView textView) {
        StringBuilder text = new StringBuilder();
        for (Cluster cluster : getClusters()) {
            text.append(String.format("<h2 style=\"color: #FF0000\">%s Dealer</h2>", cluster.getDealer().getName()));
            text.append(String.format("should handle between <b>%d-%d</b> orders,<br>", cluster.getDealer().getMinQuota(), cluster.getDealer().getMaxQuota()));
            text.append(String.format("is handling <b>%d</b> orders.<br>", cluster.getOrders().size()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text.toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(text.toString()));
        }
    }
}
