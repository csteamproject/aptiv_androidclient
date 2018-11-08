package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddInventory extends AppCompatActivity {

  final int VALUES = 3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_inventory);

    final ServerCalls sc = new ServerCalls(this);
    final JSONObject JSONHash = new JSONObject();

    final EditText name = findViewById(R.id.name);
    final EditText price = findViewById(R.id.price);
    final EditText quantity = findViewById(R.id.quantity);
    final String[] vals = new String[VALUES];
    final String[] arr = new String[VALUES];

    final TextView flash = findViewById(R.id.flash);
    Button add = findViewById(R.id.additem);
    add.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Log.d("names:", name.getText().toString());

        vals[0] = name.getText().toString();
        vals[1] = price.getText().toString();
        vals[2] = quantity.getText().toString();

        arr[0] = "name";
        arr[1] = "price";
        arr[2] = "quantity";

        for (int i = 0; i < 3; i++) {
          try {
            JSONHash.put(arr[i], vals[i]);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        final String mRequestBody = JSONHash.toString();
        sendItem(sc, mRequestBody);
        flash.setText("Successfully Added!");
      }
    });

  }

  public String[] getEditStrings(final Editable[] vals) {
    String[] response = new String[vals.length];
    for (int i = 0; i < vals.length; i++) {
      response[i] = vals[i].toString();
    }
    return response;
  }

  public void sendItem(ServerCalls sc, String mRequestBody) {
    String url = "http://10.0.2.2:3000/items";
    Log.d("mrequest", mRequestBody);
    sc.httpSendJSON(mRequestBody, url);
  }
}
