package mentobile.restaurantdemo;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.payUMoney.sdk.Constants;
import com.payUMoney.sdk.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class DeliveryTypeActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private HashMap<String, String> params = new HashMap<String, String>();
    private TextView tvAmount;
    private int inAnimation, outAnimation;
    private RadioGroup rgPayonline;

    private TextView tvPlaceOrder;
    private ImageButton imgBtnNextPage;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(inAnimation, outAnimation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvAmount.setText("" + ItemDetail.getTotalAmount());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_type);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        tvAmount = (TextView) findViewById(R.id.delivery_tv_totalamount);
        tvAmount.setText("" + ItemDetail.getTotalAmount());

        tvPlaceOrder = (TextView) findViewById(R.id.delivery_tv_place_order);
        tvPlaceOrder.setOnClickListener(this);

        imgBtnNextPage = (ImageButton) findViewById(R.id.delivery_imgbtn_next_page);
        imgBtnNextPage.setOnClickListener(this);

        inAnimation = R.anim.slide_in_left;
        outAnimation = R.anim.slide_out_right;

        rgPayonline = (RadioGroup) findViewById(R.id.delivery_rg_paytype);
        rgPayonline.setOnCheckedChangeListener(this);

        inAnimation = R.anim.slide_in_left;
        outAnimation = R.anim.slide_out_right;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void makePayment() {

        String amount = "" + ItemDetail.getTotalAmount();
        String txnID = "MO" + System.currentTimeMillis();
        String merchantKey = Application.MERCHANT_KEY;
        String merchantSalt = Application.MERCHANT_SALT;
        String merchantID = Application.MERCHANT_ID;
        String phoneNumber = AddressItem.getAddressItem().getPhone();
        String productInfo = AddressItem.getAddressItem().getDeliveryAddress();
        String name = AddressItem.getAddressItem().getFullName();
        String email1 = AddressItem.getAddressItem().getEmail();
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void onClick(View v) {
        int checkedId = rgPayonline.getCheckedRadioButtonId();
        switch (v.getId()) {
            case R.id.delivery_imgbtn_next_page:
            case R.id.delivery_tv_place_order:
                switch (checkedId) {
                    case R.id.delivery_rb_payonline:
                        makePayment();
                        break;
                    case R.id.delivery_rb_paytm_wallet:

                        break;
                    case R.id.delivery_rb_cash:
                        Intent intent = new Intent(this, MobileConfirmActivity.class);
                        startActivity(intent);
                        if (v.getId() == R.id.delivery_imgbtn_next_page) {
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
                        }
                        break;
                }
                break;
        }
    }
}
