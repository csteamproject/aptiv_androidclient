/*
Created by: Victor Lozoya
This program creates the navigation menu that will be shared among activities
it includes a menu with 3 options home, inventory, and consult/update item
it includes a method to add add an activity to the navigation menu
which can be called by any activity that needs to display the navigation menu
*/
package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;







public class NavBar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  private DrawerLayout dl;
  private ActionBarDrawerToggle adt;

  public void viewInventory(Intent intent) {
    startActivity(intent);
    finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nav_bar);


    dl = findViewById(R.id.dl);
    adt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
    adt.setDrawerIndicatorEnabled(true);
    dl.addDrawerListener(adt);
    Toolbar mToolBar = findViewById(R.id.toolbar);
    setSupportActionBar(mToolBar);

    Log.d("supportActionBar value", "" + getSupportActionBar());

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_view_list_black_24dp);
      adt.syncState();
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    final NavigationView nav_view = findViewById(R.id.nav_view);

    final String username = MainActivity.userName;
    nav_view.setNavigationItemSelectedListener(this);
    TextView profile = nav_view.getHeaderView(0).findViewById(R.id.user);
    profile.setText(username);


    nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();


        if (id == R.id.viewInventory) {
          Intent intent = new Intent(NavBar.this, InventoryIndex.class);
          viewInventory(intent);
        } else if (id == R.id.cuItem) {
          Intent intent = new Intent(NavBar.this, AddInventory.class);
          viewInventory(intent);
        } else if (id == R.id.home) {
          Intent intent = new Intent(NavBar.this, homePage.class);
          viewInventory(intent);
        }

        return true;
      }
    });

  }

  public void addContentView(int layoutId) {
    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View contentView = inflater.inflate(layoutId, null, false);
    dl.addView(contentView, 0);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (adt.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    return false;
  }


}



