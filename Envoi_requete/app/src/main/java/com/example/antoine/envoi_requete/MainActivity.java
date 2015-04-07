package com.example.antoine.envoi_requete;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity  implements LocationListener {

    private GoogleMap mMap;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private RegisterTask registerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerTask = new RegisterTask(this);
        registerTask.execute();

        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.maps)).getMap();
        }

        //Setup de la liste
        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] values = new String[] { "Panne", "Accident", "Bouchon", "Autre" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, list);
        listview.setAdapter(adapter);
        listview.setItemChecked(0, true);

        ToggleButton serviceButton = (ToggleButton) this.findViewById(R.id.toggleButton);
        serviceButton.setText("Lancer");
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    public void sendHttp(View v){

        ListView listview = (ListView) findViewById(R.id.listView);

        HttpTask httpTask = new HttpTask(this);
        httpTask.execute(registerTask.reg_id, latitude+"", longitude+"", listview.getCheckedItemPosition()+"", "123");

    }

    public void serviceButton(View v){

        ToggleButton serviceButton = (ToggleButton) this.findViewById(R.id.toggleButton);

        if(serviceButton.isChecked())//play the service
        {
            startService(new Intent(this,LocationService.class));
            serviceButton.setText("Démarré");
        }
        else//stop the service
        {
            stopService(new Intent(this,LocationService.class));
            serviceButton.setText("Stoppé");
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Vous"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
