package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class BasketActivity extends Activity implements View.OnClickListener {

    String TAG = "BasketActivity";
    private ListView listView;
    private BasketAdapter basketAdapter;
    private TextView tvChooseAddress;
    private ImageButton imgbtn_NextPage_Lower;

    static TextView tvBasketItems;
    static TextView tvTotalAmount;

    private RelativeLayout basket_rl_button ;

    private FragmentManager manager;
    public static ArrayList<ItemDetail> arrListBasketItem = new ArrayList<>();

    private int inAnimation, outAnimation;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(inAnimation, outAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        manager = getFragmentManager();
        tvChooseAddress = (TextView) findViewById(R.id.basket_tv_choose_address);
        tvChooseAddress.setOnClickListener(this);
        imgbtn_NextPage_Lower = (ImageButton) findViewById(R.id.basket_imgbtn_next_lower);
        imgbtn_NextPage_Lower.setOnClickListener(this);

        tvBasketItems = (TextView) findViewById(R.id.basket_tv_basket);
        tvBasketItems.setOnClickListener(this);
        tvTotalAmount = (TextView) findViewById(R.id.basket_tv_totalamount);
        tvTotalAmount.setOnClickListener(this);
        basket_rl_button = (RelativeLayout) findViewById(R.id.basket_rl_button);
        basket_rl_button.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.basket_lv_item);
        basketAdapter = new BasketAdapter(getApplicationContext(), R.layout.item_list, arrListBasketItem);
        listView.setAdapter(basketAdapter);

        inAnimation = R.anim.slide_in_left;
        outAnimation = R.anim.slide_out_right;
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvTotalAmount.setText("$ " + ItemDetail.getTotalAmount());
        tvBasketItems.setText("" + ItemDetail.getTotalBasketItem());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
        switch (v.getId()) {
            case R.id.basket_imgbtn_next_lower:
                ItemDetail.setIsEditItem(false);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.basket_tv_choose_address:
                ItemDetail.setIsEditItem(false);
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
                break;

            case R.id.basket_rl_button:
                inAnimation = R.anim.push_up_in;
                outAnimation = R.anim.push_down_out;
                onBackPressed();
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
