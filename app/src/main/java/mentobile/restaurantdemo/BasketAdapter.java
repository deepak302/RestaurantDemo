package mentobile.restaurantdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 6/8/2015.
 */

public class BasketAdapter extends ArrayAdapter<ItemDetail> {

    private Context context;
    private int resourceID;
    private ArrayList<ItemDetail> arrayList = new ArrayList<>();

    public BasketAdapter(Context context, int resourceID, ArrayList<ItemDetail> arrayList) {
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

    RecordHolder holder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View gridView = convertView;
        if (gridView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(resourceID, viewGroup, false);
            holder = new RecordHolder();
            holder.tvItemCounter = (TextView) gridView.findViewById(R.id.basket_tv_item_counter);
            holder.tvItemName = (TextView) gridView.findViewById(R.id.basket_tv_name);
            holder.tvItemQuantity = (TextView) gridView.findViewById(R.id.basket_tv_quantity);
            holder.tvItemPrice = (TextView) gridView.findViewById(R.id.basket_tv_price);
            holder.imgViewPlus = (ImageButton) gridView.findViewById(R.id.basket_imgbtn_plus);
            holder.imgViewMinus = (ImageButton) gridView.findViewById(R.id.basket_imgbtn_minus);
            gridView.setTag(holder);
        } else {
            holder = (RecordHolder) gridView.getTag();
        }
        final ItemDetail itemDetail = arrayList.get(position);
        holder.tvItemCounter.setText((position + 1) + ". ");
        Log.d("    ", ":::::::::Basket " + position);
        holder.tvItemName.setText(itemDetail.getName());
        holder.tvItemQuantity.setText(itemDetail.getQuantity() + " x ");
        holder.tvItemPrice.setText("$" + itemDetail.getPriceOverQuantity());
        if (ItemDetail.isEditItem) {
            holder.imgViewPlus.setVisibility(View.VISIBLE);
            holder.imgViewMinus.setVisibility(View.VISIBLE);
        } else {
            holder.imgViewPlus.setVisibility(View.GONE);
            holder.imgViewMinus.setVisibility(View.GONE);
        }
        holder.imgViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.aplha);
                view.startAnimation(animation);
                itemDetail.setQuantity(itemDetail.getQuantity() + 1);
                ItemDetail.setTotalPrice(ItemDetail.getTotalPrice() + itemDetail.getPrice());
                BasketActivity.tvTotalPrice.setText("$ " + ItemDetail.getTotalPrice());
                notifyDataSetChanged();
            }
        });
        holder.imgViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.aplha);
                view.startAnimation(animation);
                itemDetail.setQuantity(itemDetail.getQuantity() - 1);
                ItemDetail.setTotalPrice(ItemDetail.getTotalPrice() - itemDetail.getPrice());
                if (itemDetail.getQuantity() < 1 && arrayList.contains(itemDetail)) {
                    arrayList.remove(itemDetail);
                }
                BasketActivity.tvTotalPrice.setText("$ " + ItemDetail.getTotalPrice());
                notifyDataSetChanged();
            }
        });
        return gridView;
    }

    static class RecordHolder {
        ImageButton imgViewPlus;
        ImageButton imgViewMinus;
        TextView tvItemCounter;
        TextView tvItemName;
        TextView tvItemQuantity;
        TextView tvItemPrice;
    }
}
