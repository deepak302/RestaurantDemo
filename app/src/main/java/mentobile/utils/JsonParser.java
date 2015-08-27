package mentobile.utils;

/**
 * Created by Deepak Sharma on 7/27/2015.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import mentobile.restaurantdemo.Application;

public class JsonParser {

    final String TAG = "JsonParser";
    static JSONObject jObj = null;
    static String json = "";
    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    // constructor
    public JsonParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String methodname, List<NameValuePair> params) {

        // Making HTTP request
        try {

            // check for request method
//            if (method == "POST") {
            // request method is POST
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Application.URL + methodname + ".php");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
//            } else if (method == "GET") {
//                // request method is GET
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(params, "utf-8");
//                url += "?" + paramString;
//                Log.d(TAG,":::::URL"+url);
//                HttpGet httpGet = new HttpGet(url);
//
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();
            Log.d("JSON ", ":::::::::JSon " + json.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "::::Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("MainPage", ":::::Success1 " + jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "::::Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;

    }
}