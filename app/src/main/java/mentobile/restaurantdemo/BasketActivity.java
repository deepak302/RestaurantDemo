package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class BasketActivity extends Activity implements View.OnClickListener {

    String TAG = "BasketActivity";
    private ListView listView;
    private BasketAdapter basketAdapter;
    private TextView tvEditItem;
    private TextView tvAddItem;
    private Button btnCountinue;
    public static TextView tvTotalPrice;
    private FragmentManager manager ;
    public static ArrayList<ItemDetail> arrListBasketItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        manager = getFragmentManager() ;
        tvEditItem = (TextView) findViewById(R.id.basket_tv_edit_item);
        tvEditItem.setOnClickListener(this);
        tvAddItem = (TextView) findViewById(R.id.basket_tv_add_item);
        tvAddItem.setOnClickListener(this);
        btnCountinue = (Button) findViewById(R.id.basket_btn_countinue);
        btnCountinue.setOnClickListener(this);
        tvTotalPrice = (TextView) findViewById(R.id.basket_tv_your_total_price);
        tvTotalPrice.setText("$ " + ItemDetail.getTotalPrice());
        listView = (ListView) findViewById(R.id.basket_lv_item);
//        listView.setEnabled(false);
        basketAdapter = new BasketAdapter(getApplicationContext(), R.layout.item_basket, arrListBasketItem);
        listView.setAdapter(basketAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ItemDetail.isEditItem) {
            tvEditItem.setText("Edit Item");
            ItemDetail.setIsEditItem(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvTotalPrice.setText("$ " + ItemDetail.getTotalPrice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basket_tv_edit_item:
                if (!ItemDetail.isEditItem) {
                    tvEditItem.setText("Save Item");
                    ItemDetail.setIsEditItem(true);
                } else {
                    tvEditItem.setText("Edit Item");
                    ItemDetail.setIsEditItem(false);
                }
                basketAdapter.notifyDataSetChanged();
                break;
            case R.id.basket_tv_add_item:
                onBackPressed();
                break;
            case R.id.basket_btn_countinue:
                FragmentTransaction transaction = manager.beginTransaction() ;
                transaction.replace(android.R.id.content, new AddressFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}
