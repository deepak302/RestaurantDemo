package mentobile.restaurantdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectedActivity extends Activity implements ActionBar.TabListener {

    private ActionBar actionBar;
    private Button btnAddToBasket;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        manager = getFragmentManager();
        actionBar = getActionBar();
        BasketActivity.arrListBasketItem.clear();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (int i = 0; i < 4; i++) {
            ActionBar.Tab tab = actionBar.newTab();
            tab.setText("Hello World " + i);
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }
        int frag = getIntent().getExtras().getInt("frag");
        actionBar.setSelectedNavigationItem(frag);
        btnAddToBasket = (Button) findViewById(R.id.selected_btn_basket);
        btnAddToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BasketActivity.arrListBasketItem.size() < 1) {
                    Toast.makeText(SelectedActivity.this, R.string.msg_bucket_item, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SelectedActivity.this, BasketActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //Toast.makeText(getApplicationContext(), "TAB " + tab.getPosition() + " TExt " + tab.getText(), Toast.LENGTH_SHORT).show();
        switch (tab.getPosition()) {
            case 0:
                ft.replace(android.R.id.content, new VegFragment());
                break;
            case 1:
                ft.replace(android.R.id.content, new NonVegFragment());
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
