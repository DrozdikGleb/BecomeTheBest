package com.example.taskreminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.ListActivity;
import android.content.Context;


public class ReminderListActivity extends ListActivity {
    private static final int ACTIVITY_EDIT=1;
    private static final int CM_DELETE_ID = 0;
    private static final int DONE = 1;
    
    private ReminderDbAdapter mDbHelper;
    SimpleCursorAdapter reminders;
    SimpleCursorAdapter reminder;
	TextView text1;
	TextView txt;
	public static int stat;
	
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_list);
		
		txt = (TextView)findViewById(R.id.textView1);
		text1 = (TextView)findViewById(R.id.text1);
		
		
		
		
		mDbHelper = new ReminderDbAdapter(this);
		mDbHelper.open();
		fillData();
		registerForContextMenu(getListView());
	}
	
	

	private void fillData() {
		Cursor reminderCursor = 
				mDbHelper.fetchAllReminders();
		startManagingCursor(reminderCursor);

		String[] from = new
				String[] {ReminderDbAdapter.KEY_TITLE,ReminderDbAdapter.KEY_DATE_TIME};
		

	
		int[] to = new int[] {R.id.text1,R.id.text2};
	
		MyCursorAdapter Adapter = new MyCursorAdapter(
			    this, R.layout.reminder_row, 
			    reminderCursor, 
			    from, 
			    to
			    );
		
	

		
	
	setListAdapter(Adapter);
	
    }
	 private class MyCursorAdapter extends SimpleCursorAdapter{
		 
		  public MyCursorAdapter(Context context, int layout, Cursor c,
		    String[] from, int[] to) {
		   super(context, layout, c, from, to);
		  }  
		 
		  @Override 
		  public void bindView(View view, Context context, Cursor cursor) {  
		 
		  
		     super.bindView(view, context, cursor);
		 
		   
		  TextView tv = (TextView) view.findViewById(R.id.text1);
		
			   String importm = cursor.getString(cursor.getColumnIndex(ReminderDbAdapter.KEY_IMP));
			if (importm.contentEquals("Срочная задача")) {
		         tv.setTextColor(-65536);
				
		    	} else if (importm.contentEquals("Обычная задача")) {
		    		tv.setTextColor(-256);
		    	
		    	} else if (importm.contentEquals("Необязательная задача")) {
		    		tv.setTextColor(-16711936);
		    	
		    	}
			  
		
		   
		  
		  }  
		 
		 
		 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.list_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_insert) {
			createReminder();
			return true;
		}
		if (item.getItemId() == R.id.action_motivitaion) {
			startActivity( new Intent(this,Motivation.class));
			return true;
			
		}
		if (item.getItemId() == R.id.statistics) {
			startActivity( new Intent(this,StatsManager.class));
			return true;
			
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private static final int ACTIVITY_CREATE=0;
	private void createReminder() {
		Intent i = new Intent(this, ReminderEditActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, ReminderEditActivity.class);
	i.putExtra(ReminderDbAdapter.KEY_ROWID, id);
	startActivityForResult(i, ACTIVITY_EDIT);
	}
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0,CM_DELETE_ID, 0, R.string.delete_record);
		menu.add(0,DONE, 0, R.string.task_done);
	}

	@Override
	public boolean onContextItemSelected (MenuItem item) {
		if (item.getItemId() == CM_DELETE_ID) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteReminder(info.id);
			fillData();
			
			return true;
		}
		if (item.getItemId() == DONE) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteReminder(info.id);
			stat = stat + 1;
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent); fillData();
	}
	
	
	
}
