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

    private static final String PROJECT_NUMBER = "465384961834";

    private Context context;
    public String reg_id = "";

    public RegisterTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try {
            reg_id = gcm.register(PROJECT_NUMBER);
        } catch (IOException ex) {
            Log.d("[RegisterInBackground]", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){

        if(reg_id.equals(""))
            Toast.makeText(context, "Erreur lors de l'enregistrement. Veuillez vérifier votre connexion internet", Toast.LENGTH_LONG).show();
    }

}
