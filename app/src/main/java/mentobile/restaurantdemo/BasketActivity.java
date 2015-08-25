package mentobile.restaurantdemo;

import android.app.Activity;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class BasketActivity extends Activity {

    String TAG = "BasketActivity";
    private ListView listView;
    private BasketAdapter basketAdapter;
    public static ArrayList<ItemDetail> arrListBasketItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        listView = (ListView) findViewById(R.id.basket_lv_item);
        Log.d(TAG,":::::ArrayLIst "+arrListBasketItem.size());
        basketAdapter = new BasketAdapter(getApplicationContext(), R.layout.item_basket, arrListBasketItem);
        listView.setAdapter(basketAdapter);
    }
}
