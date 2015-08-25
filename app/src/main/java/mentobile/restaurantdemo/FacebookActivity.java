package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacebookActivity extends Activity implements View.OnClickListener {

    String TAG = "FaceBookActivity";
    private final String PENDING_ACTION_BUNDLE_KEY = "mentobile.restaurantdemo:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                LoginManager.getInstance().logOut();
                Log.d(TAG,"::::::::::Logout ");
                break;
            case R.id.btn_fb_login:
                Log.d(TAG,"::::::::::Login ");
                break;
        }
        Log.d(TAG,"::::::FB Button Click");
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // showHashKey(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook);
        AppEventsLogger.activateApp(this);
        Button btnLogout = (Button)findViewById(R.id.logout);
        btnLogout.setOnClickListener(this);

        final LoginButton btn_Login = (LoginButton) findViewById(R.id.btn_fb_login);
        btn_Login.setReadPermissions(Arrays.asList("basic_info", "email"));
        btn_Login.setOnClickListener(this);
        btn_Login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlePendingAction();
                final GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                Log.d("FacebookActivity ", ":::::FB " + jsonObject.toString());
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
                new AlertDialog.Builder(FacebookActivity.this)
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
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
                handlePendingAction();
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"::::::FB "+requestCode +"   :::::  "+resultCode);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void handlePendingAction() {

        PendingAction previouslyPendingAction = pendingAction;
        Log.d(TAG, ":::::PendingAction "+previouslyPendingAction.toString() );
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction) {
            case NONE:
                break;
        }
    }







}
