package com.xiuxiu.confinement_nurse.model.db.dao.migration;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration2To3 extends Migration {

    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion   The end version of the database after this migration is applied.
     */
    public Migration2To3(int startVersion, int endVersion) {
        super(startVersion, endVersion);
    }
    public Migration2To3() {
        super(2, 3);
    }
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
//        database.execSQL("ALTER TABLE user ADD COLUMN birthday TEXT");

//        private String id;  agency_id
//        private String createTime;
//        private String updateTime;
//        private String title;
//        private String orgId;
//        private String orgMobile;
//        private String orgImg;
//        private String address;
//        private String contact;
//        private String mobile;
//        private String emails;
//        private String des;
//        private String tp;
//        private String foreImg;
//        private String tailImg;
//        private String state;
//        private String ty;
        database.execSQL("ALTER TABLE user ADD COLUMN agency_id TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN createTime TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN updateTime TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN title TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN orgId TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN orgMobile TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN orgImg TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN agency_address TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN contact TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN mobile TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN emails TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN des TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN tp TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN foreImg TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN tailImg TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN state TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN ty TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN userType TEXT");



        database.execSQL("ALTER TABLE user ADD COLUMN yuesao_lng TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN yuesao_lat TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN yuesao_orgId TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN matronId TEXT");
        database.execSQL("ALTER TABLE user ADD COLUMN yuesao_id TEXT");

    }
}
