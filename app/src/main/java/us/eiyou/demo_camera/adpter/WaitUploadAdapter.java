package us.eiyou.demo_camera.adpter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.model.WaitUploadModel;

public class WaitUploadAdapter extends BaseAdapter{

	Context context;
	List<WaitUploadModel> list;
	// ��������CheckBox��ѡ��״��
	private static HashMap<Integer, Boolean> isSelected;
	
	public WaitUploadAdapter(Context context, List<WaitUploadModel> list) {
		super();
		this.context = context;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		// ��ʼ������
		initDate();
	}
	// ��ʼ��isSelected������
	private void initDate() {
		for (int i = 0; i <77; i++) {
			getIsSelected().put(i, false);
		}
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
		ViewHolder holder = null;
		if (view==null) {
			// ���ViewHolder����
			holder = new ViewHolder();
			view=LayoutInflater.from(context).inflate(R.layout.waitupload_listview_item, null);
			holder.textView=(TextView)view.findViewById(R.id.tev_waitupload_item);
			holder.imageView=(ImageView)view.findViewById(R.id.imageView);
			holder.checkBox=(AppCompatCheckBox)view.findViewById(R.id.checkBox);
			// Ϊview���ñ�ǩ
			view.setTag(holder);
		}else{
			// ȡ��holder
			holder = (ViewHolder)view.getTag();
		}
		holder.textView.setText(getItem(position).getWaitupload_name());
		holder.imageView.setImageBitmap(getItem(position).getBitmap());
		holder.checkBox.setChecked(getIsSelected().get(position));
		return view;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		WaitUploadAdapter.isSelected = isSelected;
	}
	public static class ViewHolder {
		public TextView textView;
		public ImageView imageView;
		public AppCompatCheckBox checkBox;
	}
}
