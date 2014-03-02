package ca.cryptr.transit_watch.preferences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import net.sf.nextbus.publicxmlfeed.domain.Agency;
import net.sf.nextbus.publicxmlfeed.domain.Direction;
import net.sf.nextbus.publicxmlfeed.domain.Route;
import net.sf.nextbus.publicxmlfeed.domain.Stop;

import java.util.ArrayList;
import java.util.List;

import ca.cryptr.transit_watch.util.Util;

public class PreferencesDataSource {

    private static final String[] AGENCIES_COLUMNS = new String[] {
            PreferencesSQLiteHelper.AGENCIES.COLUMN_TAG,
            PreferencesSQLiteHelper.AGENCIES.COLUMN_TITLE,
            PreferencesSQLiteHelper.AGENCIES.COLUMN_SHORT_TITLE,
            PreferencesSQLiteHelper.AGENCIES.COLUMN_REGION_TITLE,
    };

    private static final String[] ROUTES_COLUMNS = new String[] {
            PreferencesSQLiteHelper.ROUTES.COLUMN_AGENCY,
            PreferencesSQLiteHelper.ROUTES.COLUMN_TAG,
            PreferencesSQLiteHelper.ROUTES.COLUMN_TITLE,
            PreferencesSQLiteHelper.ROUTES.COLUMN_SHORT_TITLE
    };

    private static final String[] DIRECTIONS_COLUMNS = new String[] {
            PreferencesSQLiteHelper.DIRECTIONS.COLUMN_ROUTE,
            PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TAG,
            PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TITLE,
            PreferencesSQLiteHelper.DIRECTIONS.COLUMN_NAME
    };

    private static final String[] STOPS_COLUMNS = new String[] {
            PreferencesSQLiteHelper.STOPS.COLUMN_DIRECTION,
            PreferencesSQLiteHelper.STOPS.COLUMN_TAG,
            PreferencesSQLiteHelper.STOPS.COLUMN_TITLE,
            PreferencesSQLiteHelper.STOPS.COLUMN_SHORT_TITLE
    };

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

