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

public class EditTicket extends NavBar {

    int VALUES = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_edit_item);
        Intent in = getIntent();
        FloatingActionButton additem = findViewById(R.id.additem);
        final ServerCalls sc = new ServerCalls(this);

        String itemName = in.getStringExtra("title");

        final Integer itemid = in.getExtras().getInt("id");
        final Integer priority = in.getExtras().getInt("priority");

        final String description = in.getStringExtra("description");
        final String status = in.getStringExtra("status");

        final EditText name = findViewById(R.id.name);
        final EditText price = findViewById(R.id.price);
        final EditText quantity = findViewById(R.id.quantity);

        name.setText(itemName);
        price.setText(description);
        quantity.setText(itemid.toString());

        final String[] hashes = new String[VALUES];

        additem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    hashes[0] = "id";
                    hashes[1] = quantity.getText().toString();
                    hashes[2] = "title";
                    hashes[3] = name.getText().toString();
                    hashes[4] = "status";
                    hashes[5] = status;
                    hashes[6] = "priority";
                    hashes[7] = priority.toString();
                    hashes[8] = "description";
                    hashes[9] = price.getText().toString();

                    JSONObject jobj = sc.getJSONString(hashes, 0);
                    String mRequestBody = jobj.toString();
                    patchItem(sc, mRequestBody, itemid);
                    Toast.makeText(EditTicket.this, "Ticket updated successfully!",
                            Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void patchItem(ServerCalls sc, String mRequestBody, int itemid) {
        sc.httpPatchJSON(mRequestBody, MainActivity.url + "tickets/" + itemid, new ServerCalls.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("Get Error", message);
            }

            @Override
            public void onResponse(Object response) {
                Intent intent = new Intent(EditTicket.this, InventoryIndex.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
