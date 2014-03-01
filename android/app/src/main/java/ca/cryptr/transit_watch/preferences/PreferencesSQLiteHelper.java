package ca.cryptr.transit_watch.preferences;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PreferencesSQLiteHelper extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "stops.db";

    public static final class AGENCIES {
        public static final String TABLE = "agencies";

        public static final String COLUMN_AUTO_ID = "_ID";
        public static final String COLUMN_TAG = "agency_tag";
        public static final String COLUMN_TITLE = "agency_title";
        public static final String COLUMN_SHORT_TITLE = "agency_short_title";
        public static final String COLUMN_REGION_TITLE = "agency_region_title";
    }

    public static final class ROUTES {
        public static final String TABLE = "routes";

        public static final String COLUMN_AUTO_ID = "_ID";
        public static final String COLUMN_AGENCY = "route_agency";
        public static final String COLUMN_TAG = "route_tag";
        public static final String COLUMN_TITLE = "route_title";
        public static final String COLUMN_SHORT_TITLE = "route_short_title";
    }

    public static final class STOPS {
        public static final String TABLE = "stops";

        public static final String COLUMN_AUTO_ID = "_ID";
        public static final String COLUMN_ROUTE = "stop_route";
        public static final String COLUMN_TAG = "stop_tag";
        public static final String COLUMN_TITLE = "stop_title";
        public static final String COLUMN_SHORT_TITLE = "stop_short_title";
    }

    // CREATE TABLE statements

    private static final String CREATE_TABLE_AGENCIES =
            "CREATE TABLE " + AGENCIES.TABLE + " (" +
            AGENCIES.COLUMN_TAG + " TEXT PRIMARY KEY, " +
            AGENCIES.COLUMN_TITLE + " TEXT NOT NULL, " +
            AGENCIES.COLUMN_SHORT_TITLE + " TEXT, " +
            AGENCIES.COLUMN_REGION_TITLE + " TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_ROUTES =
            "CREATE TABLE " + ROUTES.TABLE + " (" +
            ROUTES.COLUMN_AGENCY + " INTEGER NOT NULL REFERENCES " + AGENCIES.TABLE + "(" + AGENCIES.COLUMN_AUTO_ID + ") ON DELETE CASCADE, " +
            ROUTES.COLUMN_TAG + " TEXT PRIMARY KEY, " +
            ROUTES.COLUMN_TITLE + " TEXT NOT NULL, " +
            ROUTES.COLUMN_SHORT_TITLE + " TEXT, " +
            ");";

    private static final String CREATE_TABLE_STOPS =
            "CREATE TABLE " + STOPS.TABLE + " (" +
            STOPS.COLUMN_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            STOPS.COLUMN_ROUTE + " TEXT NOT NULL REFERENCES " + ROUTES.TABLE + "(" + ROUTES.COLUMN_AUTO_ID + ") ON DELETE CASCADE" +
            STOPS.COLUMN_TAG + " TEXT NOT NULL, " +
            STOPS.COLUMN_TITLE + " TEXT NOT NULL, " +
            STOPS.COLUMN_SHORT_TITLE + " TEXT, " +
            ");";

    public PreferencesSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AGENCIES);
        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_STOPS);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables
        empty(db);

        // Create new tables
        onCreate(db);
    }

    /**
     * Drops all tables in this database
     */
    public void empty(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + AGENCIES.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ROUTES.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STOPS.TABLE);
    }

}
