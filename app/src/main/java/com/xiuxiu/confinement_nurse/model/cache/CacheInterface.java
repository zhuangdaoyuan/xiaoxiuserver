package com.xiuxiu.confinement_nurse.model.cache;


import android.text.TextUtils;

import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.ui.area.vm.CityVm;
import com.xiuxiu.confinement_nurse.ui.area.vm.LocationVm;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.ui.news.vm.TokenVm;
import com.xiuxiu.confinement_nurse.utlis.Utils;

public class CacheInterface {
    CacheAccessorImpl cacheAccessor;

    public CacheInterface() {
        cacheAccessor = new CacheAccessorImpl(Utils.getApp());
    }

    public void saveToken(String token) {
        cacheAccessor.putString("token", token);
    }

    public String getToken() {
        return cacheAccessor.getString("token", "");
    }

    public String getUserId() {
        return cacheAccessor.getString("user_id", String.valueOf(UserHelper.KEY_VISITOR_ID));
    }

    public void saveUserId(String id) {
        cacheAccessor.putString("user_id", id);
    }

    public void saveLocations(LocationVm locationVm) {
        String s = GonAccessorImpl.getOriginalGson().toJson(locationVm);
        cacheAccessor.putString("location", s);
    }

    public LocationVm getLocations() {
        String location = cacheAccessor.getString("location", "");
        if (TextUtils.isEmpty(location)) {
            return null;
        }
        try {
            return GonAccessorImpl.getOriginalGson().fromJson(location, LocationVm.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveNetCity(CityVm locationVm) {
        String s = GonAccessorImpl.getOriginalGson().toJson(locationVm);
        cacheAccessor.putString("citys", s);
    }

    public CityVm getNetCity() {
        String location = cacheAccessor.getString("citys", "");
        if (TextUtils.isEmpty(location)) {
            return null;
        }
        try {
            return GonAccessorImpl.getOriginalGson().fromJson(location, CityVm.class);
        } catch (Exception e) {
            return null;
        }
    }

    public int getDeliveryType() {
        return cacheAccessor.getInt("DeliveryType", -1);
    }

    public int getPriceRangeType() {
        return cacheAccessor.getInt("PriceRangeType", -1);

    }

    public int getDayRangeType() {
        return cacheAccessor.getInt("DayRangeType", -1);
    }


    public void saveDeliveryType(int mDayRangeType) {
        cacheAccessor.putInt("DeliveryType", mDayRangeType);
    }

    public void savePriceRangeType(int mPriceRangeType) {
        cacheAccessor.putInt("PriceRangeType", mPriceRangeType);
    }

    public void saveDayRangeType(int mDayRangeType) {
        cacheAccessor.putInt("DayRangeType", mDayRangeType);
    }

    public void saveLocationCity(City mCityLiveData) {
        String s = GonAccessorImpl.getOriginalGson().toJson(mCityLiveData);
        cacheAccessor.putString("saveLocationCity", s);
    }

    public City getLocationCity() {
        String saveLocationCity = cacheAccessor.getString("saveLocationCity");
        if (TextUtils.isEmpty(saveLocationCity)) {
            return null;
        }
        return GonAccessorImpl.getOriginalGson().fromJson(saveLocationCity, City.class);
    }

    public boolean getIsGuide() {
        return cacheAccessor.getBoolean("is_guide", false);
    }

    public void saveGuide() {
        cacheAccessor.putBoolean("is_guide", true);
    }

    public boolean showProtocol() {
        return cacheAccessor.getBoolean("is_show_protocol", true);
    }

    public void saveProtocol() {
        cacheAccessor.getBoolean("is_show_protocol", false);
    }

    public void saveRongyunToken(String token) {
        cacheAccessor.putString("rongyun_customer_service", token);
    }

    public String getRongyunToken() {
        return cacheAccessor.getString("rongyun_customer_service");
    }

    public void saveUserPhone(String phone) {
        cacheAccessor.putString("phone", phone);
    }

    public String getUserPhone() {
        return cacheAccessor.getString("phone");
    }

    public void saveUserType(int i) {
        cacheAccessor.putInt("user_type", i);
    }

    public int getUserType() {
        return cacheAccessor.getInt("user_type", 2);
    }
}
