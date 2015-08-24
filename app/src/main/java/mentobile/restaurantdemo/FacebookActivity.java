package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacebookActivity extends Activity {

    String TAG = "FaceBookActivity";
    private final String PENDING_ACTION_BUNDLE_KEY = "mentobile.restaurantdemo:PendingAction";

    //    private Button postStatusUpdateButton;
//    private Button postPhotoButton;
//    private ProfilePictureView profilePictureView;
//    private TextView greeting;
    private PendingAction pendingAction = PendingAction.NONE;
    //    private boolean canPresentShareDialog;
//    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
//    private ShareDialog shareDialog;


    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook);
        AppEventsLogger.activateApp(this);
        LoginButton btn_Login = (LoginButton) findViewById(R.id.btn_fb_login);
        btn_Login.setReadPermissions(Arrays.asList("user_friends"));

        btn_Login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlePendingAction();
                updateUI();
                Log.d(TAG, ":::::OnSuccess");
            }

            @Override
            public void onCancel() {
                if (pendingAction != PendingAction.NONE) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                Log.d(TAG, ":::::OnCancel");
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {
                if (pendingAction != PendingAction.NONE
                        && exception instanceof FacebookAuthorizationException) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                    Log.d(TAG, ":::::OnError");
                }
                updateUI();
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
                updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
                handlePendingAction();
            }
        };

//        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
//        greeting = (TextView) findViewById(R.id.greeting);

//        postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
//        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                onClickPostStatusUpdate();
//            }
//        });

//        postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
//        postPhotoButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                onClickPostPhoto();
//            }
//        });

        // Can we present the share dialog for regular links?
//        canPresentShareDialog = ShareDialog.canShow(
//                ShareLinkContent.class);

        // Can we present the share dialog for photos?
//        canPresentShareDialogWithPhotos = ShareDialog.canShow(
//                SharePhotoContent.class);
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

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

//        postStatusUpdateButton.setEnabled(enableButtons || canPresentShareDialog);
//        postPhotoButton.setEnabled(enableButtons || canPresentShareDialogWithPhotos);

        Profile profile = Profile.getCurrentProfile();

//        if (enableButtons && profile != null) {
//            profilePictureView.setProfileId(profile.getId());
//            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
//        } else {
//            profilePictureView.setProfileId(null);
//            greeting.setText(null);
//        }
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;
        Log.d(TAG, ":::::PendingAction " + previouslyPendingAction);
        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
//                postPhoto();
                break;
            case POST_STATUS_UPDATE:
//                postStatusUpdate();
                break;
        }
    }

//    private void onClickPostStatusUpdate() {
//        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
//    }

//    private void postStatusUpdate() {
//        Profile profile = Profile.getCurrentProfile();
//        ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                .setContentTitle("Hello Facebook")
//                .setContentDescription(
//                        "The 'Hello Facebook' sample  showcases simple Facebook integration")
//                .setContentUrl(Uri.parse("http://developers.facebook.com/docs/android"))
//                .build();
//        if (canPresentShareDialog) {
//            shareDialog.show(linkContent);
//        } else if (profile != null && hasPublishPermission()) {
//            ShareApi.share(linkContent, shareCallback);
//        } else {
//            pendingAction = PendingAction.POST_STATUS_UPDATE;
//        }
//    }

//    private void onClickPostPhoto() {
//        performPublish(PendingAction.POST_PHOTO, canPresentShareDialogWithPhotos);
//    }
//
//    private void postPhoto() {
//        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
//        SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
//        ArrayList<SharePhoto> photos = new ArrayList<>();
//        photos.add(sharePhoto);
//
//        SharePhotoContent sharePhotoContent =
//                new SharePhotoContent.Builder().setPhotos(photos).build();
//        if (canPresentShareDialogWithPhotos) {
//            shareDialog.show(sharePhotoContent);
//        } else if (hasPublishPermission()) {
//            ShareApi.share(sharePhotoContent, shareCallback);
//        } else {
//            pendingAction = PendingAction.POST_PHOTO;
//            // We need to get new permissions, then complete the action when we get called back.
//            LoginManager.getInstance().logInWithPublishPermissions(
//                    this,
//                    Arrays.asList(PERMISSION));
//        }
//    }

//    private boolean hasPublishPermission() {
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
//    }
//
//    private void performPublish(PendingAction action, boolean allowNoToken) {
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if (accessToken != null || allowNoToken) {
//            pendingAction = action;
//            handlePendingAction();
//        }
//    }
}
