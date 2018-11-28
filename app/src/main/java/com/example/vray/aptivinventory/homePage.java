package com.example.vray.aptivinventory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.vray.aptivinventory.ticketAdapter.list2;

public class homePage extends NavBar implements SearchView.OnQueryTextListener {

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<ticket> itemList;
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
        super.addContentView(R.layout.activity_ticket_index);


        mList = findViewById(R.id.main_list);
        FloatingActionButton addItem = findViewById(R.id.addItem);

        itemList = new ArrayList<>();
        adapter = new ticketAdapter(getApplicationContext(), itemList);

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
                Intent intent = new Intent(homePage.this, AddInventory.class);
                getAddItem(intent);
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mList);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.searchicon, menu);
        MenuItem menuItem = menu.findItem(R.id.action_serch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;

    }

    private void getInventory(final ServerCalls sc) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        sc.httpGetJSON(MainActivity.url+"tickets", new ServerCalls.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("Get Error", message);
            }

            @Override
            public void onResponse(Object response) throws JSONException {
                responseArray = sc.httpParseJSON(response.toString());
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject jsonObject = responseArray.getJSONObject(i);
                    //if(jsonObject.isNull("price")){ jsonObject.put("price", 0); }
                    ticket ticket = new ticket(jsonObject.getInt("id"), jsonObject.getString("title"), jsonObject.getString("status"),
                            jsonObject.getInt("priority"), jsonObject.getString("description"), jsonObject.getInt("item_id"));
                    itemList.add(ticket);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        ArrayList<ticket> newList = new ArrayList<>();
        for(ticket ticket : itemList){
            String name = ticket.getTitle().toLowerCase();
            if(name.contains(newText))
                newList.add(ticket);
        }
        setFilter(newList);
        return true;
    }

    public void setFilter(ArrayList<ticket> newList){
        list2 = new ArrayList<>();
        list2.addAll(newList);
        adapter.notifyDataSetChanged();
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){


        private ColorDrawable background = new ColorDrawable(Color.RED);
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            Toast.makeText(homePage.this, "on move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            final int position = viewHolder.getAdapterPosition();
            new android.support.v7.app.AlertDialog.Builder(homePage.this)
                    .setTitle("Are you sure you want to delete this item?")
                    .setMessage("Once this is done it cannot easily be restored.")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ServerCalls sc = new ServerCalls(homePage.this);
                            sc.httpDelete(MainActivity.url + "tickets/" + list2.get(position).getId(), new ServerCalls.VolleyResponseListener() {
                                @Override
                                public void onError(String message) {
                                    Log.d("Get Error", message);
                                }

                                @Override
                                public void onResponse(Object response) {
                                    ticket ticket = list2.get(position);
                                    list2.remove(position);
                                    itemList.remove(ticket);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(homePage.this, "Item deleted!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                            Log.d("MainActivity", "Aborting mission...");
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView rc, RecyclerView.ViewHolder vH, float dx, float dy, int actionState, boolean isCurrentlyActive ){

            Drawable icon = ContextCompat.getDrawable(homePage.this, R.drawable.ic_delete_forever_black_24dp);

            super.onChildDraw(c, rc, vH, dx, dy, actionState, isCurrentlyActive);

            View itemView = vH.itemView;
            int backgroundCornerOffset = 20;

            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if(dx < 0){
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int)dx) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            }

            else{
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(c);
            icon.draw(c);
        }
    };
}
