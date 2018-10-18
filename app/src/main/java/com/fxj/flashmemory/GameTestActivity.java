package com.fxj.flashmemory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
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

public class GameTestActivity extends Activity implements OnClickListener {
	private static final int TEST_TIME_UPDATE = 1;// "游戏计时器更新"的消息
	private  int TIME = 10;// 播放10张图片
	private  int TIMES = 60;// 用时
	private int times;// 用时的备份
	protected static final int UPDATE = 0;// "每隔一秒发送"的消息
	protected static final int SEE_ANSWER_MESSAGE = 2;//查看当前卡片的消息
	private  int SECOND = 0;// 计时器
	private  int CLICK = 0;// 第几次点击按钮
	private String title;
	private Button btnStart, btnStartAnswer,btnSeeAnswer;
	private TextView  tvGold, tvScore, tvTitle, tvTimer;
	private EditText etAnswer;
	private ImageSwitcher imageSwitcher;
	private Timer mTimer;
	private Timer testTimer;// 用于测试的计时器
	// 图片资源
	private int[] images = new int[] { R.drawable.black_0,R.drawable.black_1, R.drawable.black_2,
			R.drawable.black_3, R.drawable.black_4,
			R.drawable.black_5, R.drawable.black_6,
			R.drawable.black_7, R.drawable.black_8, R.drawable.black_9 };
	// 当前图片id
	private int currentImageId = 0;
	private int remembers[];//记录答案的数组

	private Button btnNumbers[] = new Button[10];
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;

	//	private int winGold;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_test);
		initView();
		initEvent();
	}

	private void initEvent() {
		// 获得传递过来的number
		TIME = getIntent().getIntExtra("number", 7);
		remembers = new int[TIME];

		// 获得传递过来的timer
		TIMES = getIntent().getIntExtra("timer", 30);
		tvTimer.setText(TIMES + "");
		times = TIMES;
		// 获得传递过来的title
		title = getIntent().getStringExtra("title");
		tvTitle.setText(title+TIME+"个");
		tvGold.setText(Global.gold+"");
//		setWinGold();

		resetButton();
		for (int i = 0; i < btnNumbers.length; i++) {
			btnNumbers[i].setOnClickListener(this);
		}
		// 开始播放图片
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGame();

			}
		});
		// 开始作答
		btnStartAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAnswer();

			}
		});
		//查看当前卡片的答案
		btnSeeAnswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Global.gold+=Integer.parseInt(Global.TEST_SEE_NUM);//消耗金币
				if(Global.gold<=0)
				{
					Global.gold = Global.gold -Integer.parseInt(Global.TEST_SEE_NUM);
//					CreateAlertDialog.goldInsifficent(GameTestActivity.this);
//					return;
				}
				AnimatinUtil.setUndateDataAnimatin(Global.gold, tvGold, Global.TEST_SEE_NUM);
				mHandler.sendEmptyMessage(SEE_ANSWER_MESSAGE);

			}
		});
		// 为imageSwitcher设置工厂，每设置一张图片需要一个ImageView来承载这个图片
		imageSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				// 生产出的是ImageView
				return new ImageView(GameTestActivity.this);
			}
		});

		// 设置图片切换的动画
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				GameTestActivity.this, android.R.anim.slide_in_left));//
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				GameTestActivity.this, android.R.anim.slide_out_right));// 从又滑出
		imageSwitcher.setImageResource(R.drawable.empty);//一开始就设置一个空白的
	}

	/*private void setWinGold() {
		if(title.equals("初级测试"))
		{
			winGold=Global.PRIMARY_WIN;
		}else if(title.endsWith("中级测试"))
		{
			winGold=Global.MIDDLE_WIN;
		}else if(title.equals("高级测试"))
		{
			winGold=Global.HIGH_WIN;
		}else if(title.equals("终级测试"))
		{
			winGold=Global.ULTIMATE_WIN;

		}else if(title.equals("自定义测试"))
		{
			winGold=Global.CUSTOM_WIN;
		}

	}*/

	private void initView() {
		// 实例化控件
		btnStart = (Button) findViewById(R.id.btn_start);
		btnStartAnswer = (Button) findViewById(R.id.btn_start_answer);
		btnSeeAnswer =(Button) findViewById(R.id.btn_see_answer);

		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

		tvGold = (TextView) findViewById(R.id.tv_gold);
		tvScore = (TextView) findViewById(R.id.tv_score);
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
		tvTimer = (TextView) findViewById(R.id.tv_timer);
		etAnswer = (EditText) findViewById(R.id.et_answer);
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
		if (Global.gold <= 0) {
//			CreateAlertDialog.goldInsifficent(this);
//			return;
		}
		String num = ((Button) v).getText().toString();

		if (Integer.parseInt(num) == remembers[CLICK]) {
			CLICK++;
			//将正确答案设置到edittext
			etAnswer.append(num+" ");
			Global.gold += Integer.parseInt(Global.TEST_RIGHT_NUM);

			AnimatinUtil.setUndateDataAnimatin(Global.gold,tvGold,"+"+Global.TEST_RIGHT_NUM);
			updateScore(title, true);
			if (CLICK == TIME) {
				gameOver(true);
				return;
			}

		} else {
			Toast.makeText(this, "错误！", 200).show();
			Global.gold += Integer.parseInt(Global.TEST_WRNOG_NUM);
			if(Global.gold<=0)
			{
				Global.gold = 0;
			}
			AnimatinUtil.setUndateDataAnimatin(Global.gold,tvGold,Global.TEST_WRNOG_NUM);
			updateScore(title, false);//错了别扣分了
		}

	}

	private void updateScore(String level, boolean res) {
		int y =0;
//		int y2 = 0;答错不扣分了
		if(level.equals("初级测试"))
		{
			y = (TIMES-times)*5+TIME*CLICK;//答对得分，分数等于 时间*5+卡片个数*点击的次数
			if (res) {
				Global.primaryScore += y;
				AnimatinUtil.setUndateDataAnimatin(Global.primaryScore,tvScore,"+"+y);
			} else {
				//Global.primaryScore -= y2;
				//AnimatinUtil.setUndateDataAnimatin(Global.primaryScore,tvScore,"-"+y2);
			}
		}else if(level.equals("中级测试"))
		{
			y = (TIMES-times)*3+TIME*CLICK;//答对得分，分数等于 时间*5+卡片个数*点击的次数
//			y2 = times*2+TIME*(TIMES-CLICK);//打错扣分，分数等于 时间*2+卡片个数*点击的次数
			if(res)
			{
				Global.middleScore += y;
				AnimatinUtil.setUndateDataAnimatin(Global.middleScore,tvScore,"+"+y);
			}else
			{
				//Global.middleScore -= y2;
				//AnimatinUtil.setUndateDataAnimatin(Global.middleScore,tvScore,"-"+2);
			}
		}else if(level.equals("高级测试"))
		{
			y = (TIMES-times)*2+TIME*CLICK;//答对得分，分数等于 时间*5+卡片个数*点击的次数
//			y2 = times+TIME*(TIMES-CLICK);//打错扣分，分数等于 时间*5+卡片个数*点击的次数
			if(res)
			{
				Global.highScore += y;
				AnimatinUtil.setUndateDataAnimatin(Global.highScore,tvScore,"+"+y);
			}else
			{
				//Global.highScore -= y2;
				//AnimatinUtil.setUndateDataAnimatin(Global.highScore,tvScore,"-"+y2);
			}
		}else if(level.equals("终级测试"))
		{
			y = (TIMES-times)+TIME*CLICK;//答对得分，分数等于 时间*5+卡片个数*点击的次数
//			y2 = times+TIME*(TIMES-CLICK);//打错扣分，分数等于 时间*5+卡片个数*点击的次数
			if(res)
			{
				Global.ultimateScore += y;
				AnimatinUtil.setUndateDataAnimatin(Global.ultimateScore,tvScore,"+"+y);
			}else
			{
				//Global.ultimateScore -=y2;
				//AnimatinUtil.setUndateDataAnimatin(Global.ultimateScore,tvScore,"-"+y2);
			}
		}else if(level.equals("自定义测试"))
		{
			y = (TIMES-times)+TIME*CLICK;//答对得分，分数等于 时间*5+卡片个数*点击的次数
//			y2 = times+TIME*(TIMES-CLICK);//打错扣分，分数等于 时间*5+卡片个数*点击的次数
			if (res) {
				Global.customScore += y;
				AnimatinUtil.setUndateDataAnimatin(Global.customScore,tvScore,"+"+y);
			} else {
				//Global.customScore  -= y2;
				//AnimatinUtil.setUndateDataAnimatin(Global.customScore,tvScore,"-"+y2);
			}

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
		etAnswer.setText("");
		tvTimer.setText(TIMES + "");
		//分数重置
		resetScore(TIMES);
		btnStart.setEnabled(false);// 开始按钮不可用
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 每隔一秒发送一条消息
				mHandler.sendEmptyMessage(UPDATE);
			}
		}, 0, Global.SPEED);

	}

	// 开始作答
	private void startAnswer() {
		btnStartAnswer.setEnabled(false);
		// 使底部按钮可用
		enableBottomBtn(true);
		btnSeeAnswer.setEnabled(true);
		testTimer = new Timer();
		// 计时开始
		testTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(TEST_TIME_UPDATE);
			}
		}, 0, 1000);

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
					// 开始作答按钮可按
					btnStartAnswer.setEnabled(true);
					imageSwitcher.setImageResource(R.drawable.empty);
					return;

				}
				currentImageId = random.nextInt(10);
				remembers[SECOND - 1] = currentImageId;// 记录播放的图片id
				imageSwitcher.setImageResource(images[currentImageId]);

			} else if (msg.what == TEST_TIME_UPDATE) {
				times--;
				tvTimer.setText(times + "");
				if (times <= 0) {
					gameOver(false);
				}
			}else if(msg.what == SEE_ANSWER_MESSAGE)
			{
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

	/**
	 * 重置按钮的初始状态
	 */
	private void resetButton() {
		enableBottomBtn(false);// 底部按钮不可用
		btnSeeAnswer.setEnabled(false);//查看答案按钮不可用
		btnStart.setEnabled(true);// 开始播放按钮可用
		btnStartAnswer.setEnabled(false);// 开始作答，按钮不可用
	}
	private void gameOver(boolean win) {
		if (win) {
			// 游戏结束
			CreateAlertDialog.winDialog(this, true,"test");
			saveRecord(TIMES);//存储最高纪录
			//奖励金币动画
			//AnimatinUtil.setUndateDataAnimatin(Global.gold, tvGold, "+"+winGold);
		} else {
			CreateAlertDialog.winDialog(this, false,"test");
		}

		CLICK = 0;
		//时间重置
		times = TIMES;

		testTimer.cancel();
		resetButton();


	}
	private void saveRecord(int level){
		pref = getSharedPreferences("record", MODE_PRIVATE);
		switch (level) {
			case Global.PRE_TIME:
				int maxPrimaryScore = pref.getInt("primary", 0);
				if (maxPrimaryScore < Global.primaryScore) {
					editor = pref.edit();
					editor.putInt("primary", Global.primaryScore);
					editor.commit();
					//弹出最高纪录对话框
					CreateAlertDialog.newRecordDialog(GameTestActivity.this, Global.primaryScore);
				}
				break;
			case Global.MIDDLE_TIME:
				int maxmiddleScore = pref.getInt("middle", 0);
				if (maxmiddleScore < Global.middleScore) {
					editor = pref.edit();
					editor.putInt("middle", Global.middleScore);
					editor.commit();
					//弹出最高纪录对话框
					CreateAlertDialog.newRecordDialog(GameTestActivity.this, Global.middleScore);
				}
				break;
			case Global.HIGH_TIME:
				int maxhighScore = pref.getInt("high", 0);
				if (maxhighScore < Global.highScore) {
					editor = pref.edit();
					editor.putInt("high", Global.highScore);
					editor.commit();
					//弹出最高纪录对话框
					CreateAlertDialog.newRecordDialog(GameTestActivity.this, Global.highScore);
				}
				break;
			case Global.ULTIMATE_TIME:
				int maxultimateScore = pref.getInt("ultimate", 0);
				if (maxultimateScore < Global.ultimateScore) {
					editor = pref.edit();
					editor.putInt("ultimate", Global.ultimateScore);
					editor.commit();
					//弹出最高纪录对话框
					CreateAlertDialog.newRecordDialog(GameTestActivity.this, Global.ultimateScore);
				}
				break;
			default:
				int maxCustomScore = pref.getInt("custom", 0);
				if (maxCustomScore < Global.customScore) {
					editor = pref.edit();
					editor.putInt("custom", Global.customScore);
					editor.commit();
					//弹出最高纪录对话框
					CreateAlertDialog.newRecordDialog(GameTestActivity.this, Global.customScore);
				}
				break;
		}


	}
	/**
	 * 重置分数
	 * @param level
	 */
	private void resetScore(int level) {
		tvScore.setText("0");
		switch (level) {
			case Global.PRE_TIME:
				Global.primaryScore = 0;
				break;
			case Global.MIDDLE_TIME:
				Global.middleScore = 0;
				break;
			case Global.HIGH_TIME:
				Global.highScore = 0;
				break;
			case Global.ULTIMATE_TIME:
				Global.ultimateScore = 0;
				break;
			default:
				break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
		if (testTimer != null) {
			testTimer.cancel();
		}
	}
}
