package com.fxj.flashmemory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.fxj.flashmemory.data.Global;
import com.fxj.flashmemory.util.AnimatinUtil;
import com.fxj.flashmemory.util.CreateAlertDialog;

public class AdvertureActivity extends Activity implements OnClickListener {
	private static int TIME = 7;// 播放7张图片
	private static int SPEED = 3000;// 播放速度
	protected static final int UPDATE = 0;// 每隔一秒发送的消息
	protected static final int SEE_ANSWER_MESSAGE = 2;// 查看当前卡片的消息
	private static int SECOND = 0;// 计时器
	private  int CLICK = 0;// 第几次点击按钮

	private Button btnStart, btnSeeAnswer;
	private TextView tvGold, tvTitle, tvScore;
	private EditText etAnswer;
	private ImageSwitcher imageSwitcher;
	private Timer mTimer = new Timer();
	// 图片资源
	private int[] images = new int[] { R.drawable.black_0, R.drawable.black_1,
			R.drawable.black_2, R.drawable.black_3, R.drawable.black_4,
			R.drawable.black_5, R.drawable.black_6, R.drawable.black_7,
			R.drawable.black_8, R.drawable.black_9 };
	// 当前图片id
	private int currentImageId = 0;
	private int remembers[];//

	private Button btnNumbers[] = new Button[10];
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private boolean isFinished;//标记本关是否完成
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_adventure);
		initView();
		initEvent();
	}
	public  void levelAlertDialog(){
		AlertDialog.Builder builder = new Builder(this);
		View view  = getLayoutInflater().inflate(R.layout.alert_win_dialog	, (ViewGroup)findViewById(R.layout.alert_win_dialog));
		final TextView tvWin = (TextView) view.findViewById(R.id.tv_win);
		tvWin.setText("接着上次的关卡继续游戏？");
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pref = getSharedPreferences("record", MODE_PRIVATE);
				int lastLevel = pref.getInt("level", 1);//获取上次玩到的关卡
				Global.LEVEL = lastLevel;
				int lastScore = pref.getInt("adventure", 0);
				Global.adventureScore = lastScore;
				tvTitle.setText("第" + Global.LEVEL + "关");
				tvScore.setText(""+Global.adventureScore);
			}
		});
		builder.setNegativeButton("重新开始", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Global.LEVEL = 1;
				tvTitle.setText("第" + Global.LEVEL + "关");
				tvScore.setText(""+Global.adventureScore);
			}
		});
		Dialog dialog = builder.create();dialog.show();
		dialog.getWindow().setLayout(Global.screen_width*2/3, Global.screen_height/3);
	}
	private void initEvent() {
		pref = getSharedPreferences("record", MODE_PRIVATE);
		int lastLevel = pref.getInt("level", 1);//获取上次玩到的关卡
		if(lastLevel != 1)
		{
			levelAlertDialog();
		}else{
			tvTitle.setText("第" + Global.LEVEL + "关");
		}
		Log.i("tag", ""+Global.LEVEL);

		tvGold.setText(Global.gold + "");

		enableBottomBtn(false);//
		btnSeeAnswer.setEnabled(false);
		for (int i = 0; i < btnNumbers.length; i++) {
			btnNumbers[i].setOnClickListener(this);
		}
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGame();

			}
		});
		btnSeeAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Global.gold += Integer.parseInt("-2");// 消耗2个金币
				if (Global.gold <= 0) {
					Global.gold = Global.gold+2;
					CreateAlertDialog.goldInsifficent(AdvertureActivity.this);
					return;
				}
				AnimatinUtil.setUndateDataAnimatin(Global.gold, tvGold,
						"-2");*/
				mHandler.sendEmptyMessage(SEE_ANSWER_MESSAGE);

			}
		});
		// 为imageSwitcher设置工厂，每设置一张图片需要一个ImageView来承载这个图片
		imageSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				// 生产出的是ImageView
				return new ImageView(AdvertureActivity.this);
			}
		});

		// 设置图片切换的动画
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				AdvertureActivity.this, android.R.anim.slide_in_left));//
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				AdvertureActivity.this, android.R.anim.slide_out_right));// 从又滑出
		imageSwitcher.setImageResource(R.drawable.empty);// 一开始就设置一个空白的
	}

	private void initView() {
		// 实例化控件
		btnStart = (Button) findViewById(R.id.btn_start);
		btnSeeAnswer = (Button) findViewById(R.id.btn_see_answer);

		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		tvGold = (TextView) findViewById(R.id.tv_gold);
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		etAnswer = (EditText) findViewById(R.id.et_answer);
		tvScore = (TextView) findViewById(R.id.tv_score);

		btnNumbers[0] = (Button) findViewById(R.id.btn_0);
		btnNumbers[1] = (Button) findViewById(R.id.btn_1);
		btnNumbers[2] = (Button) findViewById(R.id.btn_2);
		btnNumbers[3] = (Button) findViewById(R.id.btn_3);
		btnNumbers[4] = (Button) findViewById(R.id.btn_4);
		btnNumbers[5] = (Button) findViewById(R.id.btn_5);
		btnNumbers[6] = (Button) findViewById(R.id.btn_6);
		btnNumbers[7] = (Button) findViewById(R.id.btn_7);
		btnNumbers[8] = (Button) findViewById(R.id.btn_8);
		btnNumbers[9] = (Button) findViewById(R.id.btn_9);

	}

	@Override
	public void onClick(View v) {

		String num = ((Button) v).getText().toString();

		if (Integer.parseInt(num) == remembers[CLICK]) {
			CLICK++;
			// 将正确答案设置到edittext
			etAnswer.append(num + " ");
			Global.adventureScore += 5;
			tvScore.setText(""+Global.adventureScore);
			AnimatinUtil.setUndateDataAnimatin(Global.adventureScore, tvScore, "+5");
			if (CLICK == TIME) {
				isFinished = true;
				// 弹出对话框，过关s
				CreateAlertDialog.winDialog(this, true, tvTitle.getText().toString().trim());
				saveRecord();
				tvGold.setText(Global.gold+"");
				Log.i("tag", "----->"+tvTitle.getText().toString());
				// 游戏结束
				CLICK = 0;
				btnStart.setEnabled(true);
				btnSeeAnswer.setEnabled(false);
				enableBottomBtn(false);
				Global.LEVEL++;// 关卡加一
				if(Global.LEVEL == 11)
				{
					//通关了
					Global.LEVEL = 1;
				}
				tvTitle.setText("第" + Global.LEVEL + "关");
				return;

			}
		} else {

		}

	}

	/**
	 * 底部数字按钮是否可用
	 *
	 * @param able
	 */
	private void enableBottomBtn(boolean able) {
		if (able) {
			for (int i = 0; i < btnNumbers.length; i++) {
				btnNumbers[i].setEnabled(true);
			}
		} else {
			for (int i = 0; i < btnNumbers.length; i++) {
				btnNumbers[i].setEnabled(false);
			}
		}
	}

	// 开始游戏
	private void startGame() {
		isFinished=false;
		switch (Global.LEVEL) {
			case 1:
				TIME = 7;
				SPEED = 1000;
				break;
			case 2:
				TIME = 9;SPEED = 900;
				break;
			case 3:
				TIME = 11;SPEED = 800;
				break;
			case 4:
				TIME = 13;SPEED = 700;
				break;
			case 5:
				TIME = 15;SPEED = 1000;
				break;
			case 6:
				TIME = 16;SPEED = 1000;
				break;
			case 7:
				TIME = 17;SPEED = 1000;
				break;
			case 8:
				TIME = 18;SPEED = 1000;
				break;
			case 9:
				TIME = 19;SPEED = 1000;
				break;
			case 10:
				TIME = 20;SPEED = 1000;
				break;

			default:
				break;
		}
		remembers = new int[TIME];
		etAnswer.setText("");
		btnStart.setEnabled(false);
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 每隔一秒发送一条消息
				mHandler.sendEmptyMessage(UPDATE);
			}
		}, 0, SPEED);

	}

	private Handler mHandler = new Handler() {
		Random random = new Random();

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == UPDATE) {
				SECOND++;
				if (SECOND > TIME)// //数字播放完毕
				{
					// 数字播放完毕
					mTimer.cancel();
					mTimer = null;
					SECOND = 0;
					// 数字按钮可按
					enableBottomBtn(true);
					btnSeeAnswer.setEnabled(true);
					imageSwitcher.setImageResource(R.drawable.empty);
					return;
				}
				currentImageId = random.nextInt(10);
				remembers[SECOND - 1] = currentImageId;// 记录播放的图片id
				imageSwitcher.setImageResource(images[currentImageId]);

			} else if (msg.what == SEE_ANSWER_MESSAGE) {

				imageSwitcher.setImageResource(images[remembers[CLICK]]);

				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						imageSwitcher.setImageResource(R.drawable.empty);

					}
				}, 1000);
			}
		}

	};

	@Override
	protected void onDestroy() {
		CLICK = 0;
		//记录当前关卡
		editor = pref.edit();
		editor.putInt("level", Global.LEVEL);
		editor.commit();
		saveRecord();
		//分数重置
		tvScore.setText("0");
		Global.adventureScore = 0;

		if (mTimer != null) {
			mTimer.cancel();
		}
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		//判断本关是否完成，如果未完成则扣金币
		/*if(!isFinished)
		{
			Global.gold-=5;
			if(Global.gold<=0)
			{
				//弹出金币不足对话框
				CreateAlertDialog.goldInsifficent(AdvertureActivity.this);
				Global.gold+=5;
			}else{
				tvGold.setText(Global.gold+"");
				Toast.makeText(this, "本关未完成，扣除金币5个", 500).show();
			}
		}*/
		this.finish();
	}
	private void saveRecord() {
		int maxAdventureScore = pref.getInt("maxAdventure", 0);
		if (maxAdventureScore < Global.adventureScore) {
			editor = pref.edit();
			editor.putInt("maxAdventure", Global.adventureScore);
			editor.commit();
			//弹出最高纪录对话框
			CreateAlertDialog.newRecordDialog(AdvertureActivity.this, Global.adventureScore);
		}
		//存储本轮分数，以便恢复
		editor = pref.edit();
		editor.putInt("adventure", Global.adventureScore);
		editor.commit();
	}

}
