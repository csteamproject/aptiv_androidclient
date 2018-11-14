package com.example.vray.aptivinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class EditItem extends NavBar {

  int VALUES = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_edit_item);
        Intent in = getIntent();
        FloatingActionButton additem = findViewById(R.id.additem);
        final ServerCalls sc = new ServerCalls(this);

        String itemName = in.getStringExtra("name");

        final Integer itemid = in.getExtras().getInt("itemid");
        Integer itemQuantity = in.getExtras().getInt("quantity");

        Double itemPrice = in.getExtras().getDouble("price");

        final EditText name = findViewById(R.id.name);
        final EditText price = findViewById(R.id.price);
        final EditText quantity = findViewById(R.id.quantity);

        name.setText(itemName);
        price.setText(itemPrice.toString());
        quantity.setText(itemQuantity.toString());

        final String[] hashes = new String[VALUES];

        additem.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            try {
              hashes[0] = "name";
              hashes[1] = name.getText().toString();
              hashes[2] = "price";
              hashes[3] = price.getText().toString();
              hashes[4] = "quantity";
              hashes[5] = quantity.getText().toString();
              hashes[6] = "owner_id";
              hashes[7] = sc.getUserID();

              JSONObject jobj = sc.getJSONString(hashes, 0);
              String mRequestBody = jobj.toString();
              patchItem(sc, mRequestBody, itemid);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        });
    }

    public void patchItem(ServerCalls sc, String mRequestBody, int itemid) {
        sc.httpPatchJSON(mRequestBody, MainActivity.url + "items/" + itemid, new ServerCalls.VolleyResponseListener() {
        @Override
        public void onError(String message) {
          Log.d("Get Error", message);
        }

        @Override
        public void onResponse(Object response) {
          Intent intent = new Intent(EditItem.this, InventoryIndex.class);
          startActivity(intent);
          finish();
        }
      });
    }

}
