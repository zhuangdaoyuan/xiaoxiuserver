package com.xiuxiu.confinement_nurse.ui.citypicker.db;

import android.content.Context;

import com.google.gson.Gson;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.xiuxiu.confinement_nurse.ui.citypicker.model.City;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.xiuxiu.confinement_nurse.ui.citypicker.db.DBConfig.DB_NAME_V1;
import static com.xiuxiu.confinement_nurse.ui.citypicker.db.DBConfig.LATEST_DB_NAME;


/**
 * Author Bro0cL on 2016/1/26.
 */
public class DBManager {
    private static final int BUFFER_SIZE = 1024;
    private static final int BIG_BITMAP_INDEX = 0;
    private String DB_PATH;
    private Context mContext;

    public DBManager(Context context) {
        this.mContext = context;
//        DB_PATH = File.separator + "data"
//                + Environment.getDataDirectory().getAbsolutePath() + File.separator
//                + context.getPackageName() + File.separator + "databases" + File.separator;
//        copyDBFile();
    }

    @Deprecated
    private void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //如果旧版数据库存在，则删除
        File dbV1 = new File(DB_PATH + DB_NAME_V1);
        if (dbV1.exists()) {
            dbV1.delete();
        }
        //创建新版本数据库
        File dbFile = new File(DB_PATH + LATEST_DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(LATEST_DB_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<City> getAllCities(String data) {
        ArrayList<City> jsonBean = parseData(data);//用Gson 转成实体

        ArrayList<City> options2Items = new ArrayList<>();
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份

            for (int c = 0; c < jsonBean.get(i).getChildren().size(); c++) {//遍历该省份的所有城市
                City cityName = jsonBean.get(i).getChildren().get(c);
                options2Items.add(cityName);//添加城市
            }
        }
        Collections.sort(options2Items, new CityComparator());
        return options2Items;
    }

    public List<City> getAllCities() {
        String JsonData = CityPickerPopup.readJson(mContext, "province.json");//获取assets目录下的json文件数据
        return getAllCities(JsonData);
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
//        List<City> result = new ArrayList<>();
//        City city;
//        while (cursor.moveToNext()) {
//            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
//            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
//            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
//            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
//            city = new City(name, province, pinyin, code);
//            result.add(city);
//        }
//        cursor.close();
//        db.close();
//        Collections.sort(result, new CityComparator());


//        return new ArrayList<>();
    }

    public List<City> searchCity(final String keyword) {
//        String sql = "select * from " + TABLE_NAME + " where "
//                + COLUMN_C_NAME + " like ? " + "or "
//                + COLUMN_C_PINYIN + " like ? ";
//        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
//        Cursor cursor = db.rawQuery(sql, new String[]{"%"+keyword+"%", keyword+"%"});
//
//        List<City> result = new ArrayList<>();
//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
//            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
//            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
//            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
//            City city = new City(name, province, pinyin, code);
//            result.add(city);
//        }
//        cursor.close();
//        db.close();
//        CityComparator comparator = new CityComparator();
//        Collections.sort(result, comparator);
        return new ArrayList<>();
    }

    /**
     * sort by a-z
     */
    public static  class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }

    public static ArrayList<City> parseData(String result) {//Gson 解析
        ArrayList<City> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                City entity = gson.fromJson(data.optJSONObject(i).toString(), City.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
