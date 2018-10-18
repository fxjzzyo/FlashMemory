package com.fxj.flashmemory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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

public class GamePracticeActivity extends Activity implements OnClickListener {
	private   int TIME = 10;//播放10张图片
	protected static final int UPDATE = 0;//每隔一秒发送的消息
	protected static final int SEE_ANSWER_MESSAGE = 2;//查看当前卡片的消息
	private  int SECOND = 0;//计时器
	private  int CLICK = 0;//第几次点击按钮
	
	private String title;
	
	private Button btnStart,btnSeeAnswer;
	private TextView tvGold,tvTitle;
	private EditText etAnswer;
	private ImageSwitcher imageSwitcher;
	 private Timer mTimer = new Timer();
	// 图片资源
	 private int[] images = new int[] { R.drawable.black_0,R.drawable.black_1, R.drawable.black_2,
				R.drawable.black_3, R.drawable.black_4,
				R.drawable.black_5, R.drawable.black_6,
				R.drawable.black_7, R.drawable.black_8, R.drawable.black_9 };
	// 当前图片id
	private int currentImageId = 0;
	private int remembers[];//
	
	private Button btnNumbers[] = new Button[10];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_practice);
		initView();
		initEvent();
	}

	private void initEvent() {
		//获得传递过来的number
		TIME = getIntent().getIntExtra("number", 7);
		remembers = new int[TIME];
		// 获得传递过来的title
				title = getIntent().getStringExtra("title");
				tvTitle.setText(title);
		tvGold.setText(Global.gold+"");
		enableBottomBtn(false);//
		btnSeeAnswer.setEnabled(false);
		for(int i=0;i<btnNumbers.length;i++)
		{
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
				Global.gold+=Integer.parseInt(Global.PRACT_SEE_NUM);//消耗一个金币
				if(Global.gold<=0)
				{
					Global.gold = Global.gold -Integer.parseInt(Global.PRACT_SEE_NUM);
//					CreateAlertDialog.goldInsifficent(GamePracticeActivity.this);
//					return;
				}
				AnimatinUtil.setUndateDataAnimatin(Global.gold, tvGold, Global.PRACT_SEE_NUM);
				mHandler.sendEmptyMessage(SEE_ANSWER_MESSAGE);
				
			}
		});
		// 为imageSwitcher设置工厂，每设置一张图片需要一个ImageView来承载这个图片
		imageSwitcher.setFactory(new ViewFactory() {
		@Override
		public View makeView() {
		// 生产出的是ImageView
		return new ImageView(GamePracticeActivity.this);
		}
		});
		
		// 设置图片切换的动画
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				GamePracticeActivity.this, android.R.anim.slide_in_left));//
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				GamePracticeActivity.this, android.R.anim.slide_out_right));// 从又滑出
		imageSwitcher.setImageResource(R.drawable.empty);//一开始就设置一个空白的
	}

	private void initView() {
		// 实例化控件
		btnStart =	(Button) findViewById(R.id.btn_start);
		btnSeeAnswer = (Button) findViewById(R.id.btn_see_answer);
		
		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		tvGold = (TextView) findViewById(R.id.tv_gold);
		tvTitle = (TextView) findViewById(R.id.tv_head_title);
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
		//初级练习免费
		/*if(Global.gold<=0)
		{
			Global.gold=0;
			//弹出对话框，金币不足！
			CreateAlertDialog.goldInsifficent(this);
			return;
		}*/
		
		String num = ((Button)v).getText().toString();
		
		if(Integer.parseInt(num) == remembers[CLICK])
		{
			CLICK++;
			//将正确答案设置到edittext
			etAnswer.append(num+" ");

			Global.gold+=Integer.parseInt(Global.PRACT_RIGHT_NUM);

			AnimatinUtil.setUndateDataAnimatin(Global.gold,tvGold,"+"+Global.PRACT_RIGHT_NUM);
			if(CLICK==TIME)
			{
				//弹出对话框，你赢了。
				CreateAlertDialog.winDialog(this,true,"practice");
				//游戏结束
				CLICK = 0;
				btnStart.setEnabled(true);
				btnSeeAnswer.setEnabled(false);
				enableBottomBtn(false);
				return;
			}
			
		}else{
			Global.gold+=Integer.parseInt(Global.PRACT_WRNOG_NUM);
			
			if(Global.gold<=0)
			{
				Global.gold = 0;
			}
			AnimatinUtil.setUndateDataAnimatin(Global.gold,tvGold,Global.PRACT_WRNOG_NUM);
		}
		
		
	}
	

	/**
	 * 底部数字按钮是否可用
	 * @param able
	 */
	private void enableBottomBtn(boolean able){
		if(able)
		{
			for(int i=0;i<btnNumbers.length;i++)
			{
				btnNumbers[i].setEnabled(true);
			}
		}else{
			for(int i=0;i<btnNumbers.length;i++)
			{
				btnNumbers[i].setEnabled(false);
			}
		}
	}
	//开始游戏
	private void startGame() {
		etAnswer.setText("");
		btnStart.setEnabled(false);
		 mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				 //每隔一秒发送一条消息
				mHandler.sendEmptyMessage(UPDATE);
			}
		}, 0, Global.SPEED);
	
	}
	private Handler mHandler = new Handler(){
		 Random random = new Random();
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == UPDATE)
			{
				SECOND++;
				if(SECOND>TIME)////数字播放完毕
				{
					StringBuffer sb = new StringBuffer();
					for(int i =0;i<remembers.length;i++)
					{
						sb.append(remembers[i]+" ");
					}
					//数字播放完毕
					mTimer.cancel();
					mTimer = null;
					SECOND = 0;
					//数字按钮可按
					enableBottomBtn(true);
					btnSeeAnswer.setEnabled(true);
					imageSwitcher.setImageResource(R.drawable.empty);
					return;
				}
				currentImageId = random.nextInt(10);
				remembers[SECOND-1] = currentImageId;//记录播放的图片id
				imageSwitcher.setImageResource(images[currentImageId]);
				
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mTimer!=null)
		{
			mTimer.cancel();
		}
	}
}
