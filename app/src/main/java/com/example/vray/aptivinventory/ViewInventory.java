package com.example.vray.aptivinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout; //for code not implemented yet
import android.widget.TableRow;    //for code not implemented yet

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewInventory extends AppCompatActivity {
    String data = "";
    public void getAddItem(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        final EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getInventory();
                editText.setText(data);
            }
        });

        Button additem = findViewById(R.id.additem);
        additem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewInventory.this, AddInventory.class);
                getAddItem(intent);
            }
        });


    }

    private void getInventory() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:3000/items";

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jResponse = response.getJSONArray("id");

                            for (int count = 0; count < jResponse.length(); count++){
                                Log.d("Array_times",jResponse.get(count).toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error", error.toString());
                        data = error.toString();
                        data = data.replace("com.android.volley.ParseError:org.json.JSONException:Value", "");
                        //JSONParser parser = new JSONParser();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("jwt-token", getToken());
                return headers;
            }
        };
        queue.add(req);
    }

    //Pending implementation
  /*private void createRow( JSONArray x) {
    TableLayout tableLayout = new TableLayout(this);
    for (int i = 0; i < 10; i++) {
      TableRow tableRow = new TableRow(this);
      Button button = new Button(this);
      button.setText("1");
      tableRow.addView(button);

      button = new Button(this);
      button.setText("2");
      tableRow.addView(button);

      button = new Button(this);
      button.setText("3");
      tableRow.addView(button);

      tableLayout.addView(tableRow);
    }
    setContentView(tableLayout);
  }*/

    private String getToken() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.getString("token", "");
    }
}
