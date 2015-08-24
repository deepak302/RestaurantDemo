package mentobile.restaurantdemo;

import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class VegFragment extends Fragment {

    String TAG = "VegFragment";
    private ListView listView;
    private ListAdapter listAdapter;
    private ArrayList<ItemDetail> arrayList = new ArrayList<>();

    public VegFragment() {
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        listView = (ListView) view.findViewById(R.id.items_lv_main);
        for (int i = 0; i < 20; i++) {
            ItemDetail itemDetail = new ItemDetail("Veg", "Food Name " + i, 1, (100 + i * 2));
            arrayList.add(itemDetail);
        }
        listAdapter = new ListAdapter(getActivity(), R.layout.item_list, arrayList);
        listView.setAdapter(listAdapter);
        return view;
    }
}