package mentobile.restaurantdemo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Deepak Sharma on 8/2/2015.
 */
public class Application {

    public final static String URL = "http://mentobile.com/test/";
    public final static String SP_LOGIN_LOGOUT = "login_logout";



    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void setDataInSharedPreference(Activity activity, String spName, String key, String data) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static String getDataFromSharedPreference(Activity activity, String spName, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String val = (sharedPreferences.contains(key) ? sharedPreferences.getString(key, null) : null);
        return val;
    }

    public static void clearSharedPreferenceFile(Activity activity, String fileName) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

}