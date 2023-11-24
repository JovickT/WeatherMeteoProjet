package com.example.weather;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MaLocalisationIp {
    /*private final String apiKeyGeo = "b9e655165a164c9c913ee04473920bbc";
    private String urlIp = "https://api.apibundle.io/ip-lookup?apikey="+apiKeyGeo;

    public void start(){
        StringRequest ip = new StringRequest(Request.Method.GET, urlIp,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRootObject = new JSONObject(response);
                    String ville = jsonRootObject.optJSONObject("city").getString("name");
                    lat = jsonRootObject.getString("latitude");
                    lon = jsonRootObject.getString("longitude");
                    url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid="+apiKeyWeather;
                    geoloc = "https://api.openweathermap.org/geo/1.0/reverse?lat="+lat+"&lon="+lon+"&limit=5&appid="+apiKeyWeather;

                    geoLocal(geoloc);
                    localisation(url);
                    city.setText(ville);
                } catch (JSONException e) {
                    city.setText("Erreur: "+e);
                    throw new RuntimeException(e);
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                city.setText("Erreur");
            }
        });
    }*/
}
