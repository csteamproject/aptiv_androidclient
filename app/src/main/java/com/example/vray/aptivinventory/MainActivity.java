package com.example.vray.aptivinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  public void route(Intent intent) {
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_page);
    
    Toolbar mToolBar = findViewById(R.id.toolbar);
    setSupportActionBar(mToolBar);

    if(getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    final EditText user = findViewById(R.id.username);
    final EditText pass = findViewById(R.id.password);

    Button login = findViewById(R.id.login);
    login.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        getLogin(user.getText(), pass.getText());

      }
    });
  }


  private void getLogin(final Editable user, final Editable password) {

    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    final SharedPreferences.Editor edit = sharedPref.edit();

    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://10.0.2.2:3000/sessions";

    JsonObjectRequest req = new JsonObjectRequest
    (Request.Method.POST, url, (String) null, new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
      Log.d("JSON Response", response.toString());
      try {
        String saveToken = response.getString("token");
        String success = response.getString("success");
        String firstName = response.getString("first");
        String lastName = response.getString("last");
        String role = response.getString("role");

        edit.putString("token", saveToken);
        edit.putString("firstName", firstName);
        edit.putString("lastName", lastName);
        edit.putString("role", role);
        edit.apply();
        if (success.equals("true")) {
          Intent intent = new Intent(MainActivity.this, NavBar.class);
          route(intent);
        }
      } catch (Exception e) {
        Toast.makeText(MainActivity.this, "Login Failed!",
                Toast.LENGTH_LONG).show();
      }
    }
  }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
      Log.d("Volley Error", error.toString());
    }
  }) {
    @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("username", user.toString());
        headers.put("password", password.toString());
        return headers;
      }
    };
    queue.add(req);
  }
}

