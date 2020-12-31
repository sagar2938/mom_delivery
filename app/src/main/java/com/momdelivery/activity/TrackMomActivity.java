package com.momdelivery.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.momdelivery.R;
import com.momdelivery.network.ThisApp;
import com.momdelivery.network.request.EventPushRequest;
import com.momdelivery.network.response.PushNotificationResponse;
import com.momdelivery.network.response.on_going.OrderDatum;
import com.momdelivery.utils.FusedLocation;
import com.momdelivery.utils.Helper;
import com.momdelivery.utils.LocalRepositories;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackMomActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    ProgressDialog progressDialog;
    TextView name;
    TextView distance;
    TextView time;
    ImageView image;
    TextView order_id;
    TextView rating;
    TextView address;
    LinearLayout call_layout;
    Button submit;
    Button submit2;

    int tracking=0;

    List<Double> latitudeList = new ArrayList<>();
    List<Double> longitudeList = new ArrayList<>();
    List<Double> durationList = new ArrayList<>();


    OrderDatum orderDatum;

    void init() {

        name = findViewById(R.id.name);
        distance = findViewById(R.id.distance);
        time = findViewById(R.id.time);
        image = findViewById(R.id.image);
        order_id = findViewById(R.id.order_id);
        rating = findViewById(R.id.rating);
        call_layout = findViewById(R.id.call_layout);
        address = findViewById(R.id.address);
        submit = findViewById(R.id.submit);
        submit2 = findViewById(R.id.submit2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        getSupportActionBar().setTitle("Tracking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        fm.getMapAsync(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        Intent intent = new Intent(TrackMomActivity.this, GPSService.class);
        startService(intent);


        orderDatum = LocalRepositories.getAppUser(getApplication()).getOrderDatum();


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog();
            }
        });

        name.setText(orderDatum.getFirstName()+" "+orderDatum.getMiddleName()+" "+orderDatum.getLastName());
        order_id.setText(orderDatum.getOrderId());

        Glide.with(this)
                .load(orderDatum.getImage_name())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().placeholder(R.drawable.user)).into(image);
//        address.setText(orderDatum.getOrderId());

        call_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_layout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + orderDatum.getMomMobile()));
                        startActivity(intent);
                    }
                });
            }
        });


        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + source.latitude + "," + source.longitude +
                                "&daddr=" + destination.latitude + "," + destination.longitude));
                startActivity(intent);
            }
        });

    }

    LatLng source;
    LatLng destination;
    int recursionTime=1;

    void recursion() {

        try {

            source = new LatLng(FusedLocation.location.getLatitude(), FusedLocation.location.getLongitude());
            destination = new LatLng(Double.valueOf(orderDatum.getMom_latitude()), Double.valueOf(orderDatum.getMom_longitude()));
            if (source!=null&destination!=null){
                BitmapDescriptor icon_red = BitmapDescriptorFactory.fromResource(R.drawable.map_red);
                BitmapDescriptor icon_green = BitmapDescriptorFactory.fromResource(R.drawable.map_green);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                MarkerOptions markerOptions1 = new MarkerOptions();
                MarkerOptions markerOptions2 = new MarkerOptions();
                googleMap.clear();
                googleMap.addMarker(markerOptions1.position(source).title("Source")).setIcon(icon_green);
                googleMap.addMarker(markerOptions2.position(destination).title("destination")).setIcon(icon_red);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(source));
                builder.include(markerOptions1.getPosition());
                builder.include(markerOptions2.getPosition());

                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                googleMap.animateCamera(cu);
                recursionTime=60;
            }


//        String url = getDirectionsUrl(new LatLng(source.latitude, source.longitude), new LatLng(destination.latitude, destination.longitude));
            String url = getDirectionsUrl(new LatLng(FusedLocation.location.getLatitude(), FusedLocation.location.getLongitude()), new LatLng(Double.valueOf(orderDatum.getLatitude()), Double.valueOf(orderDatum.getLongitude())));
            ReadTask downloadTask = new ReadTask();
            downloadTask.execute(url);
            distance.setText(String.format("%.2f", Helper.getDistanceBetween(new LatLng(FusedLocation.location.getLatitude(), FusedLocation.location.getLongitude()), new LatLng(Double.valueOf(orderDatum.getLatitude()), Double.valueOf(orderDatum.getLongitude())))) + " KM");
//        distance.setText(""+Helper.getDistance(FusedLocation.location.getLatitude(),FusedLocation.location.getLongitude(),Double.valueOf(response.getResponse().getLatitude()),Double.valueOf(response.getResponse().getLatitude())));


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recursion();
                }
            }, recursionTime * 1000);
        }catch (Exception e){
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        }
    }

    //@Override
    @SuppressLint("NewApi")
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        progressDialog.dismiss();
        recursion();
    }


    /*  @Subscribe
      public void track_on_going(TrackOnGoingResponse response) {
          progressDialog.dismiss();
          BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.star_icon);

          LatLng source = new LatLng(28, 77);
          LatLng destination = new LatLng(28.555, 77.555);
          LatLngBounds.Builder builder = new LatLngBounds.Builder();
          MarkerOptions markerOptions1=new MarkerOptions();
          MarkerOptions markerOptions2=new MarkerOptions();
          googleMap.addMarker(markerOptions1.position(source).title("Source")).setIcon(icon);
          googleMap.addMarker(markerOptions2.position(destination).title("destination")).setIcon(icon);
          googleMap.moveCamera(CameraUpdateFactory.newLatLng(source));
          builder.include(markerOptions1.getPosition());
          builder.include(markerOptions2.getPosition());

          LatLngBounds bounds = builder.build();
          int width = getResources().getDisplayMetrics().widthPixels;
          int height = getResources().getDisplayMetrics().heightPixels;
          int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
          CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
          googleMap.animateCamera(cu);


          String url = getDirectionsUrl(new LatLng(source.latitude, source.longitude), new LatLng(destination.latitude, destination.longitude));
          ReadTask downloadTask = new ReadTask();
          downloadTask.execute(url);
  //        Glide.with(getApplicationContext()).load(response.getResponse().getImage_path() + response.getResponse().getImage_name()).into(image);
  //        name.setText(response.getResponse().getName());

      }




  */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
