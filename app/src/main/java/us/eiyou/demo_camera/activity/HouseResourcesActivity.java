package us.eiyou.demo_camera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.AddressPicker;
import us.eiyou.demo_camera.R;
import us.eiyou.demo_camera.utils.AssetsUtils;
import us.eiyou.demo_camera.utils.Config;
import us.eiyou.demo_camera.utils.Http;
import us.eiyou.demo_camera.utils.SP;

public class HouseResourcesActivity extends AppCompatActivity {

    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.price)
    EditText price;
    @Bind(R.id.area)
    EditText area;
    @Bind(R.id.age)
    EditText age;
    @Bind(R.id.floor)
    EditText floor;
    @Bind(R.id.floors)
    EditText floors;
    //    @Bind(R.id.room)
//    EditText room;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.contact)
    EditText contact;
    @Bind(R.id.b_submit)
    BootstrapButton bSubmit;
    @Bind(R.id.b_rent)
    BootstrapButton bRent;
    @Bind(R.id.b_sale)
    BootstrapButton bSale;
    @Bind(R.id.management_fee)
    EditText managementFee;
    int resourcesType = 1;
    @Bind(R.id.address1)
    EditText address1;
    @Bind(R.id.room0)
    EditText room0;
    @Bind(R.id.room1)
    EditText room1;
    @Bind(R.id.room2)
    EditText room2;
    @Bind(R.id.tev_title_content)
    TextView tevTitleContent;
    @Bind(R.id.structure)
    EditText structure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_resources);
        ButterKnife.bind(this);
        tevTitleContent.setText("房源信息编辑");
        getOldMessage();
    }

    private void getOldMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                JSONObject queryParameters = new JSONObject();
                JSONObject result = null;
                try {
                    jsonObject.put("serviceName", "findMyGoodsOrder3");
                    queryParameters.put("telphone", SP.getString(getApplicationContext(), "telephone"));
                    queryParameters.put("path", getIntent().getStringExtra("para").split("_")[1]);
                    jsonObject.put("queryParameters", queryParameters);
                    result = new JSONObject(Http.Result(Config.login_url, jsonObject.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = result;
                handler.sendMessage(message);
            }
        }).start();
    }

    @OnClick({R.id.address, R.id.structure, R.id.b_submit, R.id.b_rent, R.id.b_sale,R.id.btn_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_rent:
                resourcesType = 1;
                bRent.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                bSale.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                break;
            case R.id.b_sale:
                resourcesType = 2;
                bRent.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                bSale.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                price.setHint("总价  (万元)");
                break;
            case R.id.address:
                ArrayList<AddressPicker.Province> data = new ArrayList<>();
                String json = AssetsUtils.readText(this, "city.json");
                data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
                AddressPicker picker = new AddressPicker(this, data);
                picker.setSelectedItem("贵州", "贵阳", "花溪");
                picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(String province, String city, String county) {
                        address.setText(province + " " + city + " " + county);
                        name.requestFocus();
                    }
                });
                picker.show();
                break;
//            case R.id.room:
//                ArrayList<AddressPicker.Province> data1 = new ArrayList<>();
//                String json1 = AssetsUtils.readText(this, "room.json");
//                data1.addAll(JSON.parseArray(json1, AddressPicker.Province.class));
//                AddressPicker picker1 = new AddressPicker(this, data1);
//                picker1.setSelectedItem("4室", "1厅", "1卫");
//                picker1.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
//                    @Override
//                    public void onAddressPicked(String province, String city, String county) {
//                        room.setText(province + " " + city + " " + county);
//                        structure.requestFocus();
//                    }
//                });
//                picker1.show();
//                break;
            case R.id.structure:
                ArrayList<AddressPicker.Province> data2 = new ArrayList<>();
                String json2 = AssetsUtils.readText(this, "structure.json");
                data2.addAll(JSON.parseArray(json2, AddressPicker.Province.class));
                AddressPicker picker2 = new AddressPicker(this, data2);
                picker2.setSelectedItem("朝南", "钢筋混凝土结构", "精装修");
                picker2.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(String province, String city, String county) {
                        structure.setText(province + " " + city + " " + county);
                        phone.requestFocus();
                    }
                });
                picker2.show();
                break;
            case R.id.b_submit:
                if (room2.getText().toString().length() == 0 || structure.getText().toString().length() == 0 || address.getText().toString().length() == 0 || address1.getText().toString().length() == 0) {
                    Toast.makeText(this, "请完整信息后提交", Toast.LENGTH_SHORT).show();
                } else {
                    String Saddress = "";
                    String Sname = "";
                    String Scontact = "";
                    int Iroom = 1;
                    int Ioffice = 1;
                    int Itoilet = 1;
                    String Sorientation = "";
                    String Sstructure = "";
                    String Srenovation = "";
                    try {
                        Saddress = URLEncoder.encode(address.getText().toString() + ";" + address1.getText().toString(), "UTF-8");
                        Sname = URLEncoder.encode(name.getText().toString(), "UTF-8");
                        Scontact = URLEncoder.encode(contact.getText().toString(), "UTF-8");
//                        Iroom = Integer.parseInt(room.getText().toString().replace("室", "").replace("卫", "").replace("厅", "").split(" ")[0]);
//                        Ioffice = Integer.parseInt(room.getText().toString().replace("室", "").replace("卫", "").replace("厅", "").split(" ")[1]);
//                        Itoilet = Integer.parseInt(room.getText().toString().replace("室", "").replace("卫", "").replace("厅", "").split(" ")[2]);
                        Iroom = Integer.parseInt(room0.getText().toString());
                        Ioffice = Integer.parseInt(room1.getText().toString());
                        Itoilet = Integer.parseInt(room2.getText().toString());
                        Sorientation = URLEncoder.encode(structure.getText().toString().split(" ")[0], "UTF-8");
                        Sstructure = URLEncoder.encode(structure.getText().toString().split(" ")[1], "UTF-8");
                        Srenovation = URLEncoder.encode(structure.getText().toString().split(" ")[2], "UTF-8");
                    } catch (Exception e) {
                        Saddress = address.getText().toString() + ";" + address1.getText().toString();
                        Sname = contact.getText().toString();
                        Scontact = contact.getText().toString();
                        Sorientation = structure.getText().toString().split(" ")[0];
                        Sstructure = structure.getText().toString().split(" ")[1];
                        Srenovation = structure.getText().toString().split(" ")[2];
                    }
                    final String url = Config.urlHead + "uploadfile/RoomServlet?keytelphone=" + SP.getString(getApplicationContext(), "telephone") + "&filepath=" + getIntent().getStringExtra("para").split("_")[1] + "&type=" + resourcesType + "&name=" + Sname + "&address=" + Saddress + "&unitprice=" + price.getText().toString() + "&area=" + area.getText().toString() + "&propertyfee=" + managementFee.getText().toString() + "&age=" + age.getText().toString() + "&floor=" + floor.getText().toString() + "&totalfloor=" + floors.getText().toString() + "&room=" + Iroom + "&office=" + Ioffice + "&toilet=" + Itoilet + "&orientation=" + Sorientation + "&structure=" + Sstructure + "&renovation=" + Srenovation + "&telphone=" + phone.getText().toString() + "&contacts=" + Scontact;
                    Log.d("HouseResourcesActivity", url);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Http.submit(url);
                        }
                    }).start();
                    Toast.makeText(getApplicationContext(), "提交成功！", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MyHousePropertyActivity.class).putExtra("type",getIntent().getStringExtra("type")));
                }
                break;
            case R.id.btn_title_back:finish();break;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("HouseResourcesActivity", msg.toString());
            try {
                JSONObject jsonObject = (JSONObject) msg.obj;
                int status = jsonObject.getInt("status");
                if (1 == status) {
                    JSONObject dataList = jsonObject.getJSONObject("dataList");
                    if (dataList.getInt("type") == 1) {
                        bRent.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                        bSale.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                    } else if (dataList.getInt("type") == 2) {
                        bRent.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                        bSale.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                        price.setHint("总价  (万)");
                    }
                    name.setText(dataList.getString("name"));
                    address.setText(dataList.getString("address").split(";")[0]);
                    address1.setText(dataList.getString("address").split(";")[1]);
                    price.setText(dataList.getString("unitprice"));
                    area.setText(dataList.getString("area"));
                    managementFee.setText(dataList.getString("propertyfee"));
                    age.setText(dataList.getString("age"));
                    floors.setText(dataList.getString("totalfloor"));
                    floor.setText(dataList.getString("floor"));
//                    room.setText(dataList.getString("room") + "室 " + dataList.getString("office") + "厅 " + dataList.getString("toilet") + "卫");
                    room0.setText(dataList.getString("room"));
                    room1.setText(dataList.getString("office"));
                    room2.setText(dataList.getString("toilet"));
                    structure.setText(dataList.getString("orientation") + " " + dataList.getString("structure") + " " + dataList.getString("renovation"));
                    phone.setText(dataList.getString("telphone"));
                    contact.setText(dataList.getString("contacts"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
