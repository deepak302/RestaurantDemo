package mentobile.restaurantdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mentobile.utils.DBHandler;
import mentobile.utils.WebService;


public class SignupFragment extends Fragment implements View.OnClickListener {

    final String TAG = "SignupFragment";
    private EditText edName;
    private EditText edEmail;
    private EditText edMobile;
    private EditText edPassword;
    private EditText edCPassword;
//    private EditText edCity;
//    private EditText edLocation;
//    private EditText edCompany;
//    private EditText edFlatNo;
//    private EditText edApartmentNo;
//    private EditText edPostCode;
//    private EditText edOtherAddress;
//    private EditText edDeliveryIns;
    private CheckBox chkTC;
    private Button btnCreateAccount;
    private String strEmail;
    CProgressDialog cProgressDialog;
    private WebService webService;
    private ArrayList<NameValuePair> listValue;
    private DBHandler dbHandler;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(getActivity(), 1);
        getActivity().getActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        edName = (EditText) view.findViewById(R.id.signup_ed_fname);
//        edLName = (EditText) view.findViewById(R.id.signup_ed_lname);
        edEmail = (EditText) view.findViewById(R.id.signup_ed_email);
        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                strEmail = edEmail.getText().toString().trim();
                if (strEmail.length() > 1 && !hasFocus) {
                    if (!Application.isValidEmail(strEmail)) {
                        Log.d(TAG, ":::::ValidEmail" + hasFocus);
                        edEmail.setError(getString(R.string.error_email_verify));
                    }
                }
            }
        });
        edMobile = (EditText) view.findViewById(R.id.signup_ed_mobile);
        edPassword = (EditText) view.findViewById(R.id.signup_ed_password);
        edCPassword = (EditText) view.findViewById(R.id.signup_ed_cpassword);
//        edCity = (EditText) view.findViewById(R.id.signup_ed_city);
//        edLocation = (EditText) view.findViewById(R.id.signup_ed_location);
//        edCompany = (EditText) view.findViewById(R.id.signup_ed_company);
//        edFlatNo = (EditText) view.findViewById(R.id.signup_ed_flat);
//        edApartmentNo = (EditText) view.findViewById(R.id.signup_ed_apartment);
//        edPostCode = (EditText) view.findViewById(R.id.signup_ed_postcode);
//        edOtherAddress = (EditText) view.findViewById(R.id.signup_ed_optaddress);
//        edDeliveryIns = (EditText) view.findViewById(R.id.signup_ed_delv_ins);
        chkTC = (CheckBox) view.findViewById(R.id.signup_chk_tc);
        btnCreateAccount = (Button) view.findViewById(R.id.signup_btn_createaccount);
        btnCreateAccount.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn_createaccount:

                final String fName = edName.getText().toString().trim();
//                final String lName = edLName.getText().toString().trim();
                final String mobile = edMobile.getText().toString().trim();
                final String password = edPassword.getText().toString().trim();
                final String cPassword = edCPassword.getText().toString().trim();
//                final String city = edCity.getText().toString().trim();
//                final String location = edLocation.getText().toString().trim();
//                final String company = edCompany.getText().toString().trim();
//                final String flat = edFlatNo.getText().toString().trim();
//                final String apartment = edApartmentNo.getText().toString().trim();
//                final String postcode = edPostCode.getText().toString().trim();
//                final String otherAddress = edOtherAddress.getText().toString().trim();
//                final String delIns = edDeliveryIns.getText().toString().trim();

                if (fName.length() < 1) {
                    edName.setError(getString(R.string.error_fname));
                }
//                else if (lName.length() < 1) {
//                    edLName.setError(getString(R.string.error_lname));
//                }
                else if (mobile.length() < 1) {
                    edMobile.setError(getString(R.string.error_mobile));
                } else if (password.length() < 6) {
                    edPassword.setError(getString(R.string.error_password));
                } else if (!password.equals(cPassword)) {
                    edCPassword.setError(getString(R.string.error_cpassword));
                }
//                else if (city.length() < 1) {
//                    edCity.setError(getString(R.string.error_city));
//                } else if (flat.length() < 1) {
//                    edFlatNo.setError(getString(R.string.error_flat));
//                } else if (!chkTC.isChecked()) {
//                    chkTC.setError(getString(R.string.prompt_chk_tc));
//                }
                else {
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            cProgressDialog = new CProgressDialog(getActivity());
                            cProgressDialog.setMessage("Please wait...");
                            cProgressDialog.show();
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            webService = new WebService();
                            listValue = new ArrayList<NameValuePair>();
                            listValue.add(new BasicNameValuePair("fname", fName));
//                            listValue.add(new BasicNameValuePair("lname", lName));
                            listValue.add(new BasicNameValuePair("email", strEmail));
                            listValue.add(new BasicNameValuePair("mobile", mobile));
                            listValue.add(new BasicNameValuePair("pass", password));
                            listValue.add(new BasicNameValuePair("cpass", cPassword));
//                            listValue.add(new BasicNameValuePair("city", city));
//                            listValue.add(new BasicNameValuePair("location", location));
//                            listValue.add(new BasicNameValuePair("company", company));
//                            listValue.add(new BasicNameValuePair("flatno", flat));
//                            listValue.add(new BasicNameValuePair("apartment", apartment));
//                            listValue.add(new BasicNameValuePair("postcode", postcode));
//                            listValue.add(new BasicNameValuePair("otheradd", otherAddress));
//                            listValue.add(new BasicNameValuePair("del_inst", delIns));
                            listValue.add(new BasicNameValuePair("type", "2"));
                            JSONObject json = webService.makeHttpRequest("signup", listValue);
                            try {
                                String success = json.getString("description");
                                return success;

                            } catch (JSONException e) {
                                Log.d(TAG, ":::::Exception " + e.getMessage());
                            }
                            return "Invalid";
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            Log.d(TAG, "::::Result " + result);
                            Toast.makeText(getActivity(),""+result,Toast.LENGTH_SHORT).show();
                            cProgressDialog.hide();
                            if (result.equalsIgnoreCase("success")) {
                                Log.d(TAG,"::::Email "+strEmail);
                                Application.setDataInSharedPreference(getActivity(), Application.SP_LOGIN_LOGOUT, "email", strEmail);
                                ContentValues values = new ContentValues();
                                values.put("FNAME", "");
                                values.put("LNAME", "");
                                values.put("FULLNAME", fName);
                                values.put("EMAIL", strEmail);
                                values.put("MOBILE", mobile);
                                values.put("CITY", "");
                                values.put("LOCATION", "");
                                values.put("COMPANY", "");
                                values.put("FLATNO", "");
                                values.put("APARTMENT", "");
                                values.put("POSTCODE", "");
                                values.put("OTHERADDRESS", "");
                                values.put("DELINS", "");

                                dbHandler.insertData(DBHandler.TABLE_USER_PROFILE, values);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().finish();
                                startActivity(intent);
                            }
                        }
                    }.execute();
                }
                break;
        }
    }
}
