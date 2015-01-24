package mvg.com.jsonprinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Max on 1/23/2015.
 */
public class LineAdapter extends BaseAdapter {
	ArrayList<LineEntry> events;
	protected LayoutInflater layoutInflater;

	public LineAdapter(Context context){
		layoutInflater = LayoutInflater.from(context);
		events = new ArrayList<LineEntry>();
	}

	public void init(JSONArray events, Bitmap[] images){
		for(int i = 0; i < events.length(); i++){
			try {
				this.events.add(new LineEntry((String)((JSONObject)(events.get(i))).get("name"), images[i]));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getCount() {
		return events.size();
	}

	@Override
	public Object getItem(int position) {
		return events.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = layoutInflater.inflate(R.layout.event_entry, parent, false);
		TextView name = (TextView)convertView.findViewById(R.id.tv_name);
		ImageView icon = (ImageView)convertView.findViewById(R.id.imgv_icon);
		name.setText(events.get(position).getName());
		icon.setImageBitmap(events.get(position).getIcon());
		return convertView;
	}
}
