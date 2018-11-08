
package com.example.vray.aptivinventory;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;




public class homePage extends AppCompatActivity {
    private DrawerLayout dl;
    private ActionBarDrawerToggle adt;
    private NavigationView nv;
    String data = "";

    public void viewInventory(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);


        dl = (DrawerLayout) findViewById(R.id.dl);
        adt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        adt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(adt);
        adt.syncState();
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.viewInventory) {
                    Intent intent = new Intent(homePage.this, Inventory.class);
                    viewInventory(intent);
                } else if (id == R.id.cuItem) {
                    Intent intent = new Intent(homePage.this, AddInventory.class);
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



