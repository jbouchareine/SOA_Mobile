package com.example.antoine.envoi_requete;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by Antoine on 31/03/2015.
 */
public class RegisterTask  extends AsyncTask<String, String, String> {

    Context context;
    GoogleCloudMessaging gcm;

    public String reg_id = "";

    public RegisterTask(Context context, GoogleCloudMessaging gcm){
        this.context = context;
        this.gcm = gcm;
    }

    @Override
    protected String doInBackground(String... params) {

        String regId = "";
        String PROJECT_NUMBER = params[0];
        try {
            regId = gcm.register(PROJECT_NUMBER);
        } catch (IOException ex) {
            Log.d("[RegisterInBackground]", ex.getMessage());
        }
        return regId;
    }

    @Override
    protected void onPostExecute(String result){

        reg_id = result;
        Toast.makeText(context, "Register done", Toast.LENGTH_LONG).show();
    }


}
