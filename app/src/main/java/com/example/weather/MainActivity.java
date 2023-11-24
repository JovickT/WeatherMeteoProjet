package com.example.weather;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView city,temp,infoTempMax,infoTempMin,speedWind,windDirection,windComment,humidity,pression;
    private ImageView itemWeather;
    private RelativeLayout background;
    private Switch tempChange;
    private SearchView searchLocation;
    private final String apiKeyWeather = "da6374c3822465ee254f6e599a7fce5e";
    private final String apiKeyGeo = "b9e655165a164c9c913ee04473920bbc";
    private String lon;
    private String lat;
    private String searchCity;
    private String urlIp = "https://api.apibundle.io/ip-lookup?apikey="+apiKeyGeo;
    private String url = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&appid="+apiKeyWeather+"&lang=fr";
    private String geoloc = "https://api.openweathermap.org/geo/1.0/reverse?lat="+lat+"&lon="+lon+"&limit=5&appid="+apiKeyWeather+"&lang=fr";
    public  void geoLocal(String geoUrl){
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
    }
    public void localisation(String newUrl){
    // Formulate the request and handle the response.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
    new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject  jsonRootObject = new JSONObject(response);
                String degre = jsonRootObject.optJSONObject("main").getString("temp");
                String tempMax = jsonRootObject.optJSONObject("main").getString("temp_max");
                String tempMin = jsonRootObject.optJSONObject("main").getString("temp_min");
                String humidite = jsonRootObject.optJSONObject("main").getString("humidity");
                String press = jsonRootObject.optJSONObject("main").getString("pressure");
                String wind = jsonRootObject.optJSONObject("wind").getString("speed");


                JSONArray tabW =  jsonRootObject.optJSONArray("weather");
                JSONObject jsonObject = tabW.getJSONObject(0);
                String main = jsonObject.getString("main");
                //String icone = jsonObject.getString("icon");
                String comment = jsonObject.getString("description");
                double directionWind = parseDouble(jsonRootObject.optJSONObject("wind").getString("deg"));

                double tempActuDouble = parseDouble(degre);
                double tempMaxDouble = parseDouble(tempMax);
                double tempMinDouble = parseDouble(tempMin);
                double tempActu = (tempActuDouble * (9/5)) + 32;
                double convTempMax = (tempMaxDouble * (9/5)) + 32;
                double convTempMin = (tempMinDouble * (9/5)) +32;
                double convertWind = parseDouble(wind);
                double realWind = (convertWind * 3.6)/1000;

                tempChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            temp.setText(String.valueOf(tempActu).substring(0,2)+" F");
                            infoTempMax.setText(String.valueOf(convTempMax).substring(0,5).replace(".",",") +" F");
                            infoTempMin.setText(String.valueOf(convTempMin).substring(0,5).replace(".",",") +" F");

                        }else{
                            temp.setText(String.valueOf(Math.round(tempActuDouble)).replace(".",",")+"°");
                            infoTempMax.setText(String.valueOf(Math.round(tempMaxDouble)).replace(".",",")+"°");
                            infoTempMin.setText(String.valueOf(Math.round(tempMinDouble)).replace(".",",")+"°");
                        }
                    }
                });


                paramsCss(main);
                roseDesVents(directionWind);



                temp.setText(String.valueOf(Math.round(tempActuDouble)).replace(".",",")+"°");
                infoTempMax.setText(String.valueOf(Math.round(tempMaxDouble)).replace(".",",")+"°");
                infoTempMin.setText(String.valueOf(Math.round(tempMinDouble)).replace(".",",")+"°");
                speedWind.setText("Vitesse du vent: "+String.valueOf(realWind).replace(".",",").substring(0,5)+" km/h");
                windComment.setText(comment);
                humidity.setText("Humidité: "+humidite+"%");
                pression.setText("Pression: "+press+" hPa");
            }catch (JSONException e) {
                temp.setText("pas fonctionné");
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
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
}
    public void searchByCity(){
        searchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCity = query;
                city.setText(premLettreMajuscule(searchCity));
                url = "https://api.openweathermap.org/data/2.5/weather?q="+searchCity.toLowerCase()+"&appid="+apiKeyWeather+"&lang=fr&units=metric";
                StringRequest stringSearch = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                localisation(url);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringSearch);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //city.setText("textChange: "+newText);
                return true;
            }
        });
    }
    public void roseDesVents(Double vent){
        if(vent == 0){
            windDirection.setText("Vent - N");
        } else if (vent > 0 && vent <= 22.5) {
            windDirection.setText("Vent - NNE");
        }else if (vent > 22.5 && vent <= 45) {
            windDirection.setText("Vent - NE");
        }else if (vent > 45 && vent <= 67.5) {
            windDirection.setText("Vent - ENE");
        }else if (vent > 67.5 && vent <= 90) {
            windDirection.setText("Vent - E");
        }else if (vent > 90 && vent <= 112.5) {
            windDirection.setText("Vent - ESE");
        }else if (vent > 112.5 && vent <= 135) {
            windDirection.setText("Vent - SE");
        }else if (vent > 135 && vent <= 157.5) {
            windDirection.setText("Vent - SSE");
        }else if (vent > 157.5 && vent <= 180) {
            windDirection.setText("Vent - S");
        }else if (vent > 180 && vent <= 202.5) {
            windDirection.setText("Vent - SSO");
        }else if (vent > 202.5 && vent <= 225) {
            windDirection.setText("Vent - SO");
        }else if (vent > 225 && vent <= 247.5) {
            windDirection.setText("Vent - OSO");
        }else if (vent > 247.5 && vent <= 270) {
            windDirection.setText("Vent - O");
        }else if (vent > 270 && vent <= 292.5) {
            windDirection.setText("Vent - ONO");
        }else if (vent > 292.5 && vent <= 315) {
            windDirection.setText("Vent - NO");
        }else if (vent > 315 && vent <= 337.5) {
            windDirection.setText("Vent - NNO");
        }else if (vent > 337.5 ) {
            windDirection.setText("Vent - N");
        }
    }

    public void paramsCss(String skyState){
        switch (skyState){
            case "Thunderstorm":
                itemWeather.setImageResource(R.drawable.thunder);
                background.setBackgroundResource(R.drawable.thunder_back);
                city.setTextColor(Color.parseColor("#FFFFFF"));
                temp.setTextColor(Color.parseColor("#FFFFFF"));
                tempChange.setTextColor(Color.parseColor("#FFFFFF"));
                speedWind.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMax.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMin.setTextColor(Color.parseColor("#FFFFFF"));
                humidity.setTextColor(Color.parseColor("#FFFFFF"));
                pression.setTextColor(Color.parseColor("#FFFFFF"));
                windDirection.setTextColor(Color.parseColor("#FFFFFF"));
                windComment.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Drizzle":
                itemWeather.setImageResource(R.drawable.drizzle);
                background.setBackgroundResource(R.drawable.drizzle_back);
                city.setTextColor(Color.parseColor("#FFFFFF"));
                temp.setTextColor(Color.parseColor("#FFFFFF"));
                tempChange.setTextColor(Color.parseColor("#FFFFFF"));
                speedWind.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMax.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMin.setTextColor(Color.parseColor("#FFFFFF"));
                humidity.setTextColor(Color.parseColor("#FFFFFF"));
                pression.setTextColor(Color.parseColor("#FFFFFF"));
                windDirection.setTextColor(Color.parseColor("#FFFFFF"));
                windComment.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Rain":
                itemWeather.setImageResource(R.drawable.drizzle);
                background.setBackgroundResource(R.drawable.rain_back);
                city.setTextColor(Color.parseColor("#FFFFFF"));
                temp.setTextColor(Color.parseColor("#FFFFFF"));
                tempChange.setTextColor(Color.parseColor("#FFFFFF"));
                speedWind.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMax.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMin.setTextColor(Color.parseColor("#FFFFFF"));
                humidity.setTextColor(Color.parseColor("#FFFFFF"));
                pression.setTextColor(Color.parseColor("#FFFFFF"));
                windDirection.setTextColor(Color.parseColor("#FFFFFF"));
                windComment.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Snow":
                itemWeather.setImageResource(R.drawable.snow);
                background.setBackgroundResource(R.drawable.snow_back);
                city.setTextColor(Color.parseColor("#FF000000"));
                temp.setTextColor(Color.parseColor("#FF000000"));
                tempChange.setTextColor(Color.parseColor("#FF000000"));
                speedWind.setTextColor(Color.parseColor("#FF000000"));
                infoTempMax.setTextColor(Color.parseColor("#FF000000"));
                infoTempMin.setTextColor(Color.parseColor("#FF000000"));
                humidity.setTextColor(Color.parseColor("#FF000000"));
                pression.setTextColor(Color.parseColor("#FF000000"));
                windDirection.setTextColor(Color.parseColor("#FF000000"));
                windComment.setTextColor(Color.parseColor("#FF000000"));
                break;
            case "Atmosphere":
                itemWeather.setImageResource(R.drawable.atmosphere);
                background.setBackgroundResource(R.drawable.atmosphere_back);
                city.setTextColor(Color.parseColor("#FFFFFF"));
                temp.setTextColor(Color.parseColor("#FFFFFF"));
                tempChange.setTextColor(Color.parseColor("#FFFFFF"));
                speedWind.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMax.setTextColor(Color.parseColor("#FFFFFF"));
                infoTempMin.setTextColor(Color.parseColor("#FFFFFF"));
                humidity.setTextColor(Color.parseColor("#FFFFFF"));
                pression.setTextColor(Color.parseColor("#FFFFFF"));
                windDirection.setTextColor(Color.parseColor("#FFFFFF"));
                windComment.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case "Clear":
                itemWeather.setImageResource(R.drawable.clear);
                background.setBackgroundResource(R.drawable.new_sky);
                city.setTextColor(Color.parseColor("#FF000000"));
                temp.setTextColor(Color.parseColor("#FF000000"));
                tempChange.setTextColor(Color.parseColor("#FF000000"));
                speedWind.setTextColor(Color.parseColor("#FF000000"));
                infoTempMax.setTextColor(Color.parseColor("#FF000000"));
                infoTempMin.setTextColor(Color.parseColor("#FF000000"));
                humidity.setTextColor(Color.parseColor("#FF000000"));
                pression.setTextColor(Color.parseColor("#FF000000"));
                windDirection.setTextColor(Color.parseColor("#FF000000"));
                windComment.setTextColor(Color.parseColor("#FF000000"));
                break;
            case "Clouds":
                itemWeather.setImageResource(R.drawable.clouds);
                background.setBackgroundResource(R.drawable.clouds_back);
                city.setTextColor(Color.parseColor("#FF000000"));
                temp.setTextColor(Color.parseColor("#FF000000"));
                tempChange.setTextColor(Color.parseColor("#FF000000"));
                speedWind.setTextColor(Color.parseColor("#FF000000"));
                infoTempMax.setTextColor(Color.parseColor("#FF000000"));
                infoTempMin.setTextColor(Color.parseColor("#FF000000"));
                humidity.setTextColor(Color.parseColor("#FF000000"));
                pression.setTextColor(Color.parseColor("#FF000000"));
                windDirection.setTextColor(Color.parseColor("#FF000000"));
                windComment.setTextColor(Color.parseColor("#FF000000"));
                break;
            default:
                break;
        }
    }
    public String premLettreMajuscule(String ville) {
        if (ville == null || ville.isEmpty()) {
            return ville;
        } else {
            return ville.substring(0, 1).toUpperCase() + ville.substring(1);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.city);
        temp = findViewById(R.id.temperature);
        tempChange = findViewById(R.id.switch1);
        infoTempMax = findViewById(R.id.tempMax);
        infoTempMin = findViewById(R.id.tempMin);
        speedWind = findViewById(R.id.speedWind);
        itemWeather = findViewById(R.id.itemWeather);
        windDirection = findViewById(R.id.windDirection);
        windComment = findViewById(R.id.windComment);
        searchLocation = findViewById(R.id.searchLocation);
        humidity = findViewById(R.id.humidity);
        pression = findViewById(R.id.pression);
        background = findViewById(R.id.item);

        StringRequest ip = new StringRequest(Request.Method.GET, urlIp,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject  jsonRootObject = new JSONObject(response);
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

        searchByCity();
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(ip);

        //
    }

}