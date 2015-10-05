package mentobile.restaurantdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mentobile.utils.WebService;


public class OrderCompleteActivity extends Activity {

    private TableLayout tableLayout;
    private TableRow tr_CustomerView;
    private TextView tvGoToHomePage;
    private TextView tvAddress;
    private TextView tvOrderNumber;
    private TextView tvOrderMsg;

    TableRow.LayoutParams layoutParams = null;
    String[] headerString = {"S.No.", "Item Name", "Quantity", "Price"};

    private CProgressDialog cProgressDialog;
    private ArrayList<NameValuePair> listValue;
    private WebService webService;
    private String timeInMillSec;
    private String TAG = "OrderDetailActivity";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(OrderCompleteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        long timeMS = System.currentTimeMillis() / 10000;
        timeInMillSec = "OD" + timeMS;
        getActionBar().setDisplayHomeAsUpEnabled(true);
        tvOrderNumber = (TextView) findViewById(R.id.order_tv_ordernumber);
        tvOrderNumber.setText("" + timeInMillSec);
        tvOrderMsg = (TextView) findViewById(R.id.order_tv_msg);
        tvOrderMsg.setText("Your order no. " + timeInMillSec + " has been dispatched to this below address:");
        tvGoToHomePage = (TextView) findViewById(R.id.order_tv_back_home);
        tvAddress = (TextView) findViewById(R.id.order_tv_del_address);

        String address = AddressItem.getAddressItem().getFullAddress();
        tvAddress.setText(address.toUpperCase());
        tvGoToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tableLayout = (TableLayout) findViewById(R.id.order_tl_main);
        tr_CustomerView = new TableRow(this);
        for (int k = 0; k < headerString.length; k++) {
            layoutParams = new TableRow.LayoutParams();
            layoutParams.setMargins(1, 1, 1, 1);
            layoutParams.weight = 1;
            addHederinTable(headerString[k]);
        }
        tableLayout.addView(tr_CustomerView);

        for (int j = 0; j < BasketActivity.arrListBasketItem.size(); j++) {
            ItemDetail itemDetail = BasketActivity.arrListBasketItem.get(j);
            tr_CustomerView = new TableRow(this);
            for (int k = 0; k < headerString.length; k++) {
                layoutParams = new TableRow.LayoutParams();
                layoutParams.setMargins(1, 1, 1, 1);
                layoutParams.weight = 1;
                if (k == 0) {
                    addRowInTable("" + (j + 1));
                } else if (k == 1) {
                    addRowInTable("" + itemDetail.getName());

                } else if (k == 2) {
                    addRowInTable("" + itemDetail.getQuantity());

                } else {
                    addRowInTable("" + itemDetail.getPriceOverQuantity());
                }
            }
            tableLayout.addView(tr_CustomerView);
        }
        tr_CustomerView = new TableRow(this);
        for (int k = 0; k < 2; k++) {
            layoutParams = new TableRow.LayoutParams();
            layoutParams.setMargins(1, 2, 1, 2);
            layoutParams.weight = 1;
            layoutParams.span = 2;
            if (k == 0) {
                addFooterinTable("Total Amount:");
            } else {
                addFooterinTable("Rs. " + ItemDetail.getTotalAmount());
            }
        }
        tableLayout.addView(tr_CustomerView);
        cProgressDialog = new CProgressDialog(this);
        webService = new WebService();
        String itemDetails = "";
        for (int i = 0; i < BasketActivity.arrListBasketItem.size(); i++) {
            ItemDetail itemDetail = BasketActivity.arrListBasketItem.get(i);
            itemDetails += itemDetail.getName() + ", ";
        }
        Log.d(TAG, ":::::Item Detail " + itemDetails);
        listValue = new ArrayList<NameValuePair>();
        listValue.add(new BasicNameValuePair("order", "" + timeInMillSec));
        listValue.add(new BasicNameValuePair("cname", AddressItem.getAddressItem().getFullName()));
        listValue.add(new BasicNameValuePair("phone", AddressItem.getAddressItem().getPhone()));
        listValue.add(new BasicNameValuePair("address", "" + AddressItem.getAddressItem().getDeliveryAddress()));
        listValue.add(new BasicNameValuePair("detail", itemDetails));
        listValue.add(new BasicNameValuePair("amount", "" + ItemDetail.getTotalAmount()));
        listValue.add(new BasicNameValuePair("status", "complete"));
        listValue.add(new BasicNameValuePair("desc", "Test order detail and description"));
        setCustomActionBar();
        MyAsynchTask myAsynchTask = new MyAsynchTask();
        myAsynchTask.execute();

    }

    private void setCustomActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        RelativeLayout actionBarLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        TextView actionBarTitleview = (TextView) actionBarLayout.findViewById(R.id.action_bar_tvTitle);
        actionBarTitleview.setText("Order Completed");
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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        onBackPressed();
//        return super.onOptionsItemSelected(item);
//    }

    private void addRowInTable(String text) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.color.white);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setPadding(0, 5, 0, 5);
        textView.setTypeface(null, Typeface.NORMAL);
        tr_CustomerView.addView(textView, layoutParams);
    }

    private void addFooterinTable(String text) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.color.white);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setPadding(0, 10, 0, 10);
        textView.setTypeface(null, Typeface.BOLD);
        tr_CustomerView.addView(textView, layoutParams);
    }

    private void addHederinTable(String text) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.color.gray);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        textView.setText(text);
        textView.setPadding(0, 10, 0, 10);
        textView.setTypeface(null, Typeface.BOLD);
        tr_CustomerView.addView(textView, layoutParams);
    }

    private class MyAsynchTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cProgressDialog.show();
            Drawable drawable = getResources().getDrawable(R.drawable.my_progress_indeterminate);
            cProgressDialog.setIndeterminateDrawable(drawable);
            cProgressDialog.setMessage("Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "::::::Json 1");
            JSONObject json = webService.makeHttpRequest("Orders", listValue);
            try {
                String response = json.getString("description");
                return "" + response;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Invalid";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(OrderCompleteActivity.this, " " + result, Toast.LENGTH_SHORT).show();
            cProgressDialog.hide();
        }
    }
}
