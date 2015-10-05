package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import mentobile.utils.WebService;


public class LoginActivity1 extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "LoginActivity";
    private Button btnLogin;
    private Button btnSignup;
    private ImageView btnFacebook;
    private ImageView btnGoogle;
    private EditText edUserName;
    private EditText edPassword;
    private TextView tvForgetPass;
    private ArrayList<NameValuePair> listValue;
    WebService webService;
    private CProgressDialog cProgressDialog;
    public static LoginActivity1 loginActivity;
    public static int LOGIN_TYPE = 0;

    public LoginActivity1() {
        // Required empty public constructor
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }

        //Facebook

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);

        //EndFacebook
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        profileTracker.stopTracking();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // facebook sdk initialize
        this.savedInstanceState = savedInstanceState;
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        // End facebook initialize

        setContentView(R.layout.activity_login1);
        loginActivity = this;
        cProgressDialog = new CProgressDialog(LoginActivity1.this);
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(this);
        btnSignup = (Button) findViewById(R.id.login_btn_signup);
        btnSignup.setOnClickListener(this);

        edUserName = (EditText) findViewById(R.id.login_ed_username);
        edPassword = (EditText) findViewById(R.id.login_ed_password);

        tvForgetPass = (TextView) findViewById(R.id.login_tv_forgetpassword);
        tvForgetPass.setOnClickListener(this);

        btnGoogle = (ImageView) findViewById(R.id.login_btn_google);
        btnGoogle.setOnClickListener(this);

        btnFacebook = (ImageView) findViewById(R.id.login_btn_facebook);
        btnFacebook.setOnClickListener(this);

        webService = new WebService();
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
                            JSONObject json = webService.makeHttpRequest("signup", listValue);
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
                                Application.setDataInSharedPreference(LoginActivity1.this, Application.SP_LOGIN_LOGOUT, "email",
                                        edUserName.getText().toString().trim());
                                Intent intent = new Intent(LoginActivity1.this, MainActivity.class);
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
            case R.id.login_btn_facebook:
                facebookLogin();
                break;
            case R.id.login_btn_google:
                googlePlusLogin();
                break;
            case R.id.login_tv_forgetpassword:
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
            JSONObject json = webService.makeHttpRequest("signup", listValue);
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
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        signedInUser = false;
        Toast.makeText(this, "G+ Connected", Toast.LENGTH_LONG).show();
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                LOGIN_TYPE = 2;
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Profile.getProfile().setFullName(currentPerson.getDisplayName());
                Profile.getProfile().setEmailID(email);
//                Log.d(TAG, "::::Display Name " + currentPerson.getDisplayName() + " Email " + email);
                Application.setDataInSharedPreference(LoginActivity1.this, Application.SP_LOGIN_LOGOUT, "email", email);
                Intent intent = new Intent(LoginActivity1.this, MainActivity.class);
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
        callbackManager.onActivityResult(requestCode, responseCode, data);
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

    // Facebook integration

    private final String PENDING_ACTION_BUNDLE_KEY = "mentobile.restaurantdemo:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;
    private Bundle savedInstanceState;

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "mentobile.restaurantdemo", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    private void handlePendingAction() {

        PendingAction previouslyPendingAction = pendingAction;
        Log.d(TAG, ":::::PendingAction " + previouslyPendingAction.toString());
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction) {
            case NONE:
                break;
        }
    }

    public void facebokLogout() {
        LoginManager.getInstance().logOut();
    }

    public void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity1.this, Arrays.asList("basic_info", "email"));
//        btnFacebook.setReadPermissions(Arrays.asList("basic_info", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlePendingAction();
                final GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                Log.d("FacebookActivity ", ":::::FB " + jsonObject.toString());
                                LOGIN_TYPE = 3;
                                try {
                                    String email = (String) jsonObject.get("email");
                                    String name = (String) jsonObject.get("name");
                                    Profile.getProfile().setFullName(name);
                                    Profile.getProfile().setEmailID(email);
                                    Application.setDataInSharedPreference(LoginActivity1.this, Application.SP_LOGIN_LOGOUT, "email", email);
                                    Intent intent = new Intent(LoginActivity1.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    cProgressDialog.hide();
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {

                                }
                            }
                        });
                Bundle param = new Bundle();
                param.putString("fields", "id,name,email,gender, birthday");
                graphRequest.setParameters(param);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                if (pendingAction != PendingAction.NONE) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
            }

            @Override
            public void onError(FacebookException exception) {
                if (pendingAction != PendingAction.NONE
                        && exception instanceof FacebookAuthorizationException) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                    Log.d(TAG, ":::::OnError");
                }
            }

            private void showAlert() {
                new AlertDialog.Builder(LoginActivity1.this)
                        .setTitle(R.string.cancelled)
                        .setMessage(R.string.permission_not_granted)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(com.facebook.Profile oldProfile, com.facebook.Profile currentProfile) {
//                // It's possible that we were waiting for Profile to be populated in order to
//                // post a status update.
//                handlePendingAction();
//            }
//        };
    }
}