    /**
     * Add the given stop to the database.
     *
     * @param stop a transit stop
     */
    public void saveStop(Direction direction, Stop stop) {
        mDatabase.beginTransaction();
        try {
            // Insert the agency
            Agency agency = stop.getAgency();
            ContentValues agencyValues = new ContentValues();
            agencyValues.put(PreferencesSQLiteHelper.AGENCIES.COLUMN_TAG, agency.getTag());
            agencyValues.put(PreferencesSQLiteHelper.AGENCIES.COLUMN_TITLE, agency.getTitle());
            agencyValues.put(PreferencesSQLiteHelper.AGENCIES.COLUMN_SHORT_TITLE, agency.getShortTitle());
            agencyValues.put(PreferencesSQLiteHelper.AGENCIES.COLUMN_REGION_TITLE, agency.getRegionTitle());
            mDatabase.insertWithOnConflict(PreferencesSQLiteHelper.AGENCIES.TABLE, null, agencyValues, SQLiteDatabase.CONFLICT_IGNORE);

            // Insert the route
            ContentValues routeValues = new ContentValues();
            routeValues.put(PreferencesSQLiteHelper.ROUTES.COLUMN_AGENCY, agency.getTag());
            routeValues.put(PreferencesSQLiteHelper.ROUTES.COLUMN_TAG, direction.getRoute().getTag());
            routeValues.put(PreferencesSQLiteHelper.ROUTES.COLUMN_TITLE, direction.getRoute().getTitle());
            routeValues.put(PreferencesSQLiteHelper.ROUTES.COLUMN_SHORT_TITLE, direction.getRoute().getShortTitle());
            mDatabase.insertWithOnConflict(PreferencesSQLiteHelper.ROUTES.TABLE, null, routeValues, SQLiteDatabase.CONFLICT_IGNORE);

            // Insert the direction
            ContentValues directionValues = new ContentValues();
            directionValues.put(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_ROUTE, direction.getRoute().getTag());
            directionValues.put(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TAG, direction.getTag());
            directionValues.put(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TITLE, direction.getTitle());
            directionValues.put(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_NAME, direction.getName());
            mDatabase.insertWithOnConflict(PreferencesSQLiteHelper.DIRECTIONS.TABLE, null, directionValues, SQLiteDatabase.CONFLICT_IGNORE);

            // Insert the stop
            ContentValues stopValues = new ContentValues();
            stopValues.put(PreferencesSQLiteHelper.STOPS.COLUMN_DIRECTION, direction.getTag());
            stopValues.put(PreferencesSQLiteHelper.STOPS.COLUMN_TAG, stop.getTag());
            stopValues.put(PreferencesSQLiteHelper.STOPS.COLUMN_TITLE, stop.getTitle());
            stopValues.put(PreferencesSQLiteHelper.STOPS.COLUMN_SHORT_TITLE, stop.getShortTitle());
            mDatabase.insertWithOnConflict(PreferencesSQLiteHelper.STOPS.TABLE, null, stopValues, SQLiteDatabase.CONFLICT_IGNORE);

            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    /**
     * Delete the given stop from the database, if it exists
     *
     * @param stop a transit stop
     */
    public void deleteStop(Stop stop) {
        mDatabase.beginTransaction();
        try {
            // Delete the stop
            mDatabase.delete(PreferencesSQLiteHelper.STOPS.TABLE,
                             PreferencesSQLiteHelper.STOPS.COLUMN_TAG + " = ?", new String[] { stop.getTag() });

            // Delete the direction if it's no longer referenced
            mDatabase.delete(PreferencesSQLiteHelper.DIRECTIONS.TABLE,
                             PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TAG + " NOT IN (SELECT " +
                             PreferencesSQLiteHelper.STOPS.COLUMN_DIRECTION+ " FROM " +
                             PreferencesSQLiteHelper.STOPS.TABLE + ")", null);

            // Delete the route if it's no longer referenced
            mDatabase.delete(PreferencesSQLiteHelper.ROUTES.TABLE,
                             PreferencesSQLiteHelper.ROUTES.COLUMN_TAG + " NOT IN (SELECT " +
                             PreferencesSQLiteHelper.DIRECTIONS.COLUMN_ROUTE + " FROM " +
                             PreferencesSQLiteHelper.DIRECTIONS.TABLE + ")", null);

            // Delete the agency if it's no longer referenced
            mDatabase.delete(PreferencesSQLiteHelper.AGENCIES.TABLE,
                             PreferencesSQLiteHelper.AGENCIES.COLUMN_TAG + " NOT IN (SELECT " +
                             PreferencesSQLiteHelper.ROUTES.COLUMN_AGENCY + " FROM " +
                             PreferencesSQLiteHelper.ROUTES.TABLE + ")", null);

            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    /**
     * Retrieve all ca.cryptr.transit_watch.stops from the database.
     *
     * @return all ca.cryptr.transit_watch.stops from the database
     */
    public List<Direction> getStops() {
        List<Direction> directions = new ArrayList<Direction>();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(PreferencesSQLiteHelper.AGENCIES.TABLE + ", " +
                               PreferencesSQLiteHelper.ROUTES.TABLE + ", " +
                               PreferencesSQLiteHelper.DIRECTIONS.TABLE + ", " +
                               PreferencesSQLiteHelper.STOPS.TABLE);
        queryBuilder.appendWhere(PreferencesSQLiteHelper.STOPS.COLUMN_DIRECTION + " = " +
                                 PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TAG);
        queryBuilder.appendWhere(" AND ");
        queryBuilder.appendWhere(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_ROUTE+ " = " +
                                 PreferencesSQLiteHelper.ROUTES.COLUMN_TAG);
        queryBuilder.appendWhere(" AND ");
        queryBuilder.appendWhere(PreferencesSQLiteHelper.ROUTES.COLUMN_AGENCY + " = " +
                                 PreferencesSQLiteHelper.AGENCIES.COLUMN_TAG);
        Cursor cursor = queryBuilder.query(mDatabase,
                                           Util.concatAll(AGENCIES_COLUMNS, ROUTES_COLUMNS, DIRECTIONS_COLUMNS, STOPS_COLUMNS),
                                           null, null, null, null,
                                           PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TAG);

        // Keep references to the column positions of each property
        AgencyCursorColumns agencyCursorColumns = AgencyCursorColumns.fromCursor(cursor);
        RouteCursorColumns routeCursorColumns = RouteCursorColumns.fromCursor(cursor);
        DirectionCursorColumns directionCursorColumns = DirectionCursorColumns.fromCursor(cursor);
        StopCursorColumns stopCursorColumns = StopCursorColumns.fromCursor(cursor);


        List<Stop> directionStops = new ArrayList<Stop>();
        String directionTag = null;
        Agency agency = null;
        Route route = null;
        Direction direction = null;

        // Iterate over the cursor to build the list of ca.cryptr.transit_watch.stops
        //noinspection ConstantConditions
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if (directionTag == null || !directionTag.equals(cursor.getString(directionCursorColumns.getTagColumn()))) {
                // Direction has changed; append the direction to the result set
                if (!directionStops.isEmpty()) {
                    cursor.moveToPrevious();
                    directions.add(directionFromCursor(cursor, directionCursorColumns, route, directionStops));
                    cursor.moveToNext();
                }

                // Save the agency, route, direction tag for the next group of stops
                agency = agencyFromCursor(cursor, agencyCursorColumns);
                route = routeFromCursor(cursor, routeCursorColumns, agency);
                directionTag = cursor.getString(directionCursorColumns.getTagColumn());
            }

            // Add the current stop to the current direction
            directionStops.add(stopFromCursor(cursor, stopCursorColumns, agency));

            cursor.moveToNext();
        }
        cursor.close();

        return directions;
    }

    private Agency agencyFromCursor(Cursor cursor, AgencyCursorColumns agencyCursorColumns) {
        return new Agency(cursor.getString(agencyCursorColumns.getTagColumn()),
                          cursor.getString(agencyCursorColumns.getTitleColumn()),
                          cursor.getString(agencyCursorColumns.getShortTitleColumn()),
                          cursor.getString(agencyCursorColumns.getRegionTitleColumn()), null);
    }

    private Route routeFromCursor(Cursor cursor, RouteCursorColumns routeCursorColumns, Agency agency) {
        return new Route(agency,
                         cursor.getString(routeCursorColumns.getTagColumn()),
                         cursor.getString(routeCursorColumns.getTitleColumn()),
                         cursor.getString(routeCursorColumns.getShortTitleColumn()), null);
    }

    private Direction directionFromCursor(Cursor cursor, DirectionCursorColumns directionCursorColumns, Route route, List<Stop> stops) {
        return new Direction(route,
                             cursor.getString(directionCursorColumns.getTagColumn()),
                             cursor.getString(directionCursorColumns.getTitleColumn()),
                             cursor.getString(directionCursorColumns.getNameColumn()),
                             stops, null);
    }

    private Stop stopFromCursor(Cursor cursor, StopCursorColumns stopCursorColumns, Agency agency) {
        return new Stop(agency,
                        cursor.getString(stopCursorColumns.getTagColumn()),
                        cursor.getString(stopCursorColumns.getTitleColumn()),
                        cursor.getString(stopCursorColumns.getShortTitleColumn()), null, null, null);
    }

    /**
     * Specifies which columns in a cursor correspond to each property of an {@link net.sf.nextbus.publicxmlfeed.domain.Agency}
     */
    private static class AgencyCursorColumns {
        private final int tagColumn;
        private final int titleColumn;
        private final int shortTitleColumn;
        private final int regionTitleColumn;

        private AgencyCursorColumns(int tagColumn, int titleColumn, int shortTitleColumn, int regionTitleColumn) {
            this.tagColumn = tagColumn;
            this.titleColumn = titleColumn;
            this.shortTitleColumn = shortTitleColumn;
            this.regionTitleColumn = regionTitleColumn;
        }

        public static AgencyCursorColumns fromCursor(Cursor cursor) {
            return new AgencyCursorColumns(cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.AGENCIES.COLUMN_TAG),
                                           cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.AGENCIES.COLUMN_TITLE),
                                           cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.AGENCIES.COLUMN_SHORT_TITLE),
                                           cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.AGENCIES.COLUMN_REGION_TITLE));
        }

