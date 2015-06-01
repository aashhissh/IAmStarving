package ashish.iamstarving;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    public LocationService() {
        super("my_intent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("location")) {
                mCurrentLocation = intent.getParcelableExtra("location");
                getAddress();
            } else {
                buildGoogleApiClient();
                mGoogleApiClient.connect();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Intent intent = new Intent("current-location-IAmStarving");
        intent.putExtra("location", mCurrentLocation);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        stopLocationUpdates();
        stopSelf();
    }

    public void getAddress() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String addressText = new String();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        Location loc = mCurrentLocation;
        List<Address> addresses = null;

        try {
            while (addresses == null) {
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            addressText = String.format(
                    "%s, %s",
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(1) : "",
                    address.getLocality());
        } else {
            addressText = "Chillax! we have your location :)";
        }

        Intent intent = new Intent("current-location-IAmStarving");
        intent.putExtra("address", addressText);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        stopSelf();

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
