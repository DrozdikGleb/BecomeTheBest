package com.example.taskreminder;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StatsManager extends Activity {

public static int stat;
private ProgressBar lvl;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.stat);
TextView tx = (TextView) findViewById(R.id.txx);
TextView ed = (TextView)findViewById(R.id.ed);
 lvl = (ProgressBar)findViewById(R.id.anus);


Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/SEGOEPRB.TTF");
tx.setTypeface(myTypeface);
ed.setText(String.valueOf(ReminderListActivity.stat));
lvl.setProgress(ReminderListActivity.stat);
}



}
