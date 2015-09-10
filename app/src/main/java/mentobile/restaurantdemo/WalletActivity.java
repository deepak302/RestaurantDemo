package mentobile.restaurantdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.payUMoney.sdk.Constants;
import com.payUMoney.sdk.Session;

public class WalletActivity extends Activity {

    EditText amt, txnid, phone, pinfo, fname, email, surl, furl, mid;
    Button pay;

    HashMap<String, String> params = new HashMap<String, String>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        amt = (EditText) findViewById(R.id.amount);
        txnid = (EditText) findViewById(R.id.txnid);
        mid = (EditText) findViewById(R.id.merchant_id);
        phone = (EditText) findViewById(R.id.phone);
        pinfo = (EditText) findViewById(R.id.pinfo);
        fname = (EditText) findViewById(R.id.fname);
        email = (EditText) findViewById(R.id.email);
        surl = (EditText) findViewById(R.id.surl);
        furl = (EditText) findViewById(R.id.furl);
        pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment1(view);
            }
        });
    }

    private void makePayment1(View view) {
        String amount = "10";//amt.getText().toString().trim();
        String txnID = "1220006";//txnid.getText().toString().trim();
        String merchantKey = "MHWOrn";
        String merchantSalt = "F0g0Bd5S";
        String merchantID = "5190740";// mid.getText().toString().trim();
        String phoneNumber = "9999147197";//phone.getText().toString().trim();
        String produtInfo = "Hello_World_Testing";//pinfo.getText().toString().trim();
        String name = "Rahul";//fname.getText().toString().trim();
        String email1 = "rahul44ks@gmail.com";//email.getText().toString().trim();
        String sURL = "https://www.payumoney.com/mobileapp/payumoney/success.php";//surl.getText().toString().trim();
        String fURL = "https://www.payumoney.com/mobileapp/payumoney/failure.php";//furl.getText().toString().trim();

        if (Session.getInstance(this) == null) {
            Session.startPaymentProcess(this, params);
        } else {
            Session.createNewInstance(this);
        }

        String hashSequence = merchantKey + "|" + txnID + "|" + amount + "|" + produtInfo + "|" + name + "|" + email1 + "|"
                + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + merchantSalt;

        String hash = hashCal("SHA-512", hashSequence);

        params.put("key", merchantKey);
        params.put("MerchantId", merchantID);
        params.put("TxnId", txnID);
        params.put("SURL", sURL);
        params.put("FURL", fURL);
        params.put("ProductInfo", produtInfo);
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
}
