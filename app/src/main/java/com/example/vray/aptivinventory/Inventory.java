package com.example.vray.aptivinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class Inventory extends AppCompatActivity {

  String data = "";
  JSONArray responseArray;

  public void getAddItem(Intent intent) {
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);

    final ServerCalls sc = new ServerCalls(this);

    final EditText editText = findViewById(R.id.editText);
    Button button = findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          getInventory(sc);
        } catch (JSONException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
//        editText.setText(data);
      }
    });

    Button additem = findViewById(R.id.additem);
    additem.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(Inventory.this, AddInventory.class);
        getAddItem(intent);
      }
    });
  }

  private void getInventory(final ServerCalls sc) throws JSONException, InterruptedException {
    final String url = "http://10.0.2.2:3000/items";

    sc.httpGetJSON(url, new ServerCalls.VolleyResponseListener() {
      @Override
      public void onError(String message) {
        Log.d("items:", message);
      }

      @Override
      public void onResponse(Object response) {
        Log.d("items2", response.toString());
        responseArray = sc.httpParseJSON(response.toString());
        Log.d("items3", responseArray.toString());
        String [] columns =  {"id","name","price", "quantity"};
        createRow(responseArray, columns);
      }
    });

    //    for(int i = 0; i< items[0].length(); i++) {
//      JSONObject jsonObject = items[0].getJSONObject(i);
//      String jsonObjectAsString = jsonObject.toString();
//      Log.d("jsons", jsonObjectAsString);
//    }

  }


  //Generates the table with attributes
  private void createRow(JSONArray x, String [] columns) {
    TableLayout tableLayout = findViewById(R.id.targetTable);
    tableLayout.setBackgroundColor(Color.BLACK);

    // Generating Table column labels as first row
    TableRow tableRow = new TableRow(this);
    tableRow.setBackgroundColor(Color.BLACK);
    TextView sectionText;

    for (int j = 0; j < columns.length; j++ ) {
      sectionText = new TextView(this);
      sectionText.setTextColor(Color.WHITE);
      sectionText.setText(columns[j].toUpperCase());
      tableRow.addView(sectionText);
    }
    tableLayout.addView(tableRow);

    //generating table rows from JsonArray data using columns array
    for (int i = 0; i < x.length(); i++) {
      tableRow = new TableRow(this);
      tableRow.setBackgroundColor(Color.BLACK);

      JSONObject jObj = null;
      try {
        jObj = x.getJSONObject(i);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      for (int j = 0; j < columns.length; j++ ) {
        sectionText = new TextView(this);
        sectionText.setTextColor(Color.WHITE);
        try {
          sectionText.setText(jObj.getString(columns[j]));
        } catch (JSONException e) {
          e.printStackTrace();
        }
        tableRow.addView(sectionText);
      }

      /*sectionText = new TextView(this);
      try {
      sectionText.setText(jObj.getString("name"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      tableRow.addView(sectionText);

      sectionText = new TextView(this);
      try {
      sectionText.setText(jObj.getString("price"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      tableRow.addView(sectionText);

      sectionText = new TextView(this);
      try {
      sectionText.setText(jObj.getString("quantity"));
      } catch (JSONException e) {
        e.printStackTrace();
      }
      tableRow.addView(sectionText);
      */

      tableLayout.addView(tableRow);
    }
  }

}