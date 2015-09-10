package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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


    public final static String MERCHANT_KEY = "MHWOrn";
    public final static String MERCHANT_SALT = "F0g0Bd5S";
    public final static String MERCHANT_ID = "5190740";
    public final static String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php";
    public final static String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php";


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


    public static void removeFragment(Fragment fragment, Activity activity) {
        if (fragment != null && activity != null) {
            try {
                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                activity.getFragmentManager().popBackStack();
                transaction.detach(fragment).commit();
            } catch (Exception e) {

            }
        }
    }

}
