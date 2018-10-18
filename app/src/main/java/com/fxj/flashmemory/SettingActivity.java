package com.fxj.flashmemory;

import com.fxj.flashmemory.data.Global;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {
	private TextView tvTitle;
	private RadioButton rb500, rb1000, rb2000, rb3000;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		initView();
		initEvent();
	}

	private void initEvent() {
		tvTitle.setText("设置");
		pref = getSharedPreferences("record", MODE_PRIVATE);
		rb500.setOnClickListener(this);
		rb1000.setOnClickListener(this);
		rb2000.setOnClickListener(this);
		rb3000.setOnClickListener(this);
		switch (Global.SPEED) {
		case 500:
			rb500.setChecked(true);
			break;
	case 1000:
		rb1000.setChecked(true);
			break;
	case 2000:
		rb2000.setChecked(true);
		break;
	case 3000:
		rb3000.setChecked(true);
		break;
		default:
			break;
		}
	}

	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		rb500 = (RadioButton) findViewById(R.id.rb_500);
		rb1000 = (RadioButton) findViewById(R.id.rb_1000);
		rb2000 = (RadioButton) findViewById(R.id.rb_2000);
		rb3000 = (RadioButton) findViewById(R.id.rb_3000);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.rb_500:
			Global.SPEED = 500;
			break;
		case R.id.rb_1000:
			Global.SPEED = 1000;
			break;
		case R.id.rb_2000:
			Global.SPEED = 2000;
			break;
		case R.id.rb_3000:
			Global.SPEED = 3000;
			break;
		default:
			break;
		}
		editor = pref.edit();
		editor.putInt("speed", Global.SPEED);
		editor.commit();

	}
}
