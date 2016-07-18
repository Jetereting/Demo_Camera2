package us.eiyou.demo_camera.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.model.WaitUploadModel;

public class LookShareAdapter extends BaseAdapter implements OnClickListener{

	Context context;
	List<WaitUploadModel> list;
	private static HashMap<Integer, Boolean> isSelected;
	int type;
	private Callback mCallback;


	public LookShareAdapter(Context context, List<WaitUploadModel> list, int type, Callback callback) {
		super();
		this.context = context;
		this.list = list;
		this.type=type;
		mCallback=callback;
	}
	public interface Callback {
		public void click(View v);
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public WaitUploadModel getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (view==null) {
			holder = new ViewHolder();
			if(type==1){
				view=LayoutInflater.from(context).inflate(R.layout.lookshare_listview_item, null);
			}else{
				view=LayoutInflater.from(context).inflate(R.layout.lookshare_listview_item1, null);
			}
			holder.imageView=(ImageView)view.findViewById(R.id.imageView);
			holder.button=(Button)view.findViewById(R.id.button);
			holder.button1=(Button)view.findViewById(R.id.button1);
			holder.button2=(Button)view.findViewById(R.id.button2);
			holder.tv_name=(TextView)view.findViewById(R.id.tv_name);
			holder.tv_addtime=(TextView)view.findViewById(R.id.tv_addtime);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		holder.imageView.setImageBitmap(getItem(position).getBitmap());
        if(!getItem(position).getAddtime().equals("null")) {
            holder.tv_name.setText(getItem(position).getName());
            holder.tv_addtime.setText(getItem(position).getAddtime());
        }
		holder.button.setOnClickListener(this);
		holder.button.setTag(position);
		holder.button1.setOnClickListener(this);
		holder.button1.setTag(position);
		holder.button2.setOnClickListener(this);
		holder.button2.setTag(position);
		if(type==1){
			holder.button1.setVisibility(View.GONE);
		}
		return view;
	}
	public static class ViewHolder {
		public ImageView imageView;
		public Button button;//show
		public Button button1;//edit
		public Button button2;//delete
		TextView tv_name;
		TextView tv_addtime;
	}
	@Override
	public void onClick(View v) {
		mCallback.click(v);
	}

}

