package us.eiyou.demo_camera.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.activity.EditSeniorTuwenEditActivity;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;

/**
 * Created by Au on 2016/5/13.
 */
public class EditSeniorTuwenAdapter extends BaseAdapter {

    Context context;
    ArrayList list;
    Intent getIntent;
    AppCompatActivity activity;
    public EditSeniorTuwenAdapter(Context context, ArrayList list,Intent getIntent,AppCompatActivity activity) {
        this.context=context;
        this.list=list;
        this.getIntent=getIntent;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_edit_senior_tuwen, null);
        ViewHolder viewHolder = new ViewHolder(convertView);viewHolder.tvTuwen.setText(list.get(position).toString());
        viewHolder.bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                context.startActivity(new Intent(context, EditSeniorTuwenEditActivity.class).putExtra("position",position).putExtra("para",getIntent.getStringExtra("para")).putExtra("num",getIntent.getStringExtra("num")).putExtra("list",list).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        viewHolder.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Http.submit(Config.urlHead+"uploadfile/PictureUpdateServlet?path="+getIntent.getStringExtra("para")+"&num="+position+"&oper=delete");
                    }
                }).start();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_tuwen)
        TextView tvTuwen;
        @Bind(R.id.b_edit)
        BootstrapButton bEdit;
        @Bind(R.id.b_delete)
        BootstrapButton bDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
