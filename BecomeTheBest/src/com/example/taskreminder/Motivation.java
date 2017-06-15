package com.example.taskreminder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Motivation extends Activity {

	ImageView im;
	int curIndex;
	int imageResources[] = { R.drawable.p1, R.drawable.p2, R.drawable.p3,
			R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7,
			R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12 ,R.drawable.p13 ,R.drawable.p14};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.motivation);

		im = (ImageView) findViewById(R.id.imageView1);

	}

	public void onClickImage(View v) {

		if (curIndex == imageResources.length - 1) {
			curIndex = 0;
			im.setImageResource(imageResources[curIndex]);
		} else {
			curIndex++;
			im.setImageResource(imageResources[curIndex]);
		}
	}

}
