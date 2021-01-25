package com.xiuxiu.confinement_nurse.model.db.dao;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.xiuxiu.confinement_nurse.help.UserHelper;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.model.db.dao.migration.Migration1To2;
import com.xiuxiu.confinement_nurse.model.db.dao.migration.Migration1To3;
import com.xiuxiu.confinement_nurse.model.db.dao.migration.Migration2To3;
import com.xiuxiu.confinement_nurse.model.db.dao.user.DatabaseInterface;
import com.xiuxiu.confinement_nurse.model.db.dao.user.UserDaoInterface;
import com.xiuxiu.confinement_nurse.model.db.dao.user.UserDatabases;
import com.xiuxiu.confinement_nurse.model.db.pojo.UserBean;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc2;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置为 2个 数据库，
 * 一个和用户有关的数据
 * 一个是播放列表
 */
public final class DaoInterfaceImpl implements DaoInterface {
    private static final String KEY_PLAY_LIST_NAME = "play_name.db";
    private XFunc2<String, Boolean> mCallBack;

    private final Map<String, DatabaseInterface> mDatabaseArrayMap = new HashMap<>();


    @NonNull
    private DatabaseInterface mCurrentDatabase;

    @Override
    public void init(Context context) {
        String id = ModelManager.getInstance().getCacheInterface().getUserId();
        if (!TextUtils.isEmpty(id)) {
            initDb(context, id, id + ".db");
        } else {
            initDb(context, String.valueOf(UserHelper.KEY_VISITOR_ID), UserHelper.DB_NAME_BY_VISITOR);
        }
    }

    private void initDb(Context context, String id, String dbname) {
        switchDb(context, id, dbname);
    }

    @Override
    public void switchDb(Context context, String id, String dbname) {
        DatabaseInterface roomDatabase = mDatabaseArrayMap.get(dbname);
        if (null == roomDatabase) {
            roomDatabase = createDataBase(context, dbname, UserDatabases.class);
            mDatabaseArrayMap.put(dbname, roomDatabase);
        }
        switchDao(id, roomDatabase);
    }

    /**
     * @param context
     * @param dbname
     * @return
     */
    private <T extends RoomDatabase> T createDataBase(Context context, String dbname, Class<T> klass) {
        return Room.databaseBuilder(context, klass, dbname)
                .addMigrations(new Migration1To2())
                .addMigrations(new Migration1To3())
                .addMigrations(new Migration2To3())
                .allowMainThreadQueries().build();
    }

    private <T extends RoomDatabase> T createPlayDataBase(Context context, String dbname, Class<T> klass) {
        return Room.databaseBuilder(context, klass, dbname)
//                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries().build();
    }

    /**
     * 数据库版本升级在  addMigrations
     * 数据库版本 1->2 SongInfoBean 增加 song_type
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE song ADD COLUMN song_type TEXT");
        }
    };

    private void switchDao(String id, DatabaseInterface roomDatabase) {
        mCurrentDatabase = roomDatabase;
        if (mCallBack != null) {
            String na = mCurrentDatabase.database().getOpenHelper().getDatabaseName();
            mCallBack.call(id, TextUtils.equals(na, UserHelper.DB_NAME_BY_VISITOR));
        }
    }


    @NonNull
    @Override
    public UserDaoInterface<UserBean> userDaoInterface() {
        return mCurrentDatabase.uerInterface();
    }


    @Override
    public void switchDaoCallBack(XFunc2<String, Boolean> xFunc0) {
        this.mCallBack = xFunc0;
    }
}
