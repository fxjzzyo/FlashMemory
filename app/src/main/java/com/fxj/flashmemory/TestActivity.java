package com.fxj.flashmemory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.waps.AppConnect;
import cn.waps.AppListener;

import com.fxj.flashmemory.data.Global;
import com.fxj.flashmemory.util.AnimatinUtil;
import com.fxj.flashmemory.util.CreateAlertDialog;

public class TestActivity extends Activity implements OnClickListener {
	private Button btnPrimary, btnMiddle, btnHigh, btnUltimate, btnCustom;
	private TextView tvTitle;
	private TextView tvGold, tvMore;
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_practice);
		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		tvGold.setText(Global.gold + "");
	}

	private void initEvent() {
		// 获取传过来的 模式model
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
		btnCustom = (Button) findViewById(R.id.btn_custom);
		btnUltimate = (Button) findViewById(R.id.btn_ultimate);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_primary:
				Intent intent2 = new Intent(TestActivity.this,
						GameTestActivity.class);
				intent2.putExtra("number", Global.PRE_CARD);
				intent2.putExtra("title", "初级测试");
				intent2.putExtra("timer", Global.PRE_TIME);// 30秒
				startActivity(intent2);
				break;
			case R.id.btn_middle:
				Intent intent3 = new Intent(TestActivity.this,
						GameTestActivity.class);
				intent3.putExtra("number", Global.MIDDLE_CARD);
				intent3.putExtra("title", "中级测试");
				intent3.putExtra("timer", Global.MIDDLE_TIME);// 50秒
				startActivity(intent3);
				break;
			case R.id.btn_high:
				Intent intent4 = new Intent(TestActivity.this,
						GameTestActivity.class);
				intent4.putExtra("number", Global.HIGH_CARD);
				intent4.putExtra("title", "高级测试");
				intent4.putExtra("timer", Global.HIGH_TIME);// 70秒
				startActivity(intent4);
				break;
			case R.id.btn_ultimate:
				Intent intent5 = new Intent(TestActivity.this,
						GameTestActivity.class);
				intent5.putExtra("number", Global.ULTIMATE_CARD);
				intent5.putExtra("title", "终级测试");
				intent5.putExtra("timer", Global.ULTIMATE_TIME);// 90秒
				startActivity(intent5);
				break;
			case R.id.btn_custom:
				CreateAlertDialog.customModelDialog(TestActivity.this, "test");

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
