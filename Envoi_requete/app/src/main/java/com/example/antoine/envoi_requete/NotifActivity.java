package com.example.antoine.envoi_requete;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class NotifActivity extends ActionBarActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        TextView msg = (TextView) this.findViewById(R.id.msg_notif_activity);
        msg.setText(getIntent().getExtras().getString("msg"));

        TextView type = (TextView) this.findViewById(R.id.type_notif_activity);
        switch(getIntent().getExtras().getInt("type")){
            case 0: type.setText("Panne");
                break;
            case 1: type.setText("Accident");
                break;
            case 2: type.setText("Bouchon");
                break;
            case 3: type.setText("Autre");
                break;
        }

        TextView id = (TextView) this.findViewById(R.id.id_notif_activity);
        id.setText(getIntent().getExtras().getString("id"));

        setMap();
    }

    public void setMap(){

        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_notif)).getMap();
        }

        double latitude = getIntent().getExtras().getDouble("lat");
        double longitude = getIntent().getExtras().getDouble("long");

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Vous"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notif, menu);
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
