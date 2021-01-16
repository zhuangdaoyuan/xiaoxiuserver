package com.lxj.xpopupext.bean;


import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO<json数据源>
 *
 * @author: 小嵩
 * @date: 2017/3/16 15:36
 */

public class JsonBean implements IPickerViewData {


    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */

    @SerializedName(value = "label", alternate = {"name"})
    private String name;
    @SerializedName(value = "key", alternate = {"code"})
    private String code;
    @SerializedName(value = "children", alternate = {"cityList"})
    private List<CityBean> city;
    private ArrayList<IPickerViewData> iPickerViewData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCityList() {
        return city;
    }

    public void setCityList(List<CityBean> city) {
        this.city = city;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }

    @Override
    public String getPickerViewCode() {
        return code;
    }

    @Override
    public List<IPickerViewData> getPickerChilch() {
        if (iPickerViewData == null) {
            if (city != null) {
                iPickerViewData = new ArrayList<IPickerViewData>() {{
                    addAll((Collection<? extends IPickerViewData>) city);
                }};
            } else {
                return new ArrayList<>();
            }
        }
        return iPickerViewData;
    }


    public static class CityBean implements IPickerViewData {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */

        private String name;
        private String code;
        @SerializedName("areaList")
        private List<CityBean> area;
        private ArrayList<IPickerViewData> iPickerViewData;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<CityBean> getArea() {
            return area;
        }

        public void setArea(List<CityBean> area) {
            this.area = area;
        }

        @Override
        public String getPickerViewText() {
            return name;
        }

        @Override
        public String getPickerViewCode() {
            return code;
        }

        @Override
        public List<IPickerViewData> getPickerChilch() {
            if (iPickerViewData == null) {
                if (area != null) {
                    iPickerViewData = new ArrayList<IPickerViewData>() {{
                        addAll((Collection<? extends IPickerViewData>) area);
                    }};
                } else {
                    return new ArrayList<>();
                }
            }
            return iPickerViewData;
        }
    }
}
