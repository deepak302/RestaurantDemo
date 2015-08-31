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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 6/8/2015.
 */

public class AddressAdapter extends ArrayAdapter<AddressItem> {

    private Context context;
    private int resourceID;
    private ArrayList<AddressItem> arrayList = new ArrayList<>();

    public AddressAdapter(Context context, int resourceID, ArrayList<AddressItem> arrayList) {
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
    public AddressItem getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    RecordHolder holder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View gridView = convertView;
        if (gridView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(resourceID, viewGroup, false);
            holder = new RecordHolder();
            holder.tvAddress = (TextView) gridView.findViewById(R.id.addrow_tv_select);
            holder.rbSelectAddress = (RadioButton) gridView.findViewById(R.id.addrow_rb_select);
            holder.imgBtnDeleteItem = (ImageView) gridView.findViewById(R.id.addrow_img_delete);
            gridView.setTag(holder);
        } else {
            holder = (RecordHolder) gridView.getTag();
        }
        final AddressItem addressItem = arrayList.get(position);
        holder.tvAddress.setText(addressItem.getFullAddress());
        holder.rbSelectAddress.setChecked(addressItem.isAddessSelected());
        holder.imgBtnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, R.string.prompt_toast_addrow_delete, Toast.LENGTH_SHORT).show();
            }
        });
        return gridView;
    }

    static class RecordHolder {
        RadioButton rbSelectAddress;
        TextView tvAddress;
        ImageView imgBtnDeleteItem;
    }
}
