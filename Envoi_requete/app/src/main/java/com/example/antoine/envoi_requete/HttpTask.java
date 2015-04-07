package com.example.antoine.envoi_requete;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Antoine on 29/03/2015.
 */
public class HttpTask extends AsyncTask<String, String, String> {

    private static final String URL = "http://192.168.1.32:8001/test/index.php";

    private Context context;
    StringBuilder response = new StringBuilder();

    public HttpTask(Context context) {
        this.context = context;
    }

    /**
     * Envoi une alerte en arrière plan
     * @param params
     * 0 pour le register id
     * 1 pour la latitude
     * 2 pour la longitude
     * 3 pour le type de panne
     * 4 pour l'id du conducteur
     * @return
     */
    @Override
    protected String doInBackground(String... params) {

        String reg_id = "reg_id="+params[0];
        String latitude = "lat="+params[1];
        String longitude = "long="+params[2];
        String type_panne = "type="+params[3];
        String id_routier = "id_routier="+params[4];

        HttpGet httpGet = new HttpGet(URL+"?"+reg_id+"&"+latitude+"&"+longitude+"&"+type_panne+"&"+id_routier);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse requete = httpclient.execute(httpGet);

            if (requete.getStatusLine().getStatusCode() == 200) {

                HttpEntity messageEntity = requete.getEntity();
                InputStream is = messageEntity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }else{
                response.append("Erreur lors de l'envoi");
            }
        }catch(Exception e) {
            Log.d("[EXCEPTION]", e.getMessage());
        }

        return response.toString();
    }

    protected void onPostExecute(String result){

        Toast.makeText(context, result, Toast.LENGTH_LONG).show();

    }
}
