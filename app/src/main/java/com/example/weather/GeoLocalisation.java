package com.example.weather;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class GeoLocalisation {
    /*private String lon;
    private String lat;
    private final String apiKeyWeather = "da6374c3822465ee254f6e599a7fce5e";
    private String geoloc = "https://api.openweathermap.org/geo/1.0/reverse?lat="+lat+"&lon="+lon+"&limit=5&appid="+apiKeyWeather+"&lang=fr";
    public void start(geoUrl){
        StringRequest stringRequestLocalisation = new StringRequest(Request.Method.GET, geoUrl,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String name = jsonArray.optJSONObject(0).getString("name");
                    city.setText(name);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                city.setText("Problème d'affichage: "+error);
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestLocalisation);
    }*/
}
