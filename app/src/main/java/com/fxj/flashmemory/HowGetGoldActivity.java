package com.fxj.flashmemory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class HowGetGoldActivity extends Activity {
	TextView tvTitle;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_how_get_gold);
	tvTitle = (TextView) findViewById(R.id.tv_head_title);
	tvTitle.setText("如何获取金币");
}
}
