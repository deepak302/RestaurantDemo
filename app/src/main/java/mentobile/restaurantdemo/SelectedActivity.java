package mentobile.restaurantdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mentobile.categary.BreadsFragment;
import mentobile.categary.ChineseFragment;
import mentobile.categary.DessertsFragment;
import mentobile.categary.DrinksFragment;
import mentobile.categary.EggsFragment;
import mentobile.categary.MainCourseFragment;
import mentobile.categary.NonVegFragment;
import mentobile.categary.RiceFragment;
import mentobile.categary.RollsFragment;
import mentobile.categary.SaladFragment;
import mentobile.categary.SoupsFragment;

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
        String gridItems[] = getResources().getStringArray(R.array.prompt_grid_Item_Type);
        for (int i = 0; i < gridItems.length; i++) {
            ActionBar.Tab tab = actionBar.newTab();
            tab.setText(gridItems[i]);
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
            case 0: // Veg Food
                ft.replace(android.R.id.content, new VegFragment());
                break;
            case 1://NonVeg Food
                ft.replace(android.R.id.content, new NonVegFragment());
                break;
            case 2://Soups
                ft.replace(android.R.id.content, new SoupsFragment());
                break;
            case 3://Chiense
                ft.replace(android.R.id.content, new ChineseFragment());
                break;
            case 4://Main Course
                ft.replace(android.R.id.content, new MainCourseFragment());
                break;
            case 5://Rice& noddles
                ft.replace(android.R.id.content, new RiceFragment());
                break;
            case 6://Rolls
                ft.replace(android.R.id.content, new RollsFragment());
                break;
            case 7://Eggs
                ft.replace(android.R.id.content, new EggsFragment());
                break;
            case 8://Breads
                ft.replace(android.R.id.content, new BreadsFragment());
                break;
            case 9://Salad
                ft.replace(android.R.id.content, new SaladFragment());
                break;
            case 10://Desserts
                ft.replace(android.R.id.content, new DessertsFragment());
                break;
            case 11://Drinks
                ft.replace(android.R.id.content, new DrinksFragment());
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
