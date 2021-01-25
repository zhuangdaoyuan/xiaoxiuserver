package com.xiuxiu.confinement_nurse.model.db.dao.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration1To2 extends Migration {

    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion   The end version of the database after this migration is applied.
     */
    public Migration1To2(int startVersion, int endVersion) {
        super(startVersion, endVersion);
    }
    public Migration1To2() {
        super(1, 2);
    }
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE user ADD COLUMN birthday TEXT");
    }
}
