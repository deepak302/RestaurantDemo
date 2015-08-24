package mentobile.restaurantdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class SMenuAdapter extends ArrayAdapter<Items> {

	private Context context;;
	private ArrayList<Items> arrayList = new ArrayList<Items>();
	private int resourceID;

	public SMenuAdapter(Context context, int resourceId, ArrayList<Items> data) {
		// TODO Auto-generated constructor stub
		super(context, resourceId, data);
		this.context = context;
		this.resourceID = resourceId;
		this.arrayList = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Items getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}
	private int value = 2 ;

	@Override
	public View getView(int position, View convertview, ViewGroup viewGroup) {
		// TODO Auto-generated method stub

		RecordHolder holder = null;
		View gridView = convertview;

		if (gridView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			gridView = inflater.inflate(resourceID, viewGroup, false);
			holder = new RecordHolder();
			holder.imageView = (ImageView) gridView.findViewById(R.id.xml_nv_image);
			holder.textview = (TextView) gridView.findViewById(R.id.xml_nv_title);
			gridView.setTag(holder);
		} else {
			holder = (RecordHolder) gridView.getTag();
			value = 5 ;
		}
		Items item = arrayList.get(position);
		holder.imageView.setBackgroundResource(item.getImage());
		holder.textview.setText(item.getTitle());
		return gridView;

	}

	static class RecordHolder {
		TextView textview;
		ImageView imageView;
	}
}
