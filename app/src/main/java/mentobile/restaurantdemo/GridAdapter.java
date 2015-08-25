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

public class GridAdapter extends ArrayAdapter<GridItem> {

    private Context context;
    private int resourceID;
    private ArrayList<GridItem> arrayList = new ArrayList<GridItem>();

    public GridAdapter(Context context, int resourceID, ArrayList<GridItem> arrayList) {
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
    public GridItem getItem(int position) {
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
            holder.imageView = (ImageView) gridView.findViewById(R.id.custom_img_appicon);
            holder.textView = (TextView) gridView.findViewById(R.id.custom_tv_appname);
            gridView.setTag(holder);
        } else {
            holder = (RecordHolder) gridView.getTag();
        }
        GridItem gridItem = arrayList.get(position);
        holder.imageView.setImageDrawable(gridItem.getItemICon());
        holder.textView.setText(gridItem.getItemType());
        return gridView;
    }

    static class RecordHolder {
        ImageView imageView;
        TextView textView;
    }
}
