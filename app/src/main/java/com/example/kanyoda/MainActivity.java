package com.example.kanyoda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String text = savedInstanceState.getString("text");
            TextView input_text_field = (TextView)  findViewById(R.id.text_input);
            input_text_field.setText(text);

        }
    }

    @Override
    public void onSaveInstanceState( Bundle saveInstanceState ){
        super.onSaveInstanceState(saveInstanceState);
        TextView input_text_field = (TextView)  findViewById(R.id.text_input);
        String input_text = input_text_field.getText().toString();
        saveInstanceState.putString("text", input_text);
    }

    @Override
    public void onRestoreInstanceState(  Bundle savedInstanceState ) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void openProfileYoda( View view ){
        TextView input_text_field = (TextView)  findViewById(R.id.text_input);

        String input_text = input_text_field.getText().toString();

        Intent openProfileIntent = new Intent(this, resultActivity.class);
        openProfileIntent.putExtra("text", input_text);
        openProfileIntent.putExtra("kanye", false);
        startActivity( openProfileIntent );
    }

    public void openProfileKanye( View view ){

        Intent openProfileIntent = new Intent(this, resultActivity.class);
        openProfileIntent.putExtra("text", "");
        openProfileIntent.putExtra("kanye", true);
        startActivity( openProfileIntent );


    }


}
