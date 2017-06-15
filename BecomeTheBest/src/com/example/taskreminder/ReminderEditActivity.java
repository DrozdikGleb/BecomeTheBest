package com.example.taskreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.ParseException;

import com.example.taskreminder.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ReminderEditActivity extends Activity implements OnItemSelectedListener {
    String FullDate;
	Spinner spinlvl;
    String Tasklvl;
	private Button mDateButton;
	private Button mTimeButton;
	private static final int DATE_PICKER_DIALOG = 0;
	private static final int TIME_PICKER_DIALOG = 1;
	private Calendar mCalendar;
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static final String TIME_FORMAT = "HH:mm";
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm ";
	private EditText mTitleText;
	public String importm;
	
	private final String[] lvltask = { "Срочная задача", "Обычная задача",
	"Необязательная задача" };
	
	private Button mConfirmButton;
	private Long mRowId;
	public ReminderDbAdapter mDbHelper;
	public String titl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new ReminderDbAdapter(this); 
		setContentView(R.layout.reminder_edit);
		mDateButton = (Button) findViewById(R.id.reminder_date);
		mTimeButton = (Button) findViewById(R.id.reminder_time);
		mConfirmButton = (Button) findViewById(R.id.confirm);
		TextView txtv1 = (TextView) findViewById(R.id.txtv1);
		TextView txtv2 = (TextView) findViewById(R.id.txtv2);
		
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/PALA.TTF");
	    mDateButton.setTypeface(myTypeface);
	    mTimeButton.setTypeface(myTypeface);
	    mConfirmButton.setTypeface(myTypeface);
	    txtv1.setTypeface(myTypeface);
	    txtv2.setTypeface(myTypeface);
		spinlvl = (Spinner) findViewById(R.id.spinner1);
		spinlvl.setOnItemSelectedListener(this);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lvltask);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinlvl.setAdapter(arrayAdapter);
		mTitleText = (EditText) findViewById(R.id.title);
	
		mCalendar = Calendar.getInstance();
		mRowId = savedInstanceState != null
				? savedInstanceState.getLong(ReminderDbAdapter.KEY_ROWID): null;
		registerButtonListenersAndSetDefaultText();
	}
	
	private void setRowIdFromIntent() {
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null
					? extras.getLong(ReminderDbAdapter.KEY_ROWID): null;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mDbHelper.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mDbHelper.open();
		setRowIdFromIntent();
		populateFields();
	}
	
	private void registerButtonListenersAndSetDefaultText() {
	
		mDateButton.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showDialog(DATE_PICKER_DIALOG);
					}
				});
		
		mTimeButton.setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showDialog(TIME_PICKER_DIALOG);
					}
				});
		
		mConfirmButton.setOnClickListener(
				new View.OnClickListener() {
			public void onClick(View view) {
				if (FullDate != null) { 
				saveState();
				setResult(RESULT_OK);
				Toast.makeText(ReminderEditActivity.this,
				getString(R.string.task_saved_message),
				Toast.LENGTH_SHORT).show();
				finish();
				}
				else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Выберите срок задачи", Toast.LENGTH_SHORT);

					toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
					LinearLayout linearLayout = (LinearLayout) toast.getView();
					linearLayout.setBackgroundColor(-16711681);
					TextView mtoast = (TextView) linearLayout.getChildAt(0);
					mtoast.setTextSize(30);
					mtoast.setTextColor(-16776961);

					toast.show();
				}
			}
				});
	}
	
	private void populateFields() {
		if (mRowId != null) {
			Cursor reminder = mDbHelper.fetchReminder(mRowId);
			startManagingCursor(reminder);
			mTitleText.setText(reminder.getString(reminder.getColumnIndexOrThrow(ReminderDbAdapter.KEY_TITLE)));
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			Date date = null;
			try {
				String dateString = reminder.getString(reminder.getColumnIndexOrThrow(ReminderDbAdapter.KEY_DATE_TIME));
				try {
					date = dateTimeFormat.parse(dateString);
				} catch (java.text.ParseException e) {
					
					e.printStackTrace();
				}
				mCalendar.setTime(date);
			} catch (ParseException e) {
				Log.e("ReminderEditActivity", e.getMessage(), e);
			}
		} 
		
		updateDateButtonText();
		updateTimeButtonText();
	}
	
	private void saveState() {
		titl = mTitleText.getText().toString();
		String importm = Tasklvl;
		Log.i("title -", titl);
		Log.i("importm -", importm);
		
		SimpleDateFormat dateTimeFormat = new
				SimpleDateFormat(DATE_TIME_FORMAT);
		String reminderDateTime = 
				dateTimeFormat.format(mCalendar.getTimeInMillis());
		
		if (mRowId == null) {
			long id = mDbHelper.createReminder(titl, reminderDateTime, importm);
			if (id>0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateReminder(mRowId, titl, reminderDateTime, importm);
		}
	new ReminderManager(this).setReminder(mRowId, mCalendar);			
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DATE_PICKER_DIALOG:
			return showDatePicker();
		case TIME_PICKER_DIALOG:
			return showTimePicker(); 
		}
		return super.onCreateDialog(id);
	}
	
	private DatePickerDialog showDatePicker() {
		DatePickerDialog datePicker =
				new DatePickerDialog(ReminderEditActivity.this,
				new DatePickerDialog.OnDateSetListener() {
					
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
		FullDate = year + "/"+ monthOfYear+ "/"+ dayOfMonth;
	mCalendar.set(Calendar.YEAR, year);					
	mCalendar.set(Calendar.MONTH, monthOfYear);
	mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	updateDateButtonText();
	
	
	               }
	}, mCalendar.get(Calendar.YEAR),	
	   mCalendar.get(Calendar.MONTH),
	   mCalendar.get(Calendar.DAY_OF_MONTH));
		
	return datePicker;
	}
	
	private void updateDateButtonText() {
		SimpleDateFormat dateFormat =
				new SimpleDateFormat(DATE_FORMAT);
		String dateForButton = 
				dateFormat.format(mCalendar.getTime());
		mDateButton.setText(dateForButton);
	}
	
	private TimePickerDialog showTimePicker() {
		TimePickerDialog timePicker =
				new TimePickerDialog(ReminderEditActivity.this,
				new TimePickerDialog.OnTimeSetListener() {
					
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);					
	mCalendar.set(Calendar.MINUTE, minute);
	updateTimeButtonText();
	               }
	}, mCalendar.get(Calendar.HOUR_OF_DAY),	
	   mCalendar.get(Calendar.MINUTE), true);
	return timePicker;
    }
	
	private void updateTimeButtonText() {
		SimpleDateFormat timeFormat =
				new SimpleDateFormat(TIME_FORMAT);
		String timeForButton = 
				timeFormat.format(mCalendar.getTime());
		mTimeButton.setText(timeForButton);
}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Tasklvl = lvltask[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}
	
	
}









