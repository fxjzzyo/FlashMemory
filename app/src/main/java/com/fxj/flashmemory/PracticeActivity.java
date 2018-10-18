package com.fxj.flashmemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fxj.flashmemory.data.Global;
import com.fxj.flashmemory.util.CreateAlertDialog;

import cn.waps.AppConnect;

public class PracticeActivity extends Activity implements OnClickListener {
	private Button btnPrimary, btnMiddle, btnHigh, btnUltimate, btnCustom;
	private TextView tvTitle;
	private TextView tvGold, tvMore;
	private boolean flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_practice);
		AppConnect.getInstance(this).initPopAd(this);

		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		tvGold.setText(Global.gold + "");
	}

	private void initEvent() {

		String title = getIntent().getStringExtra("model");
		tvTitle.setText(title);
		btnPrimary.setOnClickListener(this);
		btnMiddle.setOnClickListener(this);
		btnHigh.setOnClickListener(this);
		btnUltimate.setOnClickListener(this);
		btnCustom.setOnClickListener(this);
		tvMore.setOnClickListener(this);
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		tvGold = (TextView) findViewById(R.id.tv_gold);
		tvMore = (TextView) findViewById(R.id.tv_more);

		btnPrimary = (Button) findViewById(R.id.btn_primary);
		btnMiddle = (Button) findViewById(R.id.btn_middle);
		btnHigh = (Button) findViewById(R.id.btn_high);
		btnUltimate = (Button) findViewById(R.id.btn_ultimate);
		btnCustom = (Button) findViewById(R.id.btn_custom);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_primary:
			Intent intent2 = new Intent(PracticeActivity.this,
					GamePracticeActivity.class);
			intent2.putExtra("number", Global.PRE_CARD);
			intent2.putExtra("title", "初级练习");
			startActivity(intent2);
			break;
		case R.id.btn_middle:
			Intent intent3 = new Intent(PracticeActivity.this,
					GamePracticeActivity.class);
			intent3.putExtra("number", Global.MIDDLE_CARD);
			intent3.putExtra("title", "中级练习");
			startActivity(intent3);
			break;
		case R.id.btn_high:
			Intent intent4 = new Intent(PracticeActivity.this,
					GamePracticeActivity.class);
			intent4.putExtra("number", Global.HIGH_CARD);
			intent4.putExtra("title", "高级练习");
			startActivity(intent4);
			break;
		case R.id.btn_ultimate:
			Intent intent5 = new Intent(PracticeActivity.this,
					GamePracticeActivity.class);
			intent5.putExtra("number", Global.ULTIMATE_CARD);
			intent5.putExtra("title", "终级练习");
			startActivity(intent5);
			break;
		case R.id.btn_custom:
            CreateAlertDialog.customModelDialog(PracticeActivity.this, "practice");
			break;
		case R.id.tv_more:

			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
