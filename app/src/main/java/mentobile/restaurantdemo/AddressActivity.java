package mentobile.restaurantdemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import mentobile.utils.DBHandler;


public class AddressActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    String TAG = "AddressActivity";
    private Button btnAddNewAddress;
    private Button btnProceedToPayment;

    private ListView listView;
    private AddressAdapter addressAdapter;
    private ArrayList<AddressItem> arrayList = new ArrayList<>();
    static DBHandler dbHandler;
    private FragmentManager manager;

    @Override
    protected void onStart() {
        super.onStart();
        if (!dbHandler.isTableEmplty(DBHandler.TABLE_DELIVERY_ADDRESS)) {
            Cursor cursor = dbHandler.getAllDeliveryAddress();
            arrayList.clear();
            while (cursor.moveToNext()) {
                AddressItem addressItem = new AddressItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                arrayList.add(addressItem);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        dbHandler = new DBHandler(this, 1);
        manager = getFragmentManager();
        btnAddNewAddress = (Button) findViewById(R.id.address_btn_new_aadress);
        btnAddNewAddress.setOnClickListener(this);
        btnProceedToPayment = (Button) findViewById(R.id.address_btn_payment);
        btnProceedToPayment.setOnClickListener(this);

//        for (int i = 0; i < 3; i++) {
//            AddressItem addressItem = new AddressItem("deepak@gmail.com", "deepak", "8826510669", "22001", "Sheetla coloy", "Gurgaon", "Haryana", "Near Patrol Pump");
//            arrayList.add(addressItem);
//        }

        listView = (ListView) findViewById(R.id.address_lv_address);
        listView.setOnItemClickListener(this);
        addressAdapter = new AddressAdapter(this, R.layout.addrss_row, arrayList);
        listView.setAdapter(addressAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_btn_new_aadress:
                if (arrayList.size() < 5) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(android.R.id.content, new NewAddressFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.prompt_toast_address_allow), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.address_btn_payment:
                if (arrayList.size() > 0) {
                    Intent intent = new Intent(AddressActivity.this, WalletActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("AddressActivity ", "::::::List Item " + position);
        for (int i = 0; i < arrayList.size(); i++) {
            AddressItem addressItem = arrayList.get(i);
            addressItem.setIsAddessSelected(false);
        }
        AddressItem addressItem = arrayList.get(position);
        addressItem.setIsAddessSelected(true);
        addressAdapter.notifyDataSetChanged();
    }
}
