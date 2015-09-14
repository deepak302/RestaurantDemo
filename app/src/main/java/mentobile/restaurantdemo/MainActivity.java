package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import mentobile.utils.DBHandler;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener, View.OnTouchListener {

    private String TAG = "MainActivity";
    private FragmentManager fragmentManager;

    //Navigation Drawer layout
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ArrayList<NvItems> arrayList;
    private SMenuAdapter sMenuAdapter;
    private ListView listView;
    private DBHandler dbHandler;

    private TextView tvNVEmail;
    private TextView tvNVName;

    private GridView gridView;
    ArrayList<GridItem> alGridItem = new ArrayList<GridItem>();
    private GridItem gridItem;
    private GridAdapter gridAdapter;

    private ViewFlipper viewFlipper;
    private ImageView imgSwipeLeft;
    private ImageView imgSwipeRight;

    private FragmentManager manager = null;

    @Override
    protected void onStart() {
        super.onStart();
        setProfile();
        Log.d(TAG, ":::::::::ONstart");
//        new AsyncTask<String, String, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//                JsonParser parser = new JsonParser();
//                ArrayList<NameValuePair> listValue = new ArrayList<NameValuePair>();
//                JSONObject jsonObject = parser.makeHttpRequest("122001", listValue);
//                try {
//                    Log.d(TAG,":::::Value "+jsonObject.getString("results"));
//                    Log.d(TAG,":::::Value1 "+jsonObject.getJSONObject("address_components").get("long_name"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return "";
//            }
//        }.execute();

    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "mentobile.restaurantdemo", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
//        showHashKey(getApplicationContext());
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(getApplicationContext(), 1);
        mTitle = mDrawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        listView = (ListView) findViewById(R.id.main_lv_nvdrawer);
        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.nv_header, null, false);
        tvNVEmail = (TextView) viewGroup.findViewById(R.id.nvheader_tv_pemail);
        tvNVName = (TextView) viewGroup.findViewById(R.id.nvheader_tv_pname);
        listView.addHeaderView(viewGroup, null, false);

        // drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);
        arrayList = new ArrayList<NvItems>();
        String nv_array[] = getResources().getStringArray(
                R.array.prompt_nv_drawer);
        for (int i = 0; i < nv_array.length; i++) {
            NvItems items = new NvItems(R.mipmap.ic_launcher, nv_array[i], null, false);
            arrayList.add(items);
        }
        sMenuAdapter = new SMenuAdapter(getApplicationContext(),
                R.layout.nv_drawer_row, arrayList);
        listView.setAdapter(sMenuAdapter);
        listView.setOnItemClickListener(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.mipmap.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                // getActionBar().setTitle(R.string.navigation_drawer_open);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
//                getActionBar().setTitle(R.string.navigation_drawer_close);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        gridView = (GridView) findViewById(R.id.main_gv_item);
        gridView.setOnItemClickListener(this);
        String gridItems[] = getResources().getStringArray(R.array.prompt_grid_Item_Type);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        for (int i = 0; i < gridItems.length; i++) {
            gridItem = new GridItem(drawable, gridItems[i], i);
            alGridItem.add(gridItem);
        }
        gridAdapter = new GridAdapter(getApplicationContext(), R.layout.custom_grid_item, alGridItem);
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        viewFlipper = (ViewFlipper) findViewById(R.id.main_viewflip_slide);
        viewFlipper.startFlipping();

        imgSwipeLeft = (ImageView) findViewById(R.id.main_img_swipe_left);
        imgSwipeLeft.setOnTouchListener(this);

        imgSwipeRight = (ImageView) findViewById(R.id.main_img_swipe_right);
        imgSwipeRight.setOnTouchListener(this);
    }

    private void setProfile() {
        String email = Application.getDataFromSharedPreference(this, Application.SP_LOGIN_LOGOUT, "email");
        if (email != null && email.length() > 5) {
            NvItems items = (NvItems) arrayList.get(5);
            items.setTitle(getString(R.string.prompt_logout));
            sMenuAdapter.notifyDataSetChanged();
            Cursor cursor = dbHandler.getPRofileDataFromDB(email);
            while (cursor.moveToNext()) {
                Profile.getProfile().setFirstName(cursor.getString(0));
                Profile.getProfile().setLastName(cursor.getString(1));
                Profile.getProfile().setFullName(cursor.getString(2));
                Profile.getProfile().setEmailID(cursor.getString(3));
                Profile.getProfile().setMobile(cursor.getString(4));
                Profile.getProfile().setCityName(cursor.getString(5));
                Profile.getProfile().setLocation(cursor.getString(6));
                Profile.getProfile().setCompanyName(cursor.getString(7));
                Profile.getProfile().setHouseNo(cursor.getString(8));
                Profile.getProfile().setApartmentName(cursor.getString(9));
                Profile.getProfile().setPostalCode(cursor.getString(10));
                Profile.getProfile().setOtherAddress(cursor.getString(11));
                Profile.getProfile().setDelIns(cursor.getString(12));
            }
        } else {
            Profile.getProfile().setEmailID(getString(R.string.prompt_nv_email));
            Profile.getProfile().setFullName(getString(R.string.prompt_nv_name));
        }
        tvNVEmail.setText(Profile.getProfile().getEmailID());
        tvNVName.setText(Profile.getProfile().getFullName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        boolean drawerOpen = drawerLayout.isDrawerOpen(listView);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        // TODO Auto-generated method stub
        mTitle = title;
        getActionBar().setTitle(mTitle);
        super.setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.main_gv_item) {
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.aplha);
            view.startAnimation(animation);
            Intent intent = new Intent(MainActivity.this, SelectedActivity.class);
            intent.putExtra("frag", position);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (parent.getId() == R.id.main_lv_nvdrawer) {

            switch (position) {
                case 0:
                    break;
                case 1:
                    Application.clearSharedPreferenceFile(this, Application.SP_LOGIN_LOGOUT);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    if (Application.getDataFromSharedPreference(this, Application.SP_LOGIN_LOGOUT, "email") != null) {
                        Application.clearSharedPreferenceFile(this, Application.SP_LOGIN_LOGOUT);
                        NvItems items = (NvItems) arrayList.get(position - 1);
                        items.setTitle(getString(R.string.prompt_login));
                        sMenuAdapter.notifyDataSetChanged();
                        tvNVEmail.setText(getString(R.string.prompt_nv_email));
                        tvNVName.setText(getString(R.string.prompt_nv_name));
                        Profile.emptyProfile();
                        switch (LoginActivity1.LOGIN_TYPE) {
                            case 1://Simple Login

                                break;
                            case 2://Google
                                LoginActivity1.loginActivity.googlePlusLogout();
                                break;
                            case 3://Facebook
                                LoginActivity1.loginActivity.facebokLogout();
                                break;
                        }

                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity1.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            switch (v.getId()) {
                case R.id.main_img_swipe_left:
                    Log.d(TAG, "::::::Show Next");
                    viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_right);
                    viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_left);
                    viewFlipper.showNext();
                    break;
                case R.id.main_img_swipe_right:
                    Log.d(TAG, "::::::Show Previous");
                    viewFlipper.setInAnimation(getApplicationContext(), R.anim.slide_in_left);
                    viewFlipper.setOutAnimation(getApplicationContext(), R.anim.slide_out_right);
                    viewFlipper.showPrevious();
                    break;
            }
        }
        return true;
    }
}