        public int getTagColumn() {
            return tagColumn;
        }

        public int getTitleColumn() {
            return titleColumn;
        }

        public int getShortTitleColumn() {
            return shortTitleColumn;
        }

        public int getRegionTitleColumn() {
            return regionTitleColumn;
        }

    }

    /**
     * Specifies which columns in a cursor correspond to each property of an {@link net.sf.nextbus.publicxmlfeed.domain.Agency}
     */
    private static class RouteCursorColumns {
        private final int tagColumn;
        private final int titleColumn;
        private final int shortTitleColumn;

        private RouteCursorColumns(int tagColumn, int titleColumn, int shortTitleColumn) {
            this.tagColumn = tagColumn;
            this.titleColumn = titleColumn;
            this.shortTitleColumn = shortTitleColumn;
        }

        public static RouteCursorColumns fromCursor(Cursor cursor) {
            return new RouteCursorColumns(cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.ROUTES.COLUMN_TAG),
                                           cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.ROUTES.COLUMN_TITLE),
                                           cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.ROUTES.COLUMN_SHORT_TITLE));
        }

        public int getTagColumn() {
            return tagColumn;
        }

        public int getTitleColumn() {
            return titleColumn;
        }

        public int getShortTitleColumn() {
            return shortTitleColumn;
        }

    }

    /**
     * Specifies which columns in a cursor correspond to each property of an {@link net.sf.nextbus.publicxmlfeed.domain.Agency}
     */
    private static class DirectionCursorColumns {
        private final int tagColumn;
        private final int titleColumn;
        private final int nameColumn;

        private DirectionCursorColumns(int tagColumn, int titleColumn, int nameColumn) {
            this.tagColumn = tagColumn;
            this.titleColumn = titleColumn;
            this.nameColumn = nameColumn;
        }

        public static DirectionCursorColumns fromCursor(Cursor cursor) {
            return new DirectionCursorColumns(cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TAG),
                                              cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_TITLE),
                                              cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.DIRECTIONS.COLUMN_NAME));
        }

        public int getTagColumn() {
            return tagColumn;
        }

        public int getTitleColumn() {
            return titleColumn;
        }

        public int getNameColumn() {
            return nameColumn;
        }

    }

    /**
     * Specifies which columns in a cursor correspond to each property of an {@link net.sf.nextbus.publicxmlfeed.domain.Stop}
     */
    private static class StopCursorColumns {
        private final int tagColumn;
        private final int titleColumn;
        private final int shortTitleColumn;

        private StopCursorColumns(int tagColumn, int titleColumn, int shortTitleColumn) {
            this.tagColumn = tagColumn;
            this.titleColumn = titleColumn;
            this.shortTitleColumn = shortTitleColumn;
        }

        public static StopCursorColumns fromCursor(Cursor cursor) {
            return new StopCursorColumns(cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.STOPS.COLUMN_TAG),
                                         cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.STOPS.COLUMN_TITLE),
                                         cursor.getColumnIndexOrThrow(PreferencesSQLiteHelper.STOPS.COLUMN_SHORT_TITLE));
        }

        public int getTagColumn() {
            return tagColumn;
        }

        public int getTitleColumn() {
            return titleColumn;
        }

        public int getShortTitleColumn() {
            return shortTitleColumn;
        }
    }

}
