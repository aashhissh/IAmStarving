package ashish.iamstarving;

import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class NoGPSActivity extends ActionBarActivity {
    private Button btGPSTryAgain, btGoToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_gps);

        btGPSTryAgain = (Button) findViewById(R.id.btGPSTryAgain);
        btGoToSettings = (Button) findViewById(R.id.btGoToSettings);

        btGPSTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                   startActivity(intent);
                   finish();
                }
            }
        });

        btGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
    }

}
