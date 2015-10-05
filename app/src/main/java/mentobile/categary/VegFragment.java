package mentobile.categary;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mentobile.restaurantdemo.Application;
import mentobile.restaurantdemo.CProgressDialog;
import mentobile.restaurantdemo.GridAdapter;
import mentobile.restaurantdemo.GridItem;
import mentobile.restaurantdemo.ItemDetail;
import mentobile.restaurantdemo.ListAdapter;
import mentobile.restaurantdemo.MainActivity;
import mentobile.restaurantdemo.R;
import mentobile.restaurantdemo.SelectedActivity;
import mentobile.utils.WebService;

public class VegFragment extends Fragment {

    String TAG = "VegFragment";
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<ItemDetail>[] arrayList = new ArrayList[MainActivity.arrListGridItem.size()];
    private CProgressDialog cProgressDialog;
    private ArrayList<NameValuePair> listValue;
    private WebService webService;

    public VegFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cProgressDialog = new CProgressDialog(getActivity());
        webService = new WebService();
        GridItem item = MainActivity.arrListGridItem.get(SelectedActivity.pos);
        listValue = new ArrayList<NameValuePair>();
        listValue.add(new BasicNameValuePair("category", item.getItemType()));
        Log.d(TAG, ":::::: Category NAme " + item.getItemType());
        Log.d(TAG, ":::::: Category Array " + arrayList[SelectedActivity.pos]);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        listView = (ListView) view.findViewById(R.id.items_lv_main);
//        for (int i = 0; i < 20; i++) {
//            ItemDetail itemDetail = new ItemDetail("Veg", "Food Name " + i, 0, (100 + i * 2));
//            arrayList.add(itemDetail);
//        }

        if (arrayList[SelectedActivity.pos] == null) {
            arrayList[SelectedActivity.pos] = new ArrayList<>();
            MyAsynchTask myAsynchTask = new MyAsynchTask();
            myAsynchTask.execute();
        } else {
            listAdapter = new ListAdapter(getActivity(), R.layout.item_list, arrayList[SelectedActivity.pos]);
            listView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }
        return view;
    }

    private class MyAsynchTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cProgressDialog.show();
            Drawable drawable = getResources().getDrawable(R.drawable.my_progress_indeterminate);
            cProgressDialog.setIndeterminateDrawable(drawable);
            cProgressDialog.setMessage("Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "::::::Json 1");
            JSONObject json = webService.makeHttpRequest("SelectCategory", listValue);
            try {
                JSONArray jsonArray = json.getJSONArray("description");
                Log.d(TAG, "::::::JSon Array " + jsonArray.toString());
                int length = jsonArray.length();
                for (int i = 1; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String type = jsonObject.getString("Category_Name");
                    String name = jsonObject.getString("Item_Name");
                    int price = jsonObject.getInt("Price");
                    Log.d(TAG, "::::::Json " + name);
                    ItemDetail itemDetail = new ItemDetail(type, name, 0, price);
                    arrayList[SelectedActivity.pos].add(itemDetail);
                }
                return "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Invalid";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (s.equalsIgnoreCase("invalid")) {
//                Toast.makeText(getActivity(), " " + s, Toast.LENGTH_SHORT).show();
//            }
            cProgressDialog.hide();
            Log.d(TAG, "::::Array " + arrayList[SelectedActivity.pos].size());
            if (getActivity() != null) {
                listAdapter = new ListAdapter(getActivity(), R.layout.item_list, arrayList[SelectedActivity.pos]);
                listView.setAdapter(listAdapter);
            }
        }
    }

}
