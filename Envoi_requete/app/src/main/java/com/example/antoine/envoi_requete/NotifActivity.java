package com.example.antoine.envoi_requete;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class NotifActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        /*TextView msg = (TextView) this.findViewById(R.id.msg);
        msg.setText(getIntent().getExtras().getString("msg"));*/

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
