
package com.example.vray.aptivinventory;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;


import java.util.Objects;


public class homePage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle adt;
    private NavigationView nv;

    public void viewInventory(Intent intent) {
        startActivity(intent);
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
                    Intent intent = new Intent(homePage.this, InventoryIndex.class);
                    viewInventory(intent);
                } else if (id == R.id.cuItem) {
                    Intent intent = new Intent(homePage.this, AddInventory.class);
                    viewInventory(intent);
                }
                else if (id == R.id.home) {
                    Intent intent = new Intent(homePage.this, homePage.class);
                    viewInventory(intent);
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(adt.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



