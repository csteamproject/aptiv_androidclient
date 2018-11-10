package com.example.vray.aptivinventory;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InventoryIndex extends AppCompatActivity {

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Item> itemList;
    private RecyclerView.Adapter adapter;
    ServerCalls sc = new ServerCalls(this);
    JSONArray responseArray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_index);

        mList = findViewById(R.id.main_list);

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(getApplicationContext(),itemList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getInventory(sc);
    }

    private void getInventory(final ServerCalls sc) {
        final String url = "http://10.0.2.2:3000/items";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sc.httpGetJSON(url, new ServerCalls.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("Get Error", message);
            }

            @Override
            public void onResponse(Object response) throws JSONException {
                responseArray = sc.httpParseJSON(response.toString());
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject jsonObject = responseArray.getJSONObject(i);
                    Item item = new Item(jsonObject.getString("name"), jsonObject.getDouble("price"), jsonObject.getInt("quantity"));
                    itemList.add(item);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }
}
