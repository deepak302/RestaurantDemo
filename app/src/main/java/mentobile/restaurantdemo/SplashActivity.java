package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import mentobile.utils.DBHandler;

public class SplashActivity extends Activity {

    private String TAG = "SplashActivity";
    private boolean isThread = true;
    private NetworkErrorFragment networkErrorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getActionBar().hide();
        networkErrorFragment = new NetworkErrorFragment();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isThread) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isThread = false;
                            // Application.clearSharedPreferenceFile(SplashActivity.this, Application.SP_LOGIN_LOGOUT);
                            if (Application.isNetworkAvailable(getApplicationContext())) {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(android.R.id.content, networkErrorFragment);
                                fragmentTransaction.commit();
                            }
                        }
                    });
                }
            }).start();
        }
    }
}
