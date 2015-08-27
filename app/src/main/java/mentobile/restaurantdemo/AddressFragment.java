package mentobile.restaurantdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mentobile.utils.DBHandler;
import mentobile.utils.JsonParser;

public class AddressFragment extends Fragment implements View.OnClickListener {

    private Button btnAddNewAddress;
    private Button btnProceedToPayment;

    private EditText edEmail;
    private EditText edFullName;
    private EditText edPhoneNumber;
    private EditText edPincode;
    private EditText edDeliveryAddress;
    private EditText edCity;
    private EditText edState;
    private EditText edLandmark;

    private ScrollView scrollView;
    private ListView listView;
    private AddressAdapter addressAdapter;
    private ArrayList<AddressItem> arrayList = new ArrayList<>();
    private DBHandler dbHandler;

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(getActivity(), 1);
//        if (!dbHandler.isTableEmplty(DBHandler.TABLE_DELIVERY_ADDRESS)) {
//            Cursor cursor = dbHandler.getAllDeliveryAddress();
//            while (cursor.moveToNext()) {
//                AddressItem addressItem = new AddressItem(cursor.getString(0), cursor.getString(1), cursor.getString(2),
//                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
//                arrayList.add(addressItem);
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        btnAddNewAddress = (Button) view.findViewById(R.id.address_btn_new_aadress);
        btnAddNewAddress.setOnClickListener(this);
        btnProceedToPayment = (Button) view.findViewById(R.id.address_btn_payment);
        btnProceedToPayment.setOnClickListener(this);

        edEmail = (EditText) view.findViewById(R.id.address_ed_email);
        edFullName = (EditText) view.findViewById(R.id.address_ed_fullname);
        edPhoneNumber = (EditText) view.findViewById(R.id.address_ed_phone);
        edPincode = (EditText) view.findViewById(R.id.address_ed_pincode);
        edDeliveryAddress = (EditText) view.findViewById(R.id.address_ed_delivery_aadress);
        edCity = (EditText) view.findViewById(R.id.address_ed_city);
        edState = (EditText) view.findViewById(R.id.address_ed_state);
        edLandmark = (EditText) view.findViewById(R.id.address_ed_landmark);

        scrollView = (ScrollView) view.findViewById(R.id.address_scrollview);
        for (int i = 0; i < 3; i++) {
            AddressItem addressItem = new AddressItem("deepak@gmail.com", "deepak", "8826510669", "22001", "Sheetla coloy", "Gurgaon", "Haryana", "Near Patrol Pump");
            arrayList.add(addressItem);
        }
        listView = (ListView) view.findViewById(R.id.address_lv_address);
        addressAdapter = new AddressAdapter(getActivity(), R.layout.addrss_row, arrayList);
        listView.setAdapter(addressAdapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_btn_new_aadress:
                scrollView.setVisibility(View.VISIBLE);
                btnAddNewAddress.setVisibility(View.GONE);
                break;
            case R.id.address_btn_payment:
                setAddress();
                break;
        }
    }

    private void setAddress() {

        String email = edEmail.getText().toString().trim();
        String fullname = edFullName.getText().toString().trim();
        String phonenumber = edPhoneNumber.getText().toString().trim();
        final String pincode = edPincode.getText().toString().trim();
        String address = edDeliveryAddress.getText().toString().trim();
        String city = edCity.getText().toString().trim();
        String state = edState.getText().toString().trim();
        String landmark = edLandmark.getText().toString().trim();

        if (email.length() < 1) {
            edEmail.setError(getString(R.string.error_email_empty_email));
            edEmail.requestFocus();
        } else if (!Application.isValidEmail(email)) {
            edEmail.setError(getString(R.string.error_email_verify));
            edEmail.requestFocus();
        } else if (fullname.length() < 1) {
            edFullName.setError(getString(R.string.error_name));
            edFullName.requestFocus();
        } else if (phonenumber.length() < 10) {
            edPhoneNumber.setError(getString(R.string.error_mobile));
            edPhoneNumber.requestFocus();
        } else if (pincode.length() < 6) {
            edPincode.setError(getString(R.string.error_pincode));
            edPincode.requestFocus();
        } else if (address.length() < 1) {
            edDeliveryAddress.setError(getString(R.string.error_delivery_address));
            edDeliveryAddress.requestFocus();
        } else if (city.length() < 1) {
            edCity.setError(getString(R.string.error_city));
            edCity.requestFocus();
        } else if (state.length() < 1) {
            edState.setError(getString(R.string.error_state));
            edState.requestFocus();
        } else {
            AddressItem addressItem = new AddressItem(email, fullname, phonenumber, pincode, address, city, state, "" + landmark);
            arrayList.add(addressItem);
            Toast.makeText(getActivity(), "Wallet Activity", Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            values.put("EMAIL", email);
            values.put("FULLNAME", fullname);
            values.put("PHONE", phonenumber);
            values.put("PINCODE", pincode);
            values.put("DELIVERY_ADDRESS", address);
            values.put("CITY", city);
            values.put("STATE", state);
            values.put("LANDMARK", landmark);
            dbHandler.insertData(DBHandler.TABLE_DELIVERY_ADDRESS, values);
            Intent intentWallet = new Intent(getActivity(), WalletActivity.class);
            startActivity(intentWallet);
        }
    }
}
