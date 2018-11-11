package com.example.vray.aptivinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AddInventory extends AppCompatActivity {

  private DrawerLayout dl;
  private ActionBarDrawerToggle adt;
  private NavigationView nv;

  public void viewInventory(Intent intent) {
    startActivity(intent);
  }

  final int VALUES = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_inventory);

    dl = (DrawerLayout) findViewById(R.id.dl);
    adt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
    adt.setDrawerIndicatorEnabled(true);
    dl.addDrawerListener(adt);
    Toolbar mToolBar = findViewById(R.id.toolbar);
    setSupportActionBar(mToolBar);

    Log.d("supportActionBar value", "" + getSupportActionBar());

    if(getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_view_list_black_24dp);
      adt.syncState();
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    final NavigationView nav_view = findViewById(R.id.nav_view);

    nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.viewInventory) {
          Intent intent = new Intent(AddInventory.this, Inventory.class);
          viewInventory(intent);
        } else if (id == R.id.cuItem) {
          Intent intent = new Intent(AddInventory.this, AddInventory.class);
          viewInventory(intent);
        }
        else if (id == R.id.home) {
          Intent intent = new Intent(AddInventory.this, homePage.class);
          viewInventory(intent);
        }
        return true;
      }
    });

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
    Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);

    ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.item_array,
            android.R.layout.simple_spinner_item);

    staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    staticSpinner.setAdapter(staticAdapter);

    staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

      @Override
              public void onItemSelected(AdapterView<?> parent, View view, int postition, long id){



              switch (postition){
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
                  break;

                case 2:
                  name.setVisibility(View.VISIBLE);
                  quantity.setVisibility(View.VISIBLE);
                  price.setVisibility(View.VISIBLE);
                  cpu.setVisibility(View.INVISIBLE);
                  ram.setVisibility(View.INVISIBLE);
                  hdd.setVisibility(View.INVISIBLE);
                  break;

                case 3:
                  name.setVisibility(View.VISIBLE);
                  quantity.setVisibility(View.VISIBLE);
                  price.setVisibility(View.VISIBLE);
                  cpu.setVisibility(View.INVISIBLE);
                  ram.setVisibility(View.INVISIBLE);
                  hdd.setVisibility(View.INVISIBLE);
                  break;

                case 4:
                  name.setVisibility(View.VISIBLE);
                  quantity.setVisibility(View.VISIBLE);
                  price.setVisibility(View.VISIBLE);
                  cpu.setVisibility(View.INVISIBLE);
                  ram.setVisibility(View.INVISIBLE);
                  hdd.setVisibility(View.INVISIBLE);
                  break;
              }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }


    });


    final ServerCalls sc = new ServerCalls(this);

    final String[] hashes = new String[VALUES*2-1];

    final TextView flash = findViewById(R.id.flash);
    //Button add = findViewById(R.id.additem);
    FloatingActionButton add = (FloatingActionButton) findViewById(R.id.additem);
    add.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

        hashes[0] = "name";
        hashes[1] = name.getText().toString();

        hashes[2] = "price";
        hashes[3] = price.getText().toString();

        hashes[4] = "quantity";
        hashes[5] = quantity.getText().toString();

        hashes[6] = "computer_attributes";
        hashes[7] = "utag";
        hashes[8] = "U578945";/**
        hashes[9] = "cpu";
        hashes[10] = cpu.getText().toString();
        hashes[11] = "ram";
        hashes[12] = ram.getText().toString();
        hashes[13] = "hdd";
        hashes[14] = hdd.getText().toString();**/

        String mRequestBody = "";
        try {
            mRequestBody = getJSONString(hashes, 0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendItem(sc, mRequestBody);
        flash.setText("Successfully Added!");
      }
    });

  }

  public void sendItem(ServerCalls sc, String mRequestBody) {
    String url = "http://10.0.2.2:3000/items";

    sc.httpSendJSON(mRequestBody, url, new ServerCalls.VolleyResponseListener() {
      @Override
      public void onError(String message) {
        Log.d("Get Error", message);
      }

      @Override
      public void onResponse(Object response) {
        Log.d("POST ITEM", response.toString());
      }
    });
  }

  public JSONObject getJSONString(String[] hashes, int offset) throws JSONException {
    final JSONObject JSONHash = new JSONObject();
    String collection = "";

    for (String hash : hashes) {
      if (!collection.equals("")) {
        JSONHash.put(collection, hash);
        collection = "";
        offset++;
      } else if (hash.contains("_attributes")) {
        String[] newArray = Arrays.copyOfRange(hashes, offset+1, hashes.length);
        return JSONHash.put(hash, getJSONString(newArray, offset));
      } else {
        collection = hash;
        offset++;
      }
    }
    return JSONHash;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(adt.onOptionsItemSelected(item)){
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
