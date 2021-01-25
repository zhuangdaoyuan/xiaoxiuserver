package com.xiuxiu.confinement_nurse.ui.area;


import com.rxjava.rxlife.RxLife;
import com.xiuxiu.confinement_nurse.model.Configuration;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.area.vm.CityVm;
import com.xiuxiu.confinement_nurse.ui.area.vm.LocationVm;
import com.xiuxiu.confinement_nurse.ui.base.mvp.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rxhttp.wrapper.param.RxHttp;

public class AreaPresenter extends BasePresenter<AreaContract.IView> implements AreaContract.IPresenter {

    public AreaPresenter(AreaContract.IView viewer) {
        super(viewer);
    }

    @Override
    public void requestLocationData() {
        LocationVm locations = ModelManager.getInstance().getCacheInterface().getLocations();
        if (locations == null) {
            requestNetLocations()
                    .as(RxLife.as(getViewer()))
                    .subscribe(new Consumer<LocationVm>() {
                        @Override
                        public void accept(LocationVm locationVm) throws Exception {
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    });
        }
    }

    @Override
    public void requestLocations() {
        Observable.concat(requestLocalLocations(), requestNetLocations())
                .firstElement()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<LocationVm>() {
                    @Override
                    public void accept(LocationVm locationVm) throws Exception {
                        getViewer().onRequestLocations(locationVm);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private Observable<LocationVm> requestLocalLocations() {
       return Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, ObservableSource<LocationVm>>() {
                    @Override
                    public ObservableSource<LocationVm> apply(String s) throws Exception {
                        LocationVm locations = ModelManager.getInstance().getCacheInterface().getLocations();
                        if (locations == null) {
                            return Observable.create(new ObservableOnSubscribe<LocationVm>() {
                                @Override
                                public void subscribe(ObservableEmitter<LocationVm> emitter) throws Exception {
                                    emitter.onComplete();
                                }
                            });
                        } else {
                            return Observable.just(locations);
                        }
                    }
                });

    }

    /**
     * 获取城市地区
     * @return
     */
    private Observable<LocationVm> requestNetLocations() {
        return RxHttp.get(Configuration.Location.KEY_LOCATION)
                .asXXResponse(LocationVm.class)
                .doOnNext(new Consumer<LocationVm>() {
                    @Override
                    public void accept(LocationVm locationVm) throws Exception {
                        Schedulers.newThread().createWorker().schedule(() -> ModelManager.getInstance().getCacheInterface().saveLocations(locationVm));
                    }
                });
    }



    @Override
    public void requestCitys() {
        Observable.concat(requestLocalCitys(), requestNetCity())
                .firstElement()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(getViewer()))
                .subscribe(new Consumer<CityVm>() {
                    @Override
                    public void accept(CityVm locationVm) throws Exception {
                        getViewer().onRequestCitys(locationVm);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private Observable<CityVm> requestLocalCitys() {
        return Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, ObservableSource<CityVm>>() {
                    @Override
                    public ObservableSource<CityVm> apply(String s) throws Exception {
                        CityVm locations = ModelManager.getInstance().getCacheInterface().getNetCity();
                        if (locations == null) {
                            return Observable.create(new ObservableOnSubscribe<CityVm>() {
                                @Override
                                public void subscribe(ObservableEmitter<CityVm> emitter) throws Exception {
                                    emitter.onComplete();
                                }
                            });
                        } else {
                            return Observable.just(locations);
                        }
                    }
                });

    }

    private Observable<CityVm> requestNetCity() {
        return RxHttp.get(Configuration.Location.KEY_CITY)
                .asXXResponse(CityVm.class)
                .doOnNext(new Consumer<CityVm>() {
                    @Override
                    public void accept(CityVm locationVm) throws Exception {
                        Schedulers.newThread().createWorker().schedule(() -> ModelManager.getInstance().getCacheInterface().saveNetCity(locationVm));
                    }
                });
    }
}
