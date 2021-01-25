package com.xiuxiu.confinement_nurse.ui.vm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.xiuxiu.confinement_nurse.App;
import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.citypicker.db.DBManager;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LocationVm extends AndroidViewModel {
    MutableLiveData<City> mCityLiveData;
    private City mLocationCity;
    private DBManager dbManager;
    private Disposable subscribe;

    public LocationVm(@NonNull Application application) {
        super(application);
        mCityLiveData = new MutableLiveData<>();
        dbManager = new DBManager(application);
    }

    public MutableLiveData<City> getCityLiveData() {
        return mCityLiveData;
    }

    public City getLocationCity() {
        return mLocationCity;
    }

    public void setLocationCity(City mLocationCity) {
        this.mLocationCity = mLocationCity;
    }

    public void findCity(String name, XFunc1<City> cityXFunc1) {
        LinkedHashMap<String, City> linkedHashMap = ((App) getApplication()).linkedHashMap;
        if (linkedHashMap.size() == 0) {
            subscribe = Observable.just("")
                    .subscribeOn(Schedulers.io())
                    .map(s -> {
                        com.xiuxiu.confinement_nurse.ui.area.vm.LocationVm locations = ModelManager.getInstance().getCacheInterface().getLocations();
                        if (locations != null) {
                            List<City> items = locations.getItems();
                            int size = items.size();
                            List<City> cities=new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                City city = items.get(i);
                                List<City> children = city.getChildren();
                                if (children!=null) {
                                    cities.addAll(children);
                                }
                            }
                            return cities;
                        }
                        List<City> allCities = dbManager.getAllCities();
                        return allCities;
                    })
                    .map(new Function<List<City>, City>() {
                        @Override
                        public City apply(List<City> cities) throws Exception {
                            City city2 = null;
                            for (City city : cities) {
                                linkedHashMap.put(city.getName(), city);
                                if (city2 == null && TextUtils.equals(name, city.getName())) {
                                    city2 = city;
                                }
                            }
                            if (city2 == null) {
                                city2 = new City(UserHelper.KEY_DEFAULT_CITY_NAME, UserHelper.KEY_DEFAULT_CITY_NAME, "", UserHelper.KEY_DEFAULT_CITY_CODE);
                            }
                            return city2;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<City>() {
                        @Override
                        public void accept(City city) throws Exception {
                            cityXFunc1.call(city);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            City city2 = new City(UserHelper.KEY_DEFAULT_CITY_NAME, UserHelper.KEY_DEFAULT_CITY_NAME, "", UserHelper.KEY_DEFAULT_CITY_CODE);
                            cityXFunc1.call(city2);
                        }
                    });
        } else {
            subscribe = Observable.just("")
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            City city = linkedHashMap.get(name);
                            if (city == null) {
                                city = new City(UserHelper.KEY_DEFAULT_CITY_NAME, UserHelper.KEY_DEFAULT_CITY_NAME, "", "110000");
                            }
                            cityXFunc1.call(city);
                        }
                    }).subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }

    public void setCityLiveData(City mCityLiveData) {
        if (mCityLiveData == null) {
            return;
        }
        ModelManager.getInstance().getCacheInterface().saveLocationCity(mCityLiveData);
        this.mCityLiveData.setValue(mCityLiveData);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (subscribe != null) {
            subscribe.dispose();
        }
    }
}
