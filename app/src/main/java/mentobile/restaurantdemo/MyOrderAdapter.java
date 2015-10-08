package mentobile.restaurantdemo;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 10/7/2015.
 */
public class MyOrderAdapter extends ArrayAdapter<MyOrder> {

    private Context context;
    private int resourceID;
    private ArrayList<MyOrder> arrayList = new ArrayList<>();


    public MyOrderAdapter(Context context, int resource, ArrayList<MyOrder> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resourceID = resource;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public MyOrder getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    RecordHolder holder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View gridView = convertView;
        if (gridView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(resourceID, viewGroup, false);
            holder = new RecordHolder();
            holder.tvOrderID = (TextView) gridView.findViewById(R.id.item_order_tv_orderno);
            holder.tvOrderDetails = (TextView) gridView.findViewById(R.id.item_order_tv_details);
            holder.tvOrderAmount = (TextView) gridView.findViewById(R.id.item_order_tv_amount);
            gridView.setTag(holder);
        } else {
            holder = (RecordHolder) gridView.getTag();
        }
        final MyOrder myOrder = arrayList.get(position);
        holder.tvOrderID.setText("" + myOrder.getOrder_id());
        holder.tvOrderDetails.setText("" + myOrder.getOrder_detail());
        holder.tvOrderAmount.setText("" + myOrder.getAmount());
        return gridView;
    }

    static class RecordHolder {
        TextView tvOrderID;
        TextView tvOrderDetails;
        TextView tvOrderAmount;
    }
}
