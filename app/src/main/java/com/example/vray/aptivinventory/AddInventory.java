package com.example.vray.aptivinventory;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AddInventory extends NavBar {

  int VALUES = 13;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.addContentView(R.layout.activity_add_inventory);

    final EditText name = findViewById(R.id.name);
    name.setVisibility(View.INVISIBLE);
    final EditText price = findViewById(R.id.price);
    price.setVisibility(View.INVISIBLE);
    final EditText quantity = findViewById(R.id.quantity);
    quantity.setVisibility(View.INVISIBLE);
    final EditText cpu = findViewById(R.id.cpu);
    cpu.setVisibility(View.INVISIBLE);
    final EditText ram = findViewById(R.id.ram);
    ram.setVisibility(View.INVISIBLE);
    final EditText hdd = findViewById(R.id.hdd);
    hdd.setVisibility(View.INVISIBLE);
    Spinner staticSpinner = findViewById(R.id.static_spinner);

    ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.item_array,
            android.R.layout.simple_spinner_item);

    staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    staticSpinner.setAdapter(staticAdapter);

    staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
          case 0:
            name.setVisibility(View.INVISIBLE);
            price.setVisibility(View.INVISIBLE);
            quantity.setVisibility(View.INVISIBLE);
            cpu.setVisibility(View.INVISIBLE);
            ram.setVisibility(View.INVISIBLE);
            hdd.setVisibility(View.INVISIBLE);
            break;

          case 1:
            name.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            cpu.setVisibility(View.VISIBLE);
            ram.setVisibility(View.VISIBLE);
            hdd.setVisibility(View.VISIBLE);
            VALUES = 13;
            break;

          case 2:
            name.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            cpu.setVisibility(View.INVISIBLE);
            ram.setVisibility(View.INVISIBLE);
            hdd.setVisibility(View.INVISIBLE);
            VALUES = 6;
            break;

          case 3:
            name.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            cpu.setVisibility(View.INVISIBLE);
            ram.setVisibility(View.INVISIBLE);
            hdd.setVisibility(View.INVISIBLE);
            VALUES = 6;
            break;

          case 4:
            name.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
            cpu.setVisibility(View.INVISIBLE);
            ram.setVisibility(View.INVISIBLE);
            hdd.setVisibility(View.INVISIBLE);
            VALUES = 6;
            break;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }

    });


    final ServerCalls sc = new ServerCalls(this);

    final String[] hashes = new String[VALUES];

    FloatingActionButton add = findViewById(R.id.additem);
    add.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

        hashes[0] = "name";
        hashes[1] = name.getText().toString();

        hashes[2] = "price";
        hashes[3] = price.getText().toString();

        hashes[4] = "quantity";
        hashes[5] = quantity.getText().toString();

        if (VALUES == 13) {
          hashes[6] = "computer_attributes";
          hashes[7] = "cpu";
          hashes[8] = cpu.getText().toString();
          hashes[9] = "ram";
          hashes[10] = ram.getText().toString();
          hashes[11] = "hdd";
          hashes[12] = hdd.getText().toString();
        }
        if ((!hashes[1].equals("")) && (!hashes[5].equals(""))){
          String mRequestBody = "";
        try {
          mRequestBody = getJSONString(hashes, 0).toString();
        } catch (JSONException e) {
          e.printStackTrace();
        }
        sendItem(sc, mRequestBody);
          Toast.makeText(AddInventory.this, "Item added successfully!",
                  Toast.LENGTH_LONG).show();
      }
        }

    });


    FloatingActionButton clearText = findViewById(R.id.clearText);
    clearText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        name.setText("");
        price.setText("");
        quantity.setText("");
        cpu.setText("");
        ram.setText("");
        hdd.setText("");
      }
    });

  }


  public void sendItem(ServerCalls sc, String mRequestBody) {
    //String url = "http://10.0.2.2:3000/items";
    String url = "https://aptiv-api.herokuapp.com/items";

    sc.httpSendJSON(mRequestBody, url, new ServerCalls.VolleyResponseListener() {
      @Override
      public void onError(String message) {
        Log.d("Get Error", message);
      }

      @Override
      public void onResponse(Object response) {
        Intent intent = new Intent(AddInventory.this, AddInventory.class);
        viewInventory(intent);
      }
    });
  }

  public JSONObject getJSONString(String[] hashes, int offset) throws JSONException {
    final JSONObject JSONHash = new JSONObject();
    String collection = "";

    for (String hash : hashes) {
      if (hash == null){
        return JSONHash;
      }
      if (!collection.equals("")) {
        JSONHash.put(collection, hash);
        collection = "";
        offset++;
      } else if (hash.contains("_attributes")) {
        String[] newArray = Arrays.copyOfRange(hashes, offset + 1, hashes.length);
        return JSONHash.put(hash, getJSONString(newArray, offset));
      } else {
        collection = hash;
        offset++;
      }
    }
    return JSONHash;
  }

}
