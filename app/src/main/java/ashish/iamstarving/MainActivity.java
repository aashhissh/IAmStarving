package ashish.iamstarving;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    private TextView tvClientLocation;
    private Location mCurrentLocation;
    static File cacheDir;

    private ProgressDialog progress;
    private RestaurantListAdapter restaurantItemArrayAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("location")) {
                mCurrentLocation = (Location) intent.getParcelableExtra("location");
                new MyAsyncTask().execute();
                tvClientLocation.setText("Addressing Location ....");
                Intent locationService = new Intent(getApplicationContext(), LocationService.class);
                locationService.putExtra("location", mCurrentLocation);
                startService(locationService);
            } else {
                tvClientLocation.setText(intent.getStringExtra("address"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean status = checkInternetAndGps();
        if(status) {
            init();
            Intent locationService = new Intent(getApplicationContext(), LocationService.class);
            startService(locationService);
        }
    }

    private void init() {
        tvClientLocation = (TextView) findViewById(R.id.tvClientLocation);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, new IntentFilter("current-location-IAmStarving"));
        cacheDir = getApplicationContext().getDir("service_api_cache", Context.MODE_PRIVATE);

        progress = new ProgressDialog(this);
        progress.setTitle("Stay with us !");
        progress.setMessage("Finding best deals near You  :P");
        progress.show();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        String responseText = null;
        OkHttpClient client;


        @Override
        protected Double doInBackground(String... params) {
            postData();
            return null;
        }

        protected void onPostExecute(Double result) {
            if (responseText != null) {
                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(responseText);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                Map<String, RestaurantData> restaurantDatas = gson.fromJson(object.get("data"), new TypeToken<Map<String, RestaurantData>>() {
                }.getType());
                ListView tripList = (ListView) findViewById(R.id.lvRestaurants);
                ArrayList<RestaurantData> restaurantList = sortedList(new ArrayList<RestaurantData>((java.util.Collection<? extends RestaurantData>) restaurantDatas.values()));
                restaurantItemArrayAdapter = new RestaurantListAdapter(getApplicationContext(), restaurantList);
                tripList.setAdapter(restaurantItemArrayAdapter);
                progress.dismiss();
            } else {

            }
            if(tvClientLocation.getText().toString().contains("Chilax")){
                Toast.makeText(getApplicationContext(), "We couldn't find your address !!", Toast.LENGTH_LONG).show();
            }
        }

        public ArrayList<RestaurantData> sortedList(ArrayList<RestaurantData> restaurantDatas) {
            for (RestaurantData restaurantData : restaurantDatas) {
                Location location = new Location("");
                location.setLatitude(restaurantData.getLatitude());
                location.setLongitude(restaurantData.getLongitude());
                restaurantData.setDistance((long) mCurrentLocation.distanceTo(location));
            }
            Collections.sort(restaurantDatas, new RestaurantComparator());
            return restaurantDatas;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData() {
            String sURL = "http://staging.couponapitest.com/task_data.txt"; //just a string

            client = new OkHttpClient();
            try {
                CacheResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                responseText = run(sURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String run(String sURL) throws IOException {
            Request request = new Request.Builder()
                    .url(sURL)
                    //.cacheControl()
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        public void CacheResponse() throws Exception {
            //File cacheDir = context.getDir("service_api_cache", Context.MODE_PRIVATE);
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(cacheDir, cacheSize);

            client = new OkHttpClient();
            client.setCache(cache);
        }

        public class RestaurantComparator implements Comparator<RestaurantData> {
            @Override
            public int compare(RestaurantData r1, RestaurantData r2) {
                return (r1.getDistance() == r2.getDistance()) ? 0 : (r1.getDistance() > r2.getDistance()) ? 1 : -1;
            }
        }

    }

    private boolean checkInternetAndGps(){
        boolean status = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((mobileNetwork != null && mobileNetwork.isConnected()) || (wifiNetwork != null && wifiNetwork.isConnected())){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                finish();
            }else{
                status = true;
            }
        }else {
            Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(intent);
            finish();
        }
        return status;
    }
}
