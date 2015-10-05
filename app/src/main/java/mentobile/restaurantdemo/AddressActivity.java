package mentobile.restaurantdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payUMoney.sdk.Constants;
import com.payUMoney.sdk.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import mentobile.utils.DBHandler;

public class AddressActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, NewAddressFragment.FragmentCommunicator {

    String TAG = "AddressActivity";

    private TextView btnAddNewAddress;
    private ImageButton imgBtnNextPage;
    private TextView tvNextPage;
    private RelativeLayout address_rl_upper;
    private ListView listView;
    private AddressAdapter addressAdapter;
    ArrayList<AddressItem> arrayList = new ArrayList<>();
    static DBHandler dbHandler;
    private FragmentManager manager;
    static String strFullAddress;

    private NewAddressFragment addressFragment;
    AddressItem addressItem = null;
    private int inAnimation, outAnimation;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(inAnimation, outAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        dbHandler = new DBHandler(this, 1);
        manager = getFragmentManager();

        btnAddNewAddress = (TextView) findViewById(R.id.address_btn_new_aadress);
        btnAddNewAddress.setOnClickListener(this);

        imgBtnNextPage = (ImageButton) findViewById(R.id.address_imgbtn_next);
        imgBtnNextPage.setOnClickListener(this);

        address_rl_upper = (RelativeLayout) findViewById(R.id.address_rl_upper);
        address_rl_upper.setOnClickListener(this);

        tvNextPage = (TextView) findViewById(R.id.address_tv_processed_payment);
        tvNextPage.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.address_lv_address);
        listView.setOnItemClickListener(this);
        addressAdapter = new AddressAdapter(this, R.layout.addrss_row, arrayList);
        listView.setAdapter(addressAdapter);
        addressFragment = new NewAddressFragment(this);
        if (!dbHandler.isTableEmplty(DBHandler.TABLE_DELIVERY_ADDRESS)) {
            Cursor cursor = dbHandler.getAllDeliveryAddress();
            arrayList.clear();

            while (cursor.moveToNext()) {
                addressItem = new AddressItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                arrayList.add(addressItem);
            }
//            addressItem = arrayList.get(arrayList.size()-1);
            addressItem.setIsAddessSelected(true);
            addressAdapter.notifyDataSetChanged();
        }

        inAnimation = R.anim.slide_in_left;
        outAnimation = R.anim.slide_out_right;
        setCustomActionBar();

    }

    private void setCustomActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        RelativeLayout actionBarLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        TextView actionBarTitleview = (TextView) actionBarLayout.findViewById(R.id.action_bar_tvTitle);
        actionBarTitleview.setText("Your Address");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.LEFT);
        ImageButton drawerImageView = (ImageButton) actionBarLayout.findViewById(R.id.action_bar_imgbtn);
        drawerImageView.setBackgroundResource(R.mipmap.ic_action_back);
        drawerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.aplha);
                view.startAnimation(animation);
                onBackPressed();
            }
        });

        actionBar.setCustomView(actionBarLayout, params);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_btn_new_aadress:
                if (arrayList.size() < 5) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    addressFragment = new NewAddressFragment(this);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content, addressFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.prompt_toast_address_allow), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.address_imgbtn_next:
                deliveyType();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.address_tv_processed_payment:
                deliveyType();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
                break;

            case R.id.address_rl_upper:
                inAnimation = R.anim.push_up_in;
                outAnimation = R.anim.push_down_out;
                onBackPressed();
                break;
        }
    }

    private void deliveyType() {

        if (arrayList.size() < 1) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(android.R.id.content, addressFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            if (addressItem != null && addressItem.isAddessSelected()) {
                AddressItem.getAddressItem().setFullName(addressItem.getFullName());
                AddressItem.getAddressItem().setCity(addressItem.getCity());
                AddressItem.getAddressItem().setDeliveryAddress(addressItem.getDeliveryAddress());
                AddressItem.getAddressItem().setEmail(addressItem.getEmail());
                AddressItem.getAddressItem().setLandmark(addressItem.getLandmark());
                AddressItem.getAddressItem().setPhone(addressItem.getPhone());
                AddressItem.getAddressItem().setState(addressItem.getState());
                AddressItem.getAddressItem().setPincode(addressItem.getPincode());

                strFullAddress = "" + addressItem.getDeliveryAddress() + "\n" +
                        "" + addressItem.getCity() + "\t" + "" + addressItem.getState() + "\n" +
                        "Pincode  " + addressItem.getPincode() + "\n" +
                        "Landmark " + addressItem.getLandmark();

                Intent intentDeliveryType = new Intent(this, DeliveryTypeActivity.class);
                startActivity(intentDeliveryType);
                //makePayment();
            } else {
                Toast.makeText(getApplicationContext(), "Please select your delivery address", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < arrayList.size(); i++) {
            AddressItem addressItem = arrayList.get(i);
            addressItem.setIsAddessSelected(false);
        }
        addressItem = arrayList.get(position);
        addressItem.setIsAddessSelected(true);
        addressAdapter.notifyDataSetChanged();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        onBackPressed();
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void fragmentDetached(boolean isAttached) {
        addressItem = null;
        Log.d(TAG, "::::::Fragment Communicator " + isAttached);
        if (!isAttached) {
            if (!dbHandler.isTableEmplty(DBHandler.TABLE_DELIVERY_ADDRESS)) {
                Cursor cursor = dbHandler.getAllDeliveryAddress();
                arrayList.clear();
                AddressItem addressItem = null;
                Log.d(TAG, "::::::On Address " + cursor.getCount());
                while (cursor.moveToNext()) {
                    addressItem = new AddressItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                    arrayList.add(addressItem);
                }
                addressAdapter.notifyDataSetChanged();
            }
        }
    }
}
