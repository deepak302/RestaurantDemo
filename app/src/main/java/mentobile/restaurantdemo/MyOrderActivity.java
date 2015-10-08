package mentobile.restaurantdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mentobile.utils.WebService;


public class MyOrderActivity extends Activity {

    private static final String TAG = "MyOrderActivity";
    private ListView listView;
    private MyOrderAdapter myOrderAdapter;
    private ArrayList<MyOrder> arrayList = new ArrayList<>();
    private CProgressDialog cProgressDialog;
    private WebService webService;
    private ArrayList<NameValuePair> listValue;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        setCustomActionBar();
        cProgressDialog = new CProgressDialog(this);
        webService = new WebService();
        listView = (ListView) findViewById(R.id.myorder_lv_detail);

        listValue = new ArrayList<NameValuePair>();
        listValue.add(new BasicNameValuePair("phone", "8826510669"));

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
            JSONObject json = webService.makeHttpRequest("MyOrderDetails", listValue);
            try {
                JSONArray jsonArray = json.getJSONArray("description");
                Log.d(TAG, "::::::JSon Array " + jsonArray.toString());
                int length = jsonArray.length();
                for (int i = 1; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String order_id = jsonObject.getString("order_id");
                    String order_date_time = jsonObject.getString("order_date_time");
                    String customer_name = jsonObject.getString("customer_name");
                    String phone = jsonObject.getString("phone");
                    String address = jsonObject.getString("address");
                    String order_detail = jsonObject.getString("order_detail");
                    String amount = jsonObject.getString("amount");
                    String status = jsonObject.getString("status");
                    String description = "";

                    MyOrder myOrder = new MyOrder(order_id, order_date_time, customer_name, phone, address, order_detail, amount, status, description);
                    arrayList.add(myOrder);
                }
                return "";
            }
            catch (JSONException e) {
                return "" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            myOrderAdapter = new MyOrderAdapter(getApplicationContext(), R.layout.items_myorder, arrayList);
            listView.setAdapter(myOrderAdapter);
            myOrderAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), " " + result, Toast.LENGTH_SHORT).show();
            cProgressDialog.hide();
        }
    }
}
