# The Vertex - Solution
### Info
Our solution developed in Android Studio with Java. We used Google Maps to simulate orders and dealers.
>  You need to replace your **Google Maps Key** in **"/res/values/google_maps.api.xml"** in order to run application.
### Algorithm
Our Algorithm bases on clustering solution. Every cluster have their own centroid. We check every single order's distance from the all dealers centroid and the less one is the optimal dealer for this single order. For the limitations of minimum and maximum carry, we hold GF (Giving Factor) and CF (Capturing Factor).  These values calculated according to how much orders can a dealer capture or how much orders should a dealer capture.
 ### Screenshot
![Alt text](/previews/1.png?raw=true "Screenshot")