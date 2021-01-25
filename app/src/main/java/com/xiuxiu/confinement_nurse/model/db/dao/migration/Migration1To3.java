package com.xiuxiu.confinement_nurse.model.db.dao.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration1To3 extends Migration2To3 {

    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion   The end version of the database after this migration is applied.
     */
    public Migration1To3(int startVersion, int endVersion) {
        super(startVersion, endVersion);
    }
    public Migration1To3() {
        super(1,3);
    }
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        super.migrate(database);
        database.execSQL("ALTER TABLE user ADD COLUMN birthday TEXT");
    }
}
