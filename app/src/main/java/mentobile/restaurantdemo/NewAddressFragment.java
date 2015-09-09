package mentobile.restaurantdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import mentobile.utils.DBHandler;


public class NewAddressFragment extends Fragment implements View.OnClickListener {

    private EditText edEmail;
    private EditText edFullName;
    private EditText edPhoneNumber;
    private EditText edPincode;
    private EditText edDeliveryAddress;
    private EditText edCity;
    private EditText edState;
    private EditText edLandmark;

    private Button btnSave;
    private Button btnCancel;


    public  NewAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_address, container, false);
        edEmail = (EditText) view.findViewById(R.id.address_ed_email);
        edFullName = (EditText) view.findViewById(R.id.address_ed_fullname);
        edPhoneNumber = (EditText) view.findViewById(R.id.address_ed_phone);
        edPincode = (EditText) view.findViewById(R.id.address_ed_pincode);
        edDeliveryAddress = (EditText) view.findViewById(R.id.address_ed_delivery_aadress);
        edCity = (EditText) view.findViewById(R.id.address_ed_city);
        edState = (EditText) view.findViewById(R.id.address_ed_state);
        edLandmark = (EditText) view.findViewById(R.id.address_ed_landmark);

        btnSave = (Button) view.findViewById(R.id.address_btn_save);
        btnSave.setOnClickListener(this);
        btnCancel = (Button) view.findViewById(R.id.address_btn_cancel);
        btnCancel.setOnClickListener(this);
        return view;
    }

    private void newAddress() {

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
            ContentValues values = new ContentValues();
            values.put("EMAIL", email);
            values.put("FULLNAME", fullname);
            values.put("PHONE", phonenumber);
            values.put("PINCODE", pincode);
            values.put("DELIVERY_ADDRESS", address);
            values.put("CITY", city);
            values.put("STATE", state);
            values.put("LANDMARK", landmark);
            AddressActivity.dbHandler.insertData(DBHandler.TABLE_DELIVERY_ADDRESS, values);
            Intent intent = new Intent(getActivity(), WalletActivity.class);
            startActivity(intent);
            getActivity().getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_btn_save:
                newAddress();
                break;

            case R.id.address_btn_cancel:
                Application.removeFragment(this,getActivity());
                break;

        }
    }
}
