package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mentobile.utils.JsonParser;


public class LoginActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "LoginActivity";
    private Button btnLogin;
    private Button btnSignup;
    private Button btnFacebook;
    private Button btnGoogle;
    private ImageButton imgBtnClose;
    private EditText edUserName;
    private EditText edPassword;
    private TextView tvForgetPass;
    private ArrayList<NameValuePair> listValue;
    JsonParser jsonParser;
    private CProgressDialog cProgressDialog;
    public static LoginActivity loginActivity;
    public static int LOGIN_TYPE = 0;

    public LoginActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = this;
        cProgressDialog = new CProgressDialog(LoginActivity.this);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.login_btn_signup);
        btnSignup.setOnClickListener(this);
        imgBtnClose = (ImageButton) findViewById(R.id.login_btn_close);
        imgBtnClose.setOnClickListener(this);

        edUserName = (EditText) findViewById(R.id.login_ed_username);
        edPassword = (EditText) findViewById(R.id.login_ed_password);

        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
        tvForgetPass.setOnClickListener(this);

        btnFacebook = (Button) findViewById(R.id.login_btn_facebook);
        btnFacebook.setOnClickListener(this);
        btnGoogle = (Button) findViewById(R.id.login_btn_google);
        btnGoogle.setOnClickListener(this);

        jsonParser = new JsonParser();
//        Log.d(TAG,":::::GoogleApiClienr "+mGoogleApiClient.isConnected());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        mGoogleApiClient.connect();
        Log.d(TAG, ":::::GoogleApiClient " + mGoogleApiClient.isConnected());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                String username = edUserName.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                Log.d(TAG, ":::::Username " + username);
                Log.d(TAG, ":::::Password " + password);
                if (username.length() < 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_username), Toast.LENGTH_SHORT).show();
                } else if (!Application.isValidEmail(username)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_email_verify), Toast.LENGTH_SHORT).show();
                } else if (password.length() < 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_password), Toast.LENGTH_SHORT).show();
                } else {
                    listValue = new ArrayList<NameValuePair>();
                    listValue.add(new BasicNameValuePair("email", username));
                    listValue.add(new BasicNameValuePair("pass", password));
                    listValue.add(new BasicNameValuePair("type", "1"));
                    Log.d("MainPage", ":::::ListValue " + listValue.toString());
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            cProgressDialog.setMessage("Please wait...");
                            cProgressDialog.show();
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            Log.d(TAG, "::::::Json 1");
                            JSONObject json = jsonParser.makeHttpRequest("signup", listValue);
                            Log.d(TAG, "::::::Json ");
                            try {
                                String success = json.getString("description");
                                return success;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return "Invalid";
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            cProgressDialog.hide();
                            Log.d(TAG, "::::::Result " + result);
                            if (result.equals("success")) {
                                LOGIN_TYPE = 1;
                                Profile.getProfile().setEmailID(edUserName.getText().toString().trim());
                                Application.setDataInSharedPreference(LoginActivity.this, Application.SP_LOGIN_LOGOUT, "email",
                                        edUserName.getText().toString().trim());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "" + result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();
                }
                break;
            case R.id.login_btn_signup:
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(android.R.id.content, new SignupFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.login_btn_close:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn_facebook:
                Intent intentFB = new Intent(this, FacebookActivity.class);
                startActivity(intentFB);
                googlePlusLogout();
                break;
            case R.id.login_btn_google:
                googlePlusLogin();
                break;
            case R.id.signin:

                break;
            case R.id.login_tv_forgetpassword:
                Log.d(TAG, ":::::::Text Click");
                forgetPassword();
                break;
        }
    }

    private void forgetPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.custom_forgotpassword);
        dialog.setTitle("Forgot Password");
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupWindowAnimation;
        final EditText edForgotPassword = (EditText) dialog.findViewById(R.id.forgotpass_ed_email);
        Button btnSubmit = (Button) dialog.findViewById(R.id.forgot_btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edForgotPassword.getText().toString().trim();
                Log.d(TAG, ":::::Username " + username);
                if (username.length() < 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_username), Toast.LENGTH_SHORT).show();
                } else if (!Application.isValidEmail(username)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_email_verify), Toast.LENGTH_SHORT).show();
                } else {
                    MyAsynchTask_ForgotPassword myAsynchTask_forgotPassword = new MyAsynchTask_ForgotPassword();
                    myAsynchTask_forgotPassword.execute(username, "4");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class MyAsynchTask_ForgotPassword extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cProgressDialog.setMessage(getString(R.string.progress_reset_password));
            cProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            listValue = new ArrayList<NameValuePair>();
            listValue.add(new BasicNameValuePair("email", params[0]));
            listValue.add(new BasicNameValuePair("type", params[1]));
            JSONObject json = jsonParser.makeHttpRequest("signup", listValue);
            try {
                String success = json.getString("description");
                return success;
            } catch (JSONException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            cProgressDialog.hide();
            Log.d(TAG, "::::::Result " + result);
            Toast.makeText(getApplicationContext(), "" + result, Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Google Login Connection Method
        ------------ Start  ---------------
     */

    private static final int RC_SIGN_IN = 0;

    // Google client to communicate with Google
    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;

    private void resolveSignInError() {

        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
                Log.d(TAG, ":::::Exception " + RC_SIGN_IN);
            }
        }
    }

    public void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        }
    }

    public void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            Toast.makeText(getApplicationContext(), "G+ Logout ", Toast.LENGTH_SHORT).show();
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            LOGIN_TYPE = 0;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        signedInUser = false;
        LOGIN_TYPE = 1;
        Toast.makeText(this, "G+ Connected", Toast.LENGTH_LONG).show();
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Profile.getProfile().setFullName(currentPerson.getDisplayName());
                Profile.getProfile().setEmailID(email);
//                Log.d(TAG, "::::Display Name " + currentPerson.getDisplayName() + " Email " + email);
                Application.setDataInSharedPreference(LoginActivity.this, Application.SP_LOGIN_LOGOUT, "email", email);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                cProgressDialog.hide();
                startActivity(intent);
                finish();
//                new LoadProfileImage(image).execute(personPhotoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = connectionResult;
            if (signedInUser) {
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
//        Log.d("GoogleActivity ", "::::RequestCode " + requestCode + " " + " Response " + responseCode);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                    cProgressDialog.setMessage("Please Wait...");
                    cProgressDialog.show();
                    if (!mGoogleApiClient.isConnecting()) {
                        mGoogleApiClient.connect();
                    }
                } else if (responseCode == RESULT_CANCELED) {
                    if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                    }
                }
//                Log.d("GoogleActivity ", ":::connection" + mGoogleApiClient.isConnecting() + " " + " value " + mIntentInProgress);
                mIntentInProgress = false;
                break;
        }
    }

    // download Google Account profile image, to complete profile
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView downloadedImage;

        public LoadProfileImage(ImageView image) {
            this.downloadedImage = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            downloadedImage.setImageBitmap(result);
        }
    }

      /*
        Google Login Connection Method
        ------------ End  ---------------
     */
}
