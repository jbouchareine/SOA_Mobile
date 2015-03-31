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

    private Context context;
    StringBuilder response = new StringBuilder();

    public HttpTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse requete = httpclient.execute(httpGet);

            if (requete.getStatusLine().getStatusCode() == 200) {
                Log.d("[REQUEST]", "Requete envoyé");

                HttpEntity messageEntity = requete.getEntity();
                InputStream is = messageEntity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        }catch(Exception e) {
            Log.d("[EXCEPTION]", e.getMessage());
        }

        return response.toString();
    }

    protected void onPostExecute(String result){
        Log.d("REPONSE", result);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
