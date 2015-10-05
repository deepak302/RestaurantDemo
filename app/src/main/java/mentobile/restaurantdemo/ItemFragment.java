package mentobile.restaurantdemo;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class ItemFragment extends Fragment {

    String TAG = "VegFragment";
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayList<ItemDetail> arrayList = new ArrayList<>();

    public ItemFragment() {
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
            ItemDetail itemDetail = new ItemDetail("Veg", "Food Name " + i, 0, (100 + i * 2));
            arrayList.add(itemDetail);
        }
        listAdapter = new ListAdapter(getActivity(), R.layout.item_list, arrayList);
        listView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }
}
