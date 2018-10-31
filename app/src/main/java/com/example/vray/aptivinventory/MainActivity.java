package com.example.vray.aptivinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public boolean success = false;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean s) {
        success = s;
    }

    public void getInventory(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLogin(user.getText(), pass.getText());
                setSuccess(true);
                Log.d("success value", "" + getSuccess());
                if (success) {
                    Log.d("success value", "" + getSuccess());
                    Intent intent = new Intent(MainActivity.this, Inventory.class);
                    getInventory(intent);
                }
            }
        });

    }

    private void getLogin(final Editable user, final Editable password) {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor edit = sharedPref.edit();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:3000/sessions";

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON Response", response.toString());
                        try {
                            String saveToken = response.getString("token");
                            String success = response.getString("success");
                            if (success.equals("true")) {
                                setSuccess(true);
                            }
                            edit.putString("token", saveToken);
                            edit.apply();
                            getToken();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("username", user.toString());
                headers.put("password", password.toString());
                return headers;
            }
        };
        queue.add(req);
    }

    public String getToken() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.getString("token", "");
    }
}

