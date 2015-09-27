package mentobile.utils;

/**
 * Created by Deepak Sharma on 7/27/2015.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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

    public JSONObject makeHttpRequest(String methodname, List<NameValuePair> params) {

        // Making HTTP request
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Application.URL + methodname + ".php");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            Log.d(TAG,":::::URl "+httpPost.getURI());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.d(TAG,":::::URl1 "+httpResponse.getEntity());
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStreamReader isr = new InputStreamReader(httpEntity.getContent());
            BufferedReader reader = new BufferedReader(isr);
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