package com.lxj.xpopupext.popup;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.contrarywind.interfaces.IPickerViewData;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopupext.R;
import com.lxj.xpopupext.bean.JsonBean;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.OnOptionsSelectListener;
import com.lxj.xpopupext.view.WheelOptions;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CityPickerPopup extends BottomPopupView {

    private List<IPickerViewData> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<IPickerViewData>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    private CityPickerListener cityPickerListener;
    private WheelOptions wheelOptions;
    public int dividerColor = 0xFFd5d5d5; //分割线的颜色
    public float lineSpace = 2.4f; // 条目间距倍数 默认2
    public int textColorOut = 0xFFa8a8a8; //分割线以外的文字颜色
    public int textColorCenter = 0xFF2a2a2a; //分割线之间的文字颜色
    private boolean isHide;
    private ArrayList<IPickerViewData> optionsAllItems = new ArrayList<>();

    public CityPickerPopup(@NonNull Context context) {
        super(context);
    }
    public CityPickerPopup(@NonNull Context context,boolean isHide) {
        super(context);
        this.isHide=isHide;
    }


    public CityPickerPopup(@NonNull Context context, ArrayList<IPickerViewData> list) {
        super(context);
        this.optionsAllItems = list;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_ext_city_picker;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setTextColor(XPopup.getPrimaryColor());
        btnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityPickerListener != null) {
                    int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                    int options1 = optionsCurrentItems[0];
                    int options2 = optionsCurrentItems[1];
                    int options3 = optionsCurrentItems[2];




                    IPickerViewData safe1 = CollectionUtil.getSafe(options1Items, options1, null);

                    IPickerViewData safe2;
                    ArrayList<IPickerViewData> safe = CollectionUtil.getSafe(options2Items, options1, null);
                    if (safe == null) {
                        safe2 = null;
                    } else {
                        safe2 = CollectionUtil.getSafe(safe, options2, null);
                    }

                    IPickerViewData safe3;
                    ArrayList<ArrayList<IPickerViewData>> safe4 = CollectionUtil.getSafe(options3Items, options1, null);
                    if (safe4 == null) {
                        safe3 = null;
                    } else {
                        ArrayList<IPickerViewData> safe5 = CollectionUtil.getSafe(safe4, options2, null);
                        if (safe5 == null) {
                            safe3 = null;
                        } else {
                            safe3 = CollectionUtil.getSafe(safe5, options3, null);
                        }
                    }



                    cityPickerListener.onCityConfirm(safe1,  safe2,safe3, v);
                }
                dismiss();
            }
        });

        wheelOptions = new WheelOptions(findViewById(R.id.citypicker), false);
        if (cityPickerListener != null) {
            wheelOptions.setOptionsSelectChangeListener(new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3) {
                    IPickerViewData safe1 = CollectionUtil.getSafe(options1Items, options1, null);

                    IPickerViewData safe2;
                    ArrayList<IPickerViewData> safe = CollectionUtil.getSafe(options2Items, options1, null);
                    if (safe == null) {
                        safe2 = null;
                    } else {
                        safe2 = CollectionUtil.getSafe(safe, options2, null);
                    }

                    IPickerViewData safe3;
                    ArrayList<ArrayList<IPickerViewData>> safe4 = CollectionUtil.getSafe(options3Items, options1, null);
                    if (safe4 == null) {
                        safe3 = null;
                    } else {
                        ArrayList<IPickerViewData> safe5 = CollectionUtil.getSafe(safe4, options2, null);
                        if (safe5 == null) {
                            safe3 = null;
                        } else {
                            safe3 = CollectionUtil.getSafe(safe5, options3, null);
                        }
                    }
                    cityPickerListener.onCityChange(safe1, safe2, safe3);
                }
            });
        }
        wheelOptions.setTextContentSize(18);
        wheelOptions.setItemsVisible(7);
        wheelOptions.setAlphaGradient(true);
        wheelOptions.setCyclic(false);

        wheelOptions.setDividerColor(dividerColor);
        wheelOptions.setDividerType(WheelView.DividerType.FILL);
        wheelOptions.setLineSpacingMultiplier(lineSpace);
        wheelOptions.setTextColorOut(textColorOut);
        wheelOptions.setTextColorCenter(textColorCenter);
        wheelOptions.isCenterLabel(false);

        if (!options1Items.isEmpty() && !options2Items.isEmpty() && !options3Items.isEmpty()) {
            //有数据直接显示
            if (wheelOptions != null) {
                wheelOptions.setPicker(options1Items, options2Items, options3Items);
                wheelOptions.setCurrentItems(0, 0, 0);
            }
        } else {
            initJsonData();
        }

        if (isHide) {
            findViewById(R.id.options3).setVisibility(View.INVISIBLE);
        }else{
            findViewById(R.id.options3).setVisibility(View.VISIBLE);
        }
    }

    public CityPickerPopup setCityPickerListener(CityPickerListener listener) {
        this.cityPickerListener = listener;
        return this;
    }

    private void initJsonData() {//解析数据
        ArrayList<IPickerViewData> jsonBean;
        if (optionsAllItems.isEmpty()) {
            String JsonData = readJson(getContext(), "province.json");//获取assets目录下的json文件数据
            jsonBean = parseData(JsonData);//用Gson 转成实体
        } else {
            jsonBean = optionsAllItems;
        }

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
//        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            options1Items.add(jsonBean.get(i));
            ArrayList<IPickerViewData> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<IPickerViewData>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getPickerChilch().size(); c++) {//遍历该省份的所有城市
                IPickerViewData cityName = jsonBean.get(i).getPickerChilch().get(c);
                cityList.add(cityName);//添加城市
                ArrayList<IPickerViewData> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getPickerChilch().get(c).getPickerChilch());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        wheelOptions.setPicker(options1Items, options2Items, options3Items);
        wheelOptions.setCurrentItems(0, 0, 0);

    }

    public static ArrayList<IPickerViewData> parseData(String result) {//Gson 解析
        ArrayList<IPickerViewData> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    public static String readJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
