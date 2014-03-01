package ca.cryptr.transit_watch.preferences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PreferencesDataSource {

    private PreferencesSQLiteHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public PreferencesDataSource(Context context) {
        mDbHelper = new PreferencesSQLiteHelper(context);
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }



}
