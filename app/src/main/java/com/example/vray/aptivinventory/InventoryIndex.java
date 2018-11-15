package com.example.vray.aptivinventory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InventoryIndex extends NavBar {

  private RecyclerView mList;

  private LinearLayoutManager linearLayoutManager;
  private DividerItemDecoration dividerItemDecoration;
  private List<Item> itemList;
  private RecyclerView.Adapter adapter;
  ServerCalls sc = new ServerCalls(this);
  JSONArray responseArray = new JSONArray();

  public void getAddItem(Intent intent) {
    startActivity(intent);
    finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
    //super.onRestoreInstanceState(savedInstanceState);
    super.addContentView(R.layout.activity_inventory_index);


    mList = findViewById(R.id.main_list);
    FloatingActionButton addItem = findViewById(R.id.addItem);

    itemList = new ArrayList<>();
    adapter = new ItemAdapter(getApplicationContext(), itemList);

    linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

    mList.setHasFixedSize(true);
    mList.setLayoutManager(linearLayoutManager);
    mList.addItemDecoration(dividerItemDecoration);
    mList.setAdapter(adapter);

    getInventory(sc);
    addItem.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(InventoryIndex.this, AddInventory.class);
        getAddItem(intent);
      }
    });

  }

  private void getInventory(final ServerCalls sc) {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading...");
    progressDialog.show();

    sc.httpGetJSON(MainActivity.url+"items", new ServerCalls.VolleyResponseListener() {
      @Override
      public void onError(String message) {
        Log.d("Get Error", message);
      }

      @Override
      public void onResponse(Object response) throws JSONException {
        responseArray = sc.httpParseJSON(response.toString());
        for (int i = 0; i < responseArray.length(); i++) {
          JSONObject jsonObject = responseArray.getJSONObject(i);
          Item item = new Item(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getDouble("price"),
                  jsonObject.getInt("quantity"), jsonObject.getInt("user_id"));
          itemList.add(item);
        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
      }
    });
  }
}
