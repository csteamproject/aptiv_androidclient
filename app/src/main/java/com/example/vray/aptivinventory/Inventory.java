package com.example.vray.aptivinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout; //for code not implemented yet
import android.widget.TableRow;    //for code not implemented yet
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    Button button = findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
          getInventory(sc);
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

  private void getInventory(final ServerCalls sc) {
    final String url = "http://10.0.2.2:3000/items";

    sc.httpGetJSON(url, new ServerCalls.VolleyResponseListener() {
      @Override
      public void onError(String message) {
        Log.d("Get Error", message);
      }

      @Override
      public void onResponse(Object response) {
        responseArray = sc.httpParseJSON(response.toString());
        Log.d("items3", responseArray.toString());
        createRow(responseArray);
      }
    });
  }


  //Pending implementation
  private void createRow(JSONArray x) {
    TableLayout tableLayout = new TableLayout(this);
    for (int i = 0; i < x.length(); i++) {
      TableRow tableRow = new TableRow(this);
      TextView sectionText = new TextView(this);

      JSONObject jObj = null;
      try {
        jObj = x.getJSONObject(i);
      } catch (JSONException e) {
        e.printStackTrace();
      }

      try {
      sectionText.setText( jObj.getString("id") );
      } catch (JSONException e) {
        e.printStackTrace();
      }
      tableRow.addView(sectionText);

      sectionText = new TextView(this);
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

      tableLayout.addView(tableRow);
    }
    setContentView(tableLayout);
  }

}