package com.fxj.flashmemory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class RecordActivity extends Activity {
	private TextView tvTitle,tvPrimaryRecord, tvMiddleRecord, tvHighRecord, tvUltimate,tvCustom,tvAdventure;
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		initView();
		initEvent();
	}

	private void initEvent() {
		String title = getIntent().getStringExtra("model");
		tvTitle.setText(title);
		pref = getSharedPreferences("record", MODE_PRIVATE);
		int premaryScore =pref.getInt("primary", 0);
		int middleScore = pref.getInt("middle", 0);
		int highScore = pref.getInt("high", 0);
		int ultimateScore = pref.getInt("ultimate", 0);
		int customScore = pref.getInt("custom", 0);
		int adventureScore = pref.getInt("maxAdventure", 0);
		tvPrimaryRecord.setText(premaryScore+"分");
		tvMiddleRecord.setText(middleScore+"分");
		tvHighRecord.setText(highScore+"分");
		tvUltimate.setText(ultimateScore+"分");
		tvCustom.setText(customScore+"分");
		tvAdventure.setText(adventureScore+"分");
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		tvHighRecord = (TextView) findViewById(R.id.tv_high_score);
		tvMiddleRecord  = (TextView) findViewById(R.id.tv_middle_score);
		tvPrimaryRecord = (TextView) findViewById(R.id.tv_primary_score);
		tvUltimate = (TextView) findViewById(R.id.tv_ultimate_score);
		tvCustom = (TextView) findViewById(R.id.tv_custom_score);
		tvAdventure = (TextView) findViewById(R.id.tv_adventure_score);
	}
}
