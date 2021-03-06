package com.example.vray.aptivinventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ServerCalls {

  private Context mContext;

  ServerCalls(Context context) {
    mContext = context;
  }

  void httpSendJSON(final String mRequestBody, final String url, VolleyResponseListener get_error) {
    RequestQueue queue = Volley.newRequestQueue(mContext);

    JsonObjectRequest req = new JsonObjectRequest
            (Request.Method.POST, url, (String) null, new Response.Listener<JSONObject>() {
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
        HashMap<String, String> headers = new HashMap<>();
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

  void httpGetJSON(String url, final VolleyResponseListener listener) {

    RequestQueue queue = Volley.newRequestQueue(mContext);

    JsonObjectRequest req = new JsonObjectRequest
            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  listener.onResponse(response);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                try {
                  listener.onResponse(error.toString());
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }) {
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("jwt-token", getToken());
        return headers;
      }
    };
    queue.add(req);
  }

  void httpPatchJSON(final String mRequestBody, final String url, final VolleyResponseListener listener) {
    RequestQueue queue = Volley.newRequestQueue(mContext);

    JsonObjectRequest req = new JsonObjectRequest
            (Request.Method.PATCH, url, (String) null, new Response.Listener<JSONObject>() {
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
        HashMap<String, String> headers = new HashMap<>();
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

  void httpDelete(final String url, final VolleyResponseListener listener) {
    RequestQueue queue = Volley.newRequestQueue(mContext);

    JsonObjectRequest req = new JsonObjectRequest
            (Request.Method.DELETE, url, (String) null, new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  listener.onResponse(response);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                try {
                  listener.onResponse(error);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }) {
      @Override
      public String getBodyContentType() {
        return "application/json; charset=utf-8";
      }

      @Override
      public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("jwt-token", getToken());
        headers.put("Content-Type", getBodyContentType());
        return headers;
      }
    };
    queue.add(req);
  }

  JSONArray httpParseJSON(String parser) {
    JSONArray sArray = null;
    String split[] = parser.split("\\[");
    split = split[1].split("]");
    try {
      sArray = new JSONArray("[" + split[0] + "]");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return sArray;
  }

  private String getToken() {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    return sharedPrefs.getString("token", "");
  }

  public String getUserID() {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    return sharedPrefs.getString("userID","");
  }

  public String getLocationID() {
    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    return sharedPrefs.getString("locationID", "");
  }

  public JSONObject getJSONString(String[] hashes, int offset) throws JSONException {
    final JSONObject JSONHash = new JSONObject();
    String collection = "";

    for (String hash : hashes) {
      if (hash == null){
        return JSONHash;
      }
      if (!collection.equals("")) {
        JSONHash.put(collection, hash);
        collection = "";
        offset++;
      } else if (hash.contains("_attributes")) {
        String[] newArray = Arrays.copyOfRange(hashes, offset + 1, hashes.length);
        return JSONHash.put(hash, getJSONString(newArray, offset));
      } else {
        collection = hash;
        offset++;
      }
    }
    return JSONHash;
  }

  public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response) throws JSONException;
  }


}