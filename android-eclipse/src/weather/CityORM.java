package weather;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityORM {

	private static final String TAG = "CityORM";

	private static final String TABLE_NAME = "city";

	private static final String COMMA_SEP = ", ";

	private static final String COLUMN_CODE_TYPE = "TEXT";
	private static final String COLUMN_CODE = "code";

	private static final String COLUMN_NAMEN_TYPE = "TEXT";
	private static final String COLUMN_NAMEEN = "nameEn";

	private static final String COLUMN_NAMEFR_TYPE = "TEXT";
	private static final String COLUMN_NAMEFR = "nameFr";

	private static final String COLUMN_PROVINCE_TYPE = "TEXT";
	private static final String COLUMN_PROVINCE = "province";

	public static final String SQL_CREATE_TABLE =
			"CREATE TABLE " + TABLE_NAME + " (" +
					COLUMN_CODE + " " + COLUMN_CODE_TYPE + COMMA_SEP +
					COLUMN_NAMEEN  + " " + COLUMN_NAMEN_TYPE + COMMA_SEP +
					COLUMN_NAMEFR + " " + COLUMN_NAMEFR_TYPE + COMMA_SEP +
					COLUMN_PROVINCE + " " + COLUMN_PROVINCE_TYPE +
					")";

	public static final String SQL_DROP_TABLE =
			"DROP TABLE IF EXISTS " + TABLE_NAME;

	public static void insertPost(Context context, City city) {
		DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
		SQLiteDatabase database = databaseWrapper.getWritableDatabase();

		ContentValues values = postToContentValues(city);
		long postId = database.insert(CityORM.TABLE_NAME, "null", values);
		Log.i(TAG, "Inserted new Post with ID: " + postId);

		database.close();
	}

	/**
	 * Packs a City object into a ContentValues map for use with SQL inserts.
	 */
	private static ContentValues postToContentValues(City city) {
		ContentValues values = new ContentValues();
		values.put(CityORM.COLUMN_CODE, city.getCode());
		values.put(CityORM.COLUMN_NAMEEN, city.getNameEn());
		values.put(CityORM.COLUMN_NAMEFR, city.getNameFr());
		values.put(CityORM.COLUMN_PROVINCE, city.getProvince());

		return values;
	}

	public static List<City> getPosts(Context context) {
		DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
		SQLiteDatabase database = databaseWrapper.getReadableDatabase();

		Cursor cursor = database.rawQuery("SELECT * FROM " + CityORM.TABLE_NAME, null);

		Log.i(TAG, "Loaded " + cursor.getCount() + " Cities...");
		List<City> cityList = new ArrayList<City>();

		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				City post = cursorToCity(cursor);
				cityList.add(post);
				cursor.moveToNext();
			}
			Log.i(TAG, "Posts loaded successfully.");
		}

		database.close();

		return cityList;
	}

	/**
	 * Populates a City object with data from a Cursor
	 * @param cursor
	 * @return
	 */
	private static City cursorToCity(Cursor cursor) {
		City city = new City();
		city.setCode(cursor.getString(cursor.getColumnIndex(COLUMN_CODE)));
		city.setNameEn(cursor.getString(cursor.getColumnIndex(COLUMN_NAMEEN)));
		city.setNameFr(cursor.getString(cursor.getColumnIndex(COLUMN_NAMEFR)));
		city.setProvince(cursor.getString(cursor.getColumnIndex(COLUMN_PROVINCE)));

		return city;
	}

}
