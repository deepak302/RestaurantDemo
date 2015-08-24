package mentobile.restaurantdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 6/8/2015.
 */

public class ListAdapter extends ArrayAdapter<ItemDetail> {

    private Context context;
    private int resourceID;
    private ArrayList<ItemDetail> arrayList = new ArrayList<ItemDetail>();

    public ListAdapter(Context context, int resourceID, ArrayList<ItemDetail> arrayList) {
        super(context, resourceID, arrayList);
        this.context = context;
        this.resourceID = resourceID;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ItemDetail getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        RecordHolder holder = null;
        View gridView = convertView;

        if (gridView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(resourceID, viewGroup, false);
            holder = new RecordHolder();
            holder.itemName = (TextView) gridView.findViewById(R.id.item_tv_name);
            holder.itemQuantity = (TextView) gridView.findViewById(R.id.item_tv_quantity);
            holder.itemPrice = (TextView) gridView.findViewById(R.id.item_tv_price);
            gridView.setTag(holder);
        } else {
            holder = (RecordHolder) gridView.getTag();
        }
        ItemDetail itemDetail = arrayList.get(position);
        holder.itemName.setText(itemDetail.getName());
        holder.itemQuantity.setText(itemDetail.getQuantity());
        holder.itemPrice.setText(itemDetail.getPrice());
        return gridView;
    }

    static class RecordHolder {
        TextView itemName;
        TextView itemQuantity;
        TextView itemPrice;


    }
}
