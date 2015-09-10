package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.payUMoney.sdk.Constants;
import com.payUMoney.sdk.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import mentobile.utils.DBHandler;

public class AddressActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, NewAddressFragment.FragmentCommunicator {

    String TAG = "AddressActivity";

    private TextView btnAddNewAddress;
    private Button btnProceedToPayment;

    private ListView listView;
    private AddressAdapter addressAdapter;
    ArrayList<AddressItem> arrayList = new ArrayList<>();
    static DBHandler dbHandler;
    private FragmentManager manager;
    HashMap<String, String> params = new HashMap<String, String>();

    private NewAddressFragment addressFragment;
    AddressItem addressItem = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        dbHandler = new DBHandler(this, 1);
        manager = getFragmentManager();
        btnAddNewAddress = (TextView) findViewById(R.id.address_btn_new_aadress);
        btnAddNewAddress.setOnClickListener(this);
        btnProceedToPayment = (Button) findViewById(R.id.address_btn_payment);
        btnProceedToPayment.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.address_lv_address);
        listView.setOnItemClickListener(this);
        addressAdapter = new AddressAdapter(this, R.layout.addrss_row, arrayList);
        listView.setAdapter(addressAdapter);
        addressFragment = new NewAddressFragment(this);
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

            case R.id.address_btn_payment:
                if (arrayList.size() < 1) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(android.R.id.content, addressFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    if (addressItem != null && addressItem.isAddessSelected()) {
                        makePayment();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select your delivery address", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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


    private void makePayment() {

        String amount = "" + ItemDetail.getTotalPrice();
        String txnID = "MO" + System.currentTimeMillis();
        String merchantKey = Application.MERCHANT_KEY;
        String merchantSalt = Application.MERCHANT_SALT;
        String merchantID = Application.MERCHANT_ID;
        String phoneNumber = addressItem.getPhone();
        String productInfo = addressItem.getDeliveryAddress();
        String name = addressItem.getFullName();
        String email1 = addressItem.getEmail();
        String sURL = Application.SUCCESS_URL;
        String fURL = Application.FAILED_URL;

        if (Session.getInstance(this) == null) {
            Session.startPaymentProcess(this, params);
        } else {
            Session.createNewInstance(this);
        }

        String hashSequence = merchantKey + "|" + txnID + "|" + amount + "|" + productInfo + "|" + name + "|" + email1 + "|"
                + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + merchantSalt;

        String hash = hashCal("SHA-512", hashSequence);

        params.put("key", merchantKey);
        params.put("MerchantId", merchantID);
        params.put("TxnId", txnID);
        params.put("SURL", sURL);
        params.put("FURL", fURL);
        params.put("ProductInfo", productInfo);
        params.put("firstName", name);
        params.put("Email", email1);
        params.put("Phone", phoneNumber);
        params.put("Amount", amount);
        params.put("hash", hash);
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");

        Session.startPaymentProcess(this, params);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(data!=null) {
        if (requestCode == Session.PAYMENT_SUCCESS) {
            if (resultCode == RESULT_OK) {
                Log.i("app_activity", "success");
                Log.i("paymentID", data.getStringExtra("paymentId"));
                Intent intent = new Intent(this, paymentSuccess.class);
                intent.putExtra(Constants.RESULT, "success");
                intent.putExtra(Constants.PAYMENT_ID, data.getStringExtra("paymentId"));
                startActivity(intent);
                // finish();
            }

            if (resultCode == RESULT_CANCELED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(Constants.RESULT).equals("cancel")) {

                    } else {
                        Intent intent = new Intent(this, paymentSuccess.class);
                        intent.putExtra(Constants.RESULT, "failure");
                        startActivity(intent);
                    }
                }
                //Write your code if there's no result
            }
        }
        //}
    }

    public static String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();
    }

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
