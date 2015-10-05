package mentobile.restaurantdemo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class SelectedActivity extends Activity implements ActionBar.TabListener, View.OnClickListener {


    static TextView tvBasketItem;
    static TextView tvTotalAmount;
    private TextView tvToalAmountText;
    private ImageButton btnNextPage;
    private ImageView imgBasket;
    private ActionBar actionBar;
    private FragmentManager manager;
    public ArrayList<ItemDetail> arrayList = new ArrayList<>();
    //    private VegFragment vegFragment;
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
    public static int pos;

    VegFragment vegFragment[] = new VegFragment[MainActivity.arrListGridItem.size()];

    @Override
    public void onBackPressed() {

        if (BasketActivity.arrListBasketItem.size() > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(SelectedActivity.this);
            builder.setTitle("Discard Cart");
            builder.setMessage("Do you want to discard your current Cart?");
            builder.setPositiveButton("Discard Cart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SelectedActivity.super.onBackPressed();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selected);

        tvBasketItem = (TextView) findViewById(R.id.selected_tv_basket);
        tvTotalAmount = (TextView) findViewById(R.id.selected_tv_totalamount);
        tvTotalAmount.setOnClickListener(this);
        tvToalAmountText = (TextView) findViewById(R.id.selected_tv_total);
        tvToalAmountText.setOnClickListener(this);
        imgBasket = (ImageView) findViewById(R.id.selected_img_basket);
        imgBasket.setOnClickListener(this);

        btnNextPage = (ImageButton) findViewById(R.id.selected_imgbtn_next);
        btnNextPage.setOnClickListener(this);

//        vegFragment = new VegFragment();
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

        for (int i = 0; i < MainActivity.arrListGridItem.size(); i++) {
            vegFragment[i] = new VegFragment();
        }

        manager = getFragmentManager();
        actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        setCustomActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //  String gridItems[] = getResources().getStringArray(R.array.prompt_grid_Item_Type);
        for (int i = 0; i < MainActivity.arrListGridItem.size(); i++) {
            ActionBar.Tab tab = actionBar.newTab();
            GridItem gridItem = MainActivity.arrListGridItem.get(i);
            tab.setText(gridItem.getItemType());
            tab.setTabListener(this);
            actionBar.addTab(tab);
        }
        int frag = getIntent().getExtras().getInt("frag");
        actionBar.setSelectedNavigationItem(frag);
        Log.d("SelectedActivity ",":::::Selected "+frag);
    }

    private void setCustomActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        RelativeLayout actionBarLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        TextView actionBarTitleview = (TextView) actionBarLayout.findViewById(R.id.action_bar_tvTitle);
        actionBarTitleview.setText("Food Items");
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//        Toast.makeText(getApplicationContext(), "TAB " + tab.getPosition() + " TExt " + tab.getText(), Toast.LENGTH_SHORT).show();
        pos = tab.getPosition();
        ft.replace(android.R.id.content, vegFragment[pos]);
//        switch (tab.getPosition()) {
//            case 0: // Veg Food
////                ft.replace(android.R.id.content, vegFragment);
//                break;
//            case 1://NonVeg Food
//                ft.replace(android.R.id.content, nonVegFragment);
//                break;
//            case 2://Soups
//                ft.replace(android.R.id.content, soupsFragment);
//                break;
//            case 3://Chiense
//                ft.replace(android.R.id.content, chineseFragment);
//                break;
//            case 4://Main Course
//                ft.replace(android.R.id.content, mainCourseFragment);
//                break;
//            case 5://Rice& noddles
//                ft.replace(android.R.id.content, riceFragment);
//                break;
//            case 6://Rolls
//                ft.replace(android.R.id.content, rollsFragment);
//                break;
//            case 7://Eggs
//                ft.replace(android.R.id.content, eggsFragment);
//                break;
//            case 8://Breads
//                ft.replace(android.R.id.content, breadsFragment);
//                break;
//            case 9://Salad
//                ft.replace(android.R.id.content, saladFragment);
//                break;
//            case 10://Desserts
//                ft.replace(android.R.id.content, dessertsFragment);
//                break;
//            case 11://Drinks
//                ft.replace(android.R.id.content, drinksFragment);
//                break;
//        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (BasketActivity.arrListBasketItem.size() < 1) {
            Toast.makeText(SelectedActivity.this, R.string.msg_bucket_item, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(SelectedActivity.this, BasketActivity.class);
        switch (v.getId()) {
            case R.id.selected_img_basket:
            case R.id.selected_tv_total:
            case R.id.selected_tv_totalamount:
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
                break;
            case R.id.selected_imgbtn_next:
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
