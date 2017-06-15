package com.example.taskreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver{
	public void onReceive(Context context, Intent intent) {
		long id =
				intent.getExtras().getLong(ReminderDbAdapter.KEY_ROWID);
		WakeReminderIntentService.acquireStaticLock(context);
		Intent i = new Intent(context, ReminderService.class);
		i.putExtra(ReminderDbAdapter.KEY_ROWID, id);
		context.startService(i);
	}

}
