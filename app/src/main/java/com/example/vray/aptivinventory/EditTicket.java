package com.example.vray.aptivinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditTicket extends NavBar {

    int VALUES = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_edit_ticket);
        Intent in = getIntent();
        FloatingActionButton additem = findViewById(R.id.additem);
        final ServerCalls sc = new ServerCalls(this);

        String itemName = in.getStringExtra("title");

        final Integer itemid = in.getExtras().getInt("itemid");
        final Integer priority = in.getExtras().getInt("priority");

        final String description = in.getStringExtra("description");
        final String status = in.getStringExtra("status");
        final Integer ticketid = in.getExtras().getInt("id");

        final EditText name = findViewById(R.id.name);
        final EditText itemids = findViewById(R.id.itemid);
        final EditText ticketids = findViewById(R.id.ticketid);
        final TextView descriptions = findViewById(R.id.description);
        final EditText statuses = findViewById(R.id.status);
        final EditText priorities = findViewById(R.id.priority);

        name.setText(itemName);
        itemids.setText(itemid.toString());
        ticketids.setText(ticketid.toString());
        descriptions.setText(description);
        statuses.setText(status);
        priorities.setText(priority.toString());


        final String[] hashes = new String[VALUES];

        additem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    hashes[0] = "id";
                    hashes[1] = ticketids.getText().toString();
                    hashes[2] = "title";
                    hashes[3] = name.getText().toString();
                    hashes[4] = "status";
                    hashes[5] = statuses.getText().toString();
                    hashes[6] = "priority";
                    hashes[7] = priorities.getText().toString();
                    hashes[8] = "description";
                    hashes[9] = descriptions.getText().toString();
                    hashes[10] = "item_id";
                    hashes[11] = itemids.getText().toString();

                    JSONObject jobj = sc.getJSONString(hashes, 0);
                    String mRequestBody = jobj.toString();
                    patchItem(sc, mRequestBody, ticketid);
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
