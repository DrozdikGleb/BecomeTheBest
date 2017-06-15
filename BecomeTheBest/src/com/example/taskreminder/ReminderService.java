package com.example.taskreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;

public class ReminderService extends WakeReminderIntentService {
	// titl = myVolatileVar;
	
	
	private CharSequence titl = "Ты не сделал дело!";
	public ReminderService() {
		super("ReminderService");
		}

	@Override
	void doReminderWork(Intent intent) {
		Long rowId = intent.getExtras()
		.getLong(ReminderDbAdapter.KEY_ROWID); 
		
		NotificationManager mgr = (NotificationManager)
				getSystemService(NOTIFICATION_SERVICE); 
		
		Intent notificationIntent = new Intent(this,
				ReminderListActivity.class); 
				
		notificationIntent.putExtra(
				ReminderDbAdapter.KEY_ROWID, rowId); 
				
		PendingIntent pi = PendingIntent.getActivity(this, 0,
			notificationIntent, PendingIntent.FLAG_ONE_SHOT); 
				
		Notification note=new Notification(
				R.drawable.ic,
				titl,
				System.currentTimeMillis()); 
				
		note.setLatestEventInfo(this, getString(
				R.string.new_task_title), titl, pi);
				
		note.defaults |= Notification.DEFAULT_SOUND; 
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		  
		
	
		
		int id = (int)((long)rowId); 
		mgr.notify(id, note);
		}


}
