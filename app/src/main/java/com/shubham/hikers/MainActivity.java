package com.shubham.hikers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;

    TextView lattv;
    TextView lngtv;
    TextView accuracytv;
    TextView speedtv;
    TextView bearingtv;
    TextView altitudetv;
    TextView addresstv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lattv = (TextView) findViewById(R.id.lat);
        lngtv = (TextView) findViewById(R.id.lng);
        accuracytv = (TextView) findViewById(R.id.accuracy);
        speedtv = (TextView) findViewById(R.id.speed);
        bearingtv = (TextView) findViewById(R.id.bearing);
        altitudetv = (TextView) findViewById(R.id.altitude);
        addresstv = (TextView) findViewById(R.id.address);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 124);

        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        Location location = locationManager.getLastKnownLocation(provider);

        onLocationChanged(location);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            //Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            // Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 124);


        }
        locationManager.removeUpdates(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 124);


        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location)
    {


        if (location != null)
        {


            Double lat = location.getLatitude();
            Double lng = location.getLongitude();

            Double alt = location.getAltitude();

            Float bearing = location.getBearing();

            Float speed = location.getSpeed();

            Float accuracy = location.getAccuracy();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try
        {
            List<Address> listAddresses = geocoder.getFromLocation(lat,lng,1);
            if (listAddresses !=null && listAddresses.size()>0)
            {
                Log.i("PlaceInfo",listAddresses.get(0).toString());

                String addressholder ="";

                for(int i=0; i<listAddresses.get(0).getMaxAddressLineIndex();i++)
                {
                    addressholder +=listAddresses.get(0).getAddressLine(i)+"\n";
                }
                addresstv.setText("Address :\n"+addressholder);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        lattv.setText("Latitude  : " +lat.toString());
        lngtv.setText("Longitude : " +lng.toString());
        altitudetv.setText("Altitude :" +alt.toString());
        bearingtv.setText("Bearing :"+bearing.toString());
        speedtv.setText("Speed : " + speed.toString() + " m/s " );
        accuracytv.setText("Accuracy : " + accuracy.toString()+" m ");

      /*  Log.i("Latitude",String.valueOf(lat));
        Log.i("Longitude",String.valueOf(lng));
        Log.i("Altitude",String.valueOf(alt));
        Log.i("Bearing",String.valueOf(bearing));
        Log.i("Speed",String.valueOf(speed));
        Log.i("Accuracy",String.valueOf(accuracy));*/

    }}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                } else
                {
                    Log.d("TAG", "Coarse Permission Not Granted");
                }
                break;

            case 124:

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                } else
                {
                    Log.d("TAG", " Fine Permission Not Granted ");
                }
                break;
            default:
                break;
        }
    }
}
