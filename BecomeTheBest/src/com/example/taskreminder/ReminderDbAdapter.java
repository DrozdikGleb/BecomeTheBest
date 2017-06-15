package com.example.taskreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ReminderDbAdapter { 

private static final String DATABASE_NAME = "data";
private static final String DATABASE_TABLE = "reminders";
private static final int DATABASE_VERSION = 1;
public static final String KEY_TITLE = "title";
public static final String KEY_DATE_TIME = "reminder_date_time";
public static final String KEY_ROWID = "_id";
public static final String KEY_IMP = "importance";

public DatabaseHelper mDbHelper;
private SQLiteDatabase mDb;

private static final String DATABASE_CREATE = "create table "
        + DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement,"
        + KEY_TITLE + " text not null, " + KEY_IMP + " text not null, " + KEY_DATE_TIME 
        + " text not null);";

private final Context mCtx;
public String titl;
public ReminderDbAdapter(Context ctx) {
	this.mCtx = ctx;
}

public ReminderDbAdapter open()
throws android.database.SQLException {
	mDbHelper = new DatabaseHelper(mCtx);
	mDb = mDbHelper.getWritableDatabase();
	return this;
}

public long createReminder(String title, String reminderDateTime, String importm) {
	ContentValues initialValues = new ContentValues();
	initialValues.put(KEY_TITLE, title);
	initialValues.put(KEY_DATE_TIME, reminderDateTime);
	initialValues.put(KEY_IMP, importm);
	
	return mDb.insert(DATABASE_TABLE, null, initialValues);
}

public boolean deleteReminder(long rowId) {
	return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
}

public Cursor fetchAllReminders() {
	Cursor mCursor= mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
			KEY_DATE_TIME, KEY_IMP}, null, null, null, null,KEY_DATE_TIME + " ASC",null); 
	if (mCursor != null) {
		mCursor.moveToFirst();
	}
		return mCursor;
	}

public Cursor fetchReminder(long rowId) throws SQLException {
	Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, 
	KEY_TITLE, KEY_DATE_TIME, KEY_IMP}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
if (mCursor != null) {
	mCursor.moveToFirst();
	titl = mCursor.getString(mCursor.getColumnIndex("title"));

 }
return mCursor;
}

public boolean updateReminder(long rowId, String title, String reminderDateTime, String importm) {
	ContentValues args = new ContentValues();
	args.put(KEY_TITLE, title);
	args.put(KEY_DATE_TIME, reminderDateTime);
	args.put(KEY_IMP, importm);
	
return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
}

public static class DatabaseHelper  extends SQLiteOpenHelper {
DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);

	
	
}

@Override
public void onCreate(SQLiteDatabase db) {
	db.execSQL(DATABASE_CREATE);	
    }

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

}

}

public void close() {
	mDbHelper.close();
}

}


