package com.example.vray.aptivinventory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
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
import static com.example.vray.aptivinventory.ItemAdapter.list;

public class InventoryIndex extends NavBar implements SearchView.OnQueryTextListener {

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
          if(jsonObject.isNull("price")){ jsonObject.put("price", 0); }
          if(jsonObject.isNull("serial_number")){ jsonObject.put("serial_number", ""); }
          if(jsonObject.isNull("brand")){ jsonObject.put("brand", ""); }
          if(jsonObject.isNull("model")){ jsonObject.put("model", ""); }
          if (jsonObject.isNull("operable")) {
            jsonObject.put("operable", "Operable");
          }



          Item item = new Item(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getDouble("price"),
                  jsonObject.getInt("quantity"), jsonObject.getInt("user_id"), jsonObject.getString("serial_number"),
                  jsonObject.getString("brand"), jsonObject.getString("model"), jsonObject.getString("operable"));
          itemList.add(item);
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
    ArrayList<Item> newList = new ArrayList<>();
    for(Item item : itemList){
      String name = item.getName().toLowerCase();
      if(name.contains(newText))
        newList.add(item);
    }
    setFilter(newList);
    return true;
  }

  public void setFilter(ArrayList<Item> newList){
    list = new ArrayList<>();
    list.addAll(newList);
    adapter.notifyDataSetChanged();
  }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){


      private ColorDrawable background = new ColorDrawable(Color.RED);
      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        Toast.makeText(InventoryIndex.this, "on move", Toast.LENGTH_SHORT).show();
        return false;
      }

      @Override
      public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final int position = viewHolder.getAdapterPosition();
            new android.support.v7.app.AlertDialog.Builder(InventoryIndex.this)
                    .setTitle("Are you sure you want to delete this item?")
                    .setMessage("Once this is done it cannot easily be restored.")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        ServerCalls sc = new ServerCalls(InventoryIndex.this);
                        sc.httpDelete(MainActivity.url + "items/" + list.get(position).getItemid(), new ServerCalls.VolleyResponseListener() {
                          @Override
                          public void onError(String message) {
                            Log.d("Get Error", message);
                          }

                          @Override
                          public void onResponse(Object response) {
                            Item item = list.get(position);
                            list.remove(position);
                            itemList.remove(item);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(InventoryIndex.this, "Item deleted!",
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

        Drawable icon = ContextCompat.getDrawable(InventoryIndex.this, R.drawable.ic_delete_forever_black_24dp);

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
