package com.example.kanyoda;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class resultActivity extends AppCompatActivity implements SensorEventListener {

    RequestQueue queue;
    private SensorManager sensorManager;
    private Sensor gravity;
    private String kanye_text;
    private boolean sensorStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        queue = Volley.newRequestQueue(this);

        String text = getIntent().getStringExtra("text");
        Boolean kanye = getIntent().getBooleanExtra("kanye", true);
        if (savedInstanceState == null) {
            if (kanye) {
                getKanye();
            } else {
                getYodaSpeak(text);
            }
        } else {
            this.kanye_text = savedInstanceState.getString("text", kanye_text);
            TextView result = (TextView)  findViewById(R.id.result_text);
            result.setText(kanye_text);
        }
        startSensor(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( sensorStarted ) {
            sensorManager.unregisterListener(this);
            this.sensorStarted = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( sensorStarted != true ) {
            startSensor(null);
        }
    }

    @Override
    public void onSaveInstanceState( Bundle saveInstanceState ){
        super.onSaveInstanceState(saveInstanceState);

        saveInstanceState.putString("text", kanye_text);
    }

    @Override
    public void onRestoreInstanceState(  Bundle savedInstanceState ) {
        super.onRestoreInstanceState(savedInstanceState);
        this.kanye_text = savedInstanceState.getString("text", kanye_text);
        TextView result = (TextView)  findViewById(R.id.result_text);
        result.setText(kanye_text);
    }

    public void startSensor(View view) {
        this.sensorStarted = true;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (gravity != null) {
            sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xSensor = event.values[0];
        ImageView yoda_image = (ImageView) findViewById(R.id.imageView);
        yoda_image.setRotation(xSensor*9);
        //TextView result = (TextView)  findViewById(R.id.result_text);
        //result.setText("X: " + xSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getKanye() {
        // Instantiate the RequestQueue
        String url ="https://api.kanye.rest";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //parseYodaResponse( response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String text = jsonObject.getString("quote");
                            kanye_text = text;
                            getYodaSpeak(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView result = (TextView)  findViewById(R.id.result_text);
                result.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    protected void getYodaSpeak(String input){
        // Instantiate the RequestQueue
        String url ="https://api.funtranslations.com/translate/yoda?text=" + input;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    parseYodaResponse( response );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TextView result = (TextView)  findViewById(R.id.result_text);
                    result.setText("That didn't work!");
                }
            });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseYodaResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String text = jsonObject.getJSONObject("contents").getString("translated");
            TextView result = (TextView)  findViewById(R.id.result_text);
            result.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
