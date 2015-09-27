package mentobile.restaurantdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class OrderCompleteActivity extends Activity {

    private ListView lvOrderDetail;
    private OrderAdapter orderAdapter;
    private TableLayout tableLayout;
    private TableRow tr_CustomerView;
    private TextView tvGoToHomePage;
    private TextView tvAddress;

    TableRow.LayoutParams layoutParams = null;
    String[] headerString = {"S.No.", "Title", "Quantity", "Price"};

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        tvGoToHomePage = (TextView)findViewById(R.id.order_tv_back_home);
        tvAddress = (TextView)findViewById(R.id.order_tv_del_address);
        tvAddress.setText(AddressActivity.strFullAddress);
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
            layoutParams.setMargins(1, 1, 1, 1);
            layoutParams.weight = 1;
            layoutParams.span = 2;
            if (k == 0) {
                addFooterinTable("Total");
            } else {
                addFooterinTable("" + ItemDetail.getTotalAmount());
            }
        }
        tableLayout.addView(tr_CustomerView);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void addRowInTable(String text) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.color.white);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setTypeface(null, Typeface.NORMAL);
        tr_CustomerView.addView(textView, layoutParams);
    }

    private void addFooterinTable(String text) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.color.white);
        textView.setTextColor(Color.BLACK);
        textView.setText(text);
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
        textView.setTypeface(null, Typeface.BOLD);
        tr_CustomerView.addView(textView, layoutParams);
    }
}
