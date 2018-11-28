package com.example.vray.aptivinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        String serialNum = in.getExtras().getString("serialNumber");
        String brand = in.getExtras().getString("brand");
        String model = in.getExtras().getString("model");
        String operable = in.getExtras().getString("operable");

        final EditText name = findViewById(R.id.name);
        final EditText price = findViewById(R.id.itemid);
        final EditText quantity = findViewById(R.id.ticketid);
        final EditText serial = findViewById(R.id.description);
        final EditText brands = findViewById(R.id.brand);
        final EditText models = findViewById(R.id.model);
        final EditText ops = findViewById(R.id.operable);

        name.setText(itemName);
        price.setText(itemPrice.toString());
        quantity.setText(itemQuantity.toString());
        serial.setText(serialNum);
        brands.setText(brand);
        models.setText(model);
        ops.setText(operable);

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
              hashes[8] = "serial_number";
              hashes[9] = serial.getText().toString();
              hashes[10] = "brand";
              hashes[11] = brands.getText().toString();
              hashes[12] = "model";
              hashes[13] = models.getText().toString();
              hashes[14] = "operable";
              hashes[15] = ops.getText().toString();
              hashes[16] = "id";
              hashes[17] = itemid.toString();

              JSONObject jobj = sc.getJSONString(hashes, 0);
              String mRequestBody = jobj.toString();
              patchItem(sc, mRequestBody, itemid);
                Toast.makeText(EditItem.this, "Item updated successfully!",
                        Toast.LENGTH_LONG).show();
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
