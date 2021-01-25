package com.xiuxiu.confinement_nurse.ui.citypicker.model;

import android.text.TextUtils;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author Bro0cL on 2016/1/26.
 */
public class City implements Serializable, IPickerViewData {
    @SerializedName(value = "label", alternate = {"name"})
    private String name;
    private String province;
    private String pinyin;
    @SerializedName(value = "key", alternate = {"code"})
    private String code;
    protected List<City> children;

    private double latitude;
    private double longitude;
    private String cityCode;

    public City() {
    }



    public City(String name, String province, String pinyin, String code) {
        this.name = name;
        this.province = province;
        this.pinyin = pinyin;
        this.code = code;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<City> getChildren() {
        return children == null ? new ArrayList<City>() : children;
    }

    public void setChildren(List<City> children) {
        this.children = children;
    }

    /***
     * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
     * @return
     */
    public String getSection() {
        if (TextUtils.isEmpty(pinyin)) {
            return "#";
        } else {
            String c = pinyin.substring(0, 1);
            Pattern p = Pattern.compile("[a-zA-Z]");
            Matcher m = p.matcher(c);
            if (m.matches()) {
                return c.toUpperCase();
            }
            //在添加定位和热门数据时设置的section就是‘定’、’热‘开头
            else if (TextUtils.equals(c, "定") || TextUtils.equals(c, "热"))
                return pinyin;
            else
                return "#";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    private ArrayList<IPickerViewData> iPickerViewData;

    @Override
    public String getPickerViewText() {
        return getName();
    }

    @Override
    public String getPickerViewCode() {
        return getCode();
    }

    @Override
    public List<IPickerViewData> getPickerChilch() {
        if (iPickerViewData == null) {
            if (children != null) {
                iPickerViewData = new ArrayList<IPickerViewData>() {{
                    addAll((Collection<? extends IPickerViewData>) children);
                }};
            } else {
                return new ArrayList<>();
            }
        }
        return iPickerViewData;
    }
}
