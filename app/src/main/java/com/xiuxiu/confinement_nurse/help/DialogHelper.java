package com.xiuxiu.confinement_nurse.help;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.contrarywind.interfaces.IPickerViewData;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.InputConfirmPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.lxj.xpopupext.popup.TimePickerPopup;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.ui.MainActivity;
import com.xiuxiu.confinement_nurse.ui.area.vm.LocationVm;
import com.xiuxiu.confinement_nurse.ui.dialog.CalendarDialog;
import com.xiuxiu.confinement_nurse.ui.dialog.ConfirmDialog;
import com.xiuxiu.confinement_nurse.ui.dialog.ProtocolDialog;
import com.xiuxiu.confinement_nurse.ui.dialog.SuccessfulDeliveryDialog;
import com.xiuxiu.confinement_nurse.ui.schedule.vm.ScheduleVm;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc0;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1R;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc3;

import java.util.ArrayList;
import java.util.Date;

public final class DialogHelper {

    public static void showProtocolDialog(Context context, XFunc0 xFunc0) {
        ProtocolDialog confirmDialog = new ProtocolDialog(context);
        confirmDialog.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (xFunc0 != null) {
                    xFunc0.call();
                }
            }
        });
        BasePopupView show = new XPopup.Builder(context).asCustom(confirmDialog)
                .show();
    }

    /**
     * 用户个人还没有认证的提示
     */
    public static void authenticationRequired(Context context, XFunc0 xFunc0) {
        ConfirmDialog confirmDialog = new ConfirmDialog(context);
        confirmDialog.setTitleContent("您还未认证信息，请到\n" + "个人中心填写资料");
        confirmDialog.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (xFunc0 != null) {
                    xFunc0.call();
                }
            }
        });
        new XPopup.Builder(context).asCustom(confirmDialog)
                .show();
    }

    /**
     * 还有多少次投递机会
     *
     * @param context
     * @param num
     */
    public static void howManyDeliveryOpportunities(Context context, String num) {
        ConfirmDialog confirmDialog = new ConfirmDialog(context);
        confirmDialog.setNoCancel("好的");
        confirmDialog.setTitleContent("您本月只有" + num + "投递机会");
        confirmDialog.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {

            }
        });
        new XPopup.Builder(context).asCustom(confirmDialog)
                .show();
    }

    /**
     * 显示投递成功
     *
     * @param context
     */
    public static void showSuccessfulDeliveryDialog(Context context) {
        SuccessfulDeliveryDialog confirmDialog = new SuccessfulDeliveryDialog(context);
        new XPopup.Builder(context).asCustom(confirmDialog)
                .show();
    }

    public static void showCalendarDialog(Context context, ScheduleVm scheduleVm, CalendarDialog.ISelectCalendar iSelectCalendar) {
        CalendarDialog confirmDialog = new CalendarDialog(context,scheduleVm,iSelectCalendar);
        BasePopupView show = new XPopup.Builder(context).asCustom(confirmDialog)
                .show();
    }


    public static void showLocation(Context context, XFunc0 xFunc0) {
        ConfirmDialog confirmDialog = new ConfirmDialog(context);
        confirmDialog.setTitleContent("当前应用缺少必要权限。请点击设置权限-打开所需权限。");
        confirmDialog.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (xFunc0 != null) {
                    xFunc0.call();
                }
            }
        });
        new XPopup.Builder(context).asCustom(confirmDialog)
                .show();
    }

    public static void showLoadingDialog(Context context) {

    }


    public static InputConfirmPopupView showInputDialog(Context context, String title, XFunc1 xFunc0) {
        InputConfirmPopupView inputConfirmPopupView = new XPopup.Builder(context)
                .asInputConfirm(title, "请输入内容。",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
                                if (xFunc0 != null) {
                                    xFunc0.call(text);
                                }
                            }
                        });
        BasePopupView show = inputConfirmPopupView
                .show();
        return inputConfirmPopupView;
    }


    public static void showCalendar(Activity activity, XFunc1<Date> dateXFunc1) {
//        Calendar date = Calendar.getInstance();
//        date.set(2000, 5,1);
//        Calendar date2 = Calendar.getInstance();
//        date2.set(2020, 5,1);
        TimePickerPopup popup = new TimePickerPopup(activity)
//                        .setDefaultDate(date)  //设置默认选中日期
//                        .setYearRange(1990, 1999) //设置年份范围
//                        .setDateRang(date, date2) //设置日期范围
                .setTimePickerListener(new TimePickerListener() {
                    @Override
                    public void onTimeChanged(Date date) {
                        //时间改变
                    }

                    @Override
                    public void onTimeConfirm(Date date, View view) {
                        if (dateXFunc1 != null) {
                            dateXFunc1.call(date);
                        }
                    }
                });

        new XPopup.Builder(activity)
                .asCustom(popup)
                .show();
    }

    public static void showCityPickerPopup(Context a, XFunc3<IPickerViewData, IPickerViewData, IPickerViewData> xFunc3) {

        LocationVm locations = ModelManager.getInstance().getCacheInterface().getLocations();
        CityPickerPopup popup;
        if (locations != null) {
            popup = new CityPickerPopup(a, new ArrayList<IPickerViewData>() {{
                addAll(locations.getItems());
            }});
        } else {
            popup = new CityPickerPopup(a);
        }
        popup.setCityPickerListener(new CityPickerListener() {
            @Override
            public void onCityChange(IPickerViewData province, IPickerViewData city, IPickerViewData area) {

            }

            @Override
            public void onCityConfirm(IPickerViewData province, IPickerViewData city, IPickerViewData area, View v) {
                if (xFunc3 != null) {
                    xFunc3.call(province, city, area);
                }
            }
        });
        new XPopup.Builder(a)
                .asCustom(popup)
                .show();
    }
}
