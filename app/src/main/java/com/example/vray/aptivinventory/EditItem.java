package com.example.vray.aptivinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Integer value = getIntent().getExtras().getInt("itemid");
        Log.d("getEditItem", value.toString());

        final EditText idd = findViewById(R.id.itemid);
        idd.setText(value.toString());
    }
}
