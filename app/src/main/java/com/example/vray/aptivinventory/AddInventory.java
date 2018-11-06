package com.example.vray.aptivinventory;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddInventory extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_inventory);

    final EditText name = findViewById(R.id.name);
    final EditText price = findViewById(R.id.price);
    final EditText quantity = findViewById(R.id.quantity);
    final TextView flash = findViewById(R.id.flash);
    Button add = findViewById(R.id.add);
    add.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          sendItem(name.getText(), price.getText(), quantity.getText());
        } catch (JSONException e) {
          e.printStackTrace();
        }
        flash.setText("Successfully Added!");
      }
    });

  }

  public void sendItem(final Editable name, final Editable price, final Editable quantity) throws JSONException {
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://10.0.2.2:3000/items";

    final JSONObject innerHash = new JSONObject();
    innerHash.put("name", name.toString());
    innerHash.put("price", price.toString());
    innerHash.put("quantity", quantity.toString());
    final String mRequestBody = innerHash.toString();

    final JSONObject outerHash = new JSONObject();
    outerHash.put("item:", innerHash.toString());

    JsonObjectRequest req = new JsonObjectRequest
    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        Log.d("JSON Response", response.toString());
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d("Volley Error", error.toString());
      }
    }) {
      @Override
      public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("jwt-token", getToken());
        headers.put("Content-Type", getBodyContentType());
        return headers;
      }
      @Override
      public byte[] getBody() {
        try {
          return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
          VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
          return null;
        }
      }
    };
        queue.add(req);

  }

//  public void sendItem(final Editable name, final Editable price, final Editable quantity) {
//    RequestQueue queue = Volley.newRequestQueue(this);
//    String url = "http://10.0.2.2:3000/items";
//
//    JsonObjectRequest req = new JsonObjectRequest
//    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//      @Override
//      public void onResponse(JSONObject response) {
//        Log.d("JSON Response", response.toString());
//      }
//    }, new Response.ErrorListener() {
//      @Override
//      public void onErrorResponse(VolleyError error) {
//        Log.d("Volley Error", error.toString());
//      }
//    }) {
//      @Override
//      protected Map<String,String> getParams(){
//        Map<String,String> params = new HashMap<>();
//        params.put("name", name.toString());
//        params.put("quantity", quantity.toString());
//        params.put("price", price.toString());
//        Log.d("Params:", name.toString() + price.toString() + quantity.toString());
//
//        return params;
//      }
//      @Override
//      public Map<String, String> getHeaders() {
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("jwt-token", getToken());
//        return headers;
//      }
//    };
//  queue.add(req);
//  }

  private String getToken() {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPrefs.getString("token", "");
  }

}