//        String mode = "mode=driving";
        String mode = "mode=walking";
//        String mode = "mode=bicycling";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyDFrE1WgFtmWfWPExKEnreTaFdsyqJLVfs";
        return url;
    }


    @SuppressLint("NewApi")
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    @SuppressLint("NewApi")
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            try {
                for (int i = 0; i < routes.size(); i++) {
                    latitudeList.clear();
                    longitudeList.clear();
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                        latitudeList.add(lat);
                        longitudeList.add(lng);
                    }
                    polyLineOptions.addAll(points);
                    polyLineOptions.width(7);
                    polyLineOptions.color(Color.BLUE);
                    googleMap.addPolyline(polyLineOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("http ex  " + e.getMessage());
            }
            double d = 0.0;
//            double d2 = 0.0;
            for (int i = 0; i < latitudeList.size() - 1; i++) {
                d = d + Helper.getDistance(latitudeList.get(i), longitudeList.get(i), latitudeList.get(i + 1), longitudeList.get(i + 1));
//                d2 = d2 + Helper.getDistanceBetween(new LatLng(latitudeList.get(i), longitudeList.get(i)),new LatLng( latitudeList.get(i + 1), longitudeList.get(i + 1)));
            }

            double t = 0;
            for (int i = 0; i < durationList.size(); i++) {
                t = t + durationList.get(i);
            }

//            System.out.println("llllllllll " + latitudeList.get(0) + "," + longitudeList.get(0));
//            System.out.println("llllllllll " + latitudeList.get(latitudeList.size() - 1) + "," + longitudeList.get(latitudeList.size() - 1));


            int hours = (int) (t / 60);
            int minutes = (int) (t % 60);
            System.out.printf("%d:%02d", hours, minutes);
            distance.setText(String.format("%.1f", d) + " km");
            time.setText(String.format("%02d:%02d", hours, minutes) + " min");

        }
    }


    @Subscribe
    public void timeout(String msg) {
        progressDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }


    public class HttpConnection {
        @SuppressLint("LongLogTag")
        public String readUrl(String mapsApiDirectionsUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mapsApiDirectionsUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            } catch (Exception e) {
                Log.d("Exception while reading url", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

    }


    public class PathJSONParser {

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {
                jRoutes = jObject.getJSONArray("routes");
                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                    durationList.clear();
                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {

                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                    .get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

//                            source = list.get(0);
//                            destination = list.get(list.size() - 1);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }

//                            distance_text = (String) ((JSONObject) ((JSONObject) jSteps
//                                    .get(k)).get("distance")).get("text");
//                            duration_text = (String) ((JSONObject) ((JSONObject) jSteps
//                                    .get(k)).get("duration")).get("text");
                            String[] ar = ((String) ((JSONObject) ((JSONObject) jSteps
                                    .get(k)).get("duration")).get("text")).split(" ");
                            durationList.add(Double.valueOf(ar[0]));

                        }
                        routes.add(path);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        /**
         * Method Courtesy :
         * jeffreysambells.com/2010/05/27
         * /decoding-polylines-from-google-maps-direction-api-with-java
         */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    @Subscribe
    public void event_click(LocationEvent locationEvent) {
//        FusedLocation.location = locationEvent.getLocation();
        if (Helper.isNetworkAvailable(getApplicationContext())) {
//            ApiCallsService.action(this, Cv.ACTION_TRACK_ONGOING);
        }
    }


    public void getDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reached?")
                .setMessage("Are you sure, you have reached to MOM's CHEF destination?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ThisApp.getApi(getApplicationContext()).pushNotification(new EventPushRequest(orderDatum.getMobile(),""+LocalRepositories.getAppUser(getApplicationContext()).getUserDatum().getName()+" has reached")).enqueue(new Callback<PushNotificationResponse>() {
                            @Override
                            public void onResponse(Call<PushNotificationResponse> call, Response<PushNotificationResponse> response) {
                                Toast.makeText(TrackMomActivity.this, "You have notified your MOM' CHEF", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PushNotificationResponse> call, Throwable t) {
                                Toast.makeText(TrackMomActivity.this, "Sorry, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }
}
