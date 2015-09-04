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

import java.util.ArrayList;

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
import mentobile.categary.VegFragment;

public class SelectedActivity extends Activity implements ActionBar.TabListener {

    private ActionBar actionBar;
    private Button btnAddToBasket;
    private FragmentManager manager;
    public ArrayList<ItemDetail> arrayList = new ArrayList<>();
    private VegFragment vegFragment;
    private NonVegFragment nonVegFragment;
    private SoupsFragment soupsFragment;
    private ChineseFragment chineseFragment;
    private MainCourseFragment mainCourseFragment;
    private RiceFragment riceFragment;
    private RollsFragment rollsFragment;
    private EggsFragment eggsFragment;
    private BreadsFragment breadsFragment;
    private SaladFragment saladFragment;
    private DessertsFragment dessertsFragment;
    private DrinksFragment drinksFragment;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        vegFragment = new VegFragment();
        nonVegFragment = new NonVegFragment();
        soupsFragment = new SoupsFragment();
        chineseFragment = new ChineseFragment();
        mainCourseFragment = new MainCourseFragment();
        riceFragment = new RiceFragment();
        rollsFragment = new RollsFragment();
        eggsFragment = new EggsFragment();
        breadsFragment = new BreadsFragment();
        saladFragment = new SaladFragment();
        dessertsFragment = new DessertsFragment();
        drinksFragment = new DrinksFragment();

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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //Toast.makeText(getApplicationContext(), "TAB " + tab.getPosition() + " TExt " + tab.getText(), Toast.LENGTH_SHORT).show();
        switch (tab.getPosition()) {
            case 0: // Veg Food
                ft.replace(android.R.id.content, vegFragment);
                break;
            case 1://NonVeg Food
                ft.replace(android.R.id.content, nonVegFragment);
                break;
            case 2://Soups
                ft.replace(android.R.id.content, soupsFragment);
                break;
            case 3://Chiense
                ft.replace(android.R.id.content, chineseFragment);
                break;
            case 4://Main Course
                ft.replace(android.R.id.content, mainCourseFragment);
                break;
            case 5://Rice& noddles
                ft.replace(android.R.id.content, riceFragment);
                break;
            case 6://Rolls
                ft.replace(android.R.id.content, rollsFragment);
                break;
            case 7://Eggs
                ft.replace(android.R.id.content, eggsFragment);
                break;
            case 8://Breads
                ft.replace(android.R.id.content, breadsFragment);
                break;
            case 9://Salad
                ft.replace(android.R.id.content, saladFragment);
                break;
            case 10://Desserts
                ft.replace(android.R.id.content, dessertsFragment);
                break;
            case 11://Drinks
                ft.replace(android.R.id.content, drinksFragment);
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
