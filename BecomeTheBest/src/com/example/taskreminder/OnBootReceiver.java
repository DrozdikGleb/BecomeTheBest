package com.example.taskreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.http.ParseException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiver
        extends BroadcastReceiver { 
@Override
  public void onReceive(Context context, Intent intent) { 
    ReminderManager reminderMgr = new ReminderManager(context); 
    ReminderDbAdapter dbHelper = new ReminderDbAdapter(context);
    dbHelper.open();
    Cursor cursor = dbHelper.fetchAllReminders(); 
    if(cursor != null) {
        cursor.moveToFirst(); 
    int rowIdColumnIndex = cursor.getColumnIndex(ReminderDbAdapter.KEY_ROWID);
    int dateTimeColumnIndex = cursor.getColumnIndex(ReminderDbAdapter.KEY_DATE_TIME);
    while(cursor.isAfterLast() == false) { 
      Long rowId = cursor.getLong(rowIdColumnIndex);
      String dateTime = cursor.getString(dateTimeColumnIndex);
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat format = new SimpleDateFormat(ReminderEditActivity.DATE_TIME_FORMAT);
      Log.d("OnBootReceiver",
    		  "Добавление сигнала при загрузке.");
    		  Log.d("OnBootReceiver",
    		  "Индекс столбца = "+rowIdColumnIndex);

  try {
    java.util.Date date = format.parse(dateTime); 
    cal.setTime(date); 
    reminderMgr.setReminder(rowId, cal); 
    } catch (ParseException e) {
      Log.e("OnBootReceiver", e.getMessage(), e); 
    } catch (java.text.ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    cursor.moveToNext(); 
  }
    cursor.close() ; 
  }
   dbHelper.close(); 
  }



}
