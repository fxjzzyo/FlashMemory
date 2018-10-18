package com.fxj.flashmemory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsListener;

import com.fxj.flashmemory.data.Global;
import com.fxj.flashmemory.util.AnimatinUtil;
import com.fxj.flashmemory.util.CreateAlertDialog;

public class MainActivity extends Activity implements OnClickListener
		 {
	private Button btnPractice, btnTest, btnRecord,btnAdventure;
	private TextView tvGold, tvTitle, tvMore;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private String displayPointsText;
	final Handler mHandler = new Handler();
	private boolean flag;
	private boolean isFirstIn = true;//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		AppConnect.getInstance(this);//
		AppConnect.getInstance(this).setWeixinAppId("wx0d0e56e8aa8c77e2", this);
		initGold();
		
		initView();
		initEvent();

	}

	private void initGold() {
		pref = getSharedPreferences("record", MODE_PRIVATE);
		isFirstIn = pref.getBoolean("isFirst", true);
		int goldLast = pref.getInt("gold", 50);
		Global.gold = goldLast;
	}

	@Override
	protected void onResume() {
		super.onResume();
//		setGlod();

	}

/*	private void setGlod() {
		pref = getSharedPreferences("record", MODE_PRIVATE);
		int goldLast =pref.getInt("gold", Global.gold);
		int result = Global.gold - goldLast;
		if (result < 0)
		{
			int res = 0 - result;
			AppConnect.getInstance(this).spendPoints(res, this);
		} else if (result > 0) {//
			AppConnect.getInstance(this).awardPoints(result, this);
		}

	}*/

	private void initEvent() {
		tvTitle.setText("记忆闪现");
		tvTitle.setTextSize(20);

		btnPractice.setOnClickListener(this);
		btnTest.setOnClickListener(this);
		btnRecord.setOnClickListener(this);
		tvMore.setOnClickListener(this);
		btnAdventure.setOnClickListener(this);
		setSpeed();
		getScreenSize();

	}

	private void setSpeed() {
		pref = getSharedPreferences("record", MODE_PRIVATE);
		int speed = pref.getInt("speed", 2000);
		Global.SPEED = speed;
	}

	private void getScreenSize() {
		WindowManager wm = this.getWindowManager();

		Global.screen_width = wm.getDefaultDisplay().getWidth();
		Global.screen_height = wm.getDefaultDisplay().getHeight();

	}

	private void initView() {
		btnPractice = (Button) findViewById(R.id.btn_practice);
		btnTest = (Button) findViewById(R.id.btn_test);
		btnAdventure = (Button) findViewById(R.id.btn_adventure);
		btnRecord = (Button) findViewById(R.id.btn_record);
		tvGold = (TextView) findViewById(R.id.tv_gold);
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		tvMore = (TextView) findViewById(R.id.tv_more);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_practice:
			Intent intent1 = new Intent(MainActivity.this,
					PracticeActivity.class);
			intent1.putExtra("model", "练习模式");
			startActivity(intent1);
			break;
		case R.id.btn_test:
			Intent intent2 = new Intent(MainActivity.this, TestActivity.class);
			intent2.putExtra("model", "测试模式");
			startActivity(intent2);
			break;
		case R.id.btn_record:
			Intent intent3 = new Intent(MainActivity.this, RecordActivity.class);
			intent3.putExtra("model", "最高纪录");
			startActivity(intent3);
			break;
		case R.id.btn_adventure:
//			AppConnect.getInstance(this).showOffers(this);
			Intent intent4 = new Intent(MainActivity.this, AdvertureActivity.class);
			startActivity(intent4);
			break;
		case R.id.tv_more:
//			AppConnect.getInstance(this).showOffers(this);
			break;

		default:
			break;
		}

	}
	private boolean alertCostGoldDialog(final int cost) {

		AlertDialog.Builder builder = new Builder(this);
		View view = getLayoutInflater().inflate(R.layout.alert_win_dialog,
				(ViewGroup) findViewById(R.layout.alert_win_dialog));
		final TextView tvWin = (TextView) view.findViewById(R.id.tv_win);
		builder.setView(view);
		builder.setTitle("ϵͳ��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Global.gold += cost;
				if (Global.gold < 0) {
					CreateAlertDialog.goldInsifficent(MainActivity.this);
					Global.gold -= cost;//��һָ�
					return;
				}
				flag = true;
				if (flag) {

					flag = false;
				}
				AnimatinUtil.setUndateDataAnimatin(Global.gold, tvGold, cost
						+ "");
				
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				flag = false;
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setLayout(Global.screen_width * 2 / 3,
				Global.screen_height / 3);
		return flag;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			QuitPopAd.getInstance().show(this);
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
