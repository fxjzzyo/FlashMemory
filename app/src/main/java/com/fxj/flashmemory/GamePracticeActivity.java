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
	private   int TIME = 10;//����10��ͼƬ
	protected static final int UPDATE = 0;//ÿ��һ�뷢�͵���Ϣ
	protected static final int SEE_ANSWER_MESSAGE = 2;//�鿴��ǰ��Ƭ����Ϣ
	private  int SECOND = 0;//��ʱ��
	private  int CLICK = 0;//�ڼ��ε����ť
	
	private String title;
	
	private Button btnStart,btnSeeAnswer;
	private TextView tvGold,tvTitle;
	private EditText etAnswer;
	private ImageSwitcher imageSwitcher;
	 private Timer mTimer = new Timer();
	// ͼƬ��Դ
	 private int[] images = new int[] { R.drawable.black_0,R.drawable.black_1, R.drawable.black_2,
				R.drawable.black_3, R.drawable.black_4,
				R.drawable.black_5, R.drawable.black_6,
				R.drawable.black_7, R.drawable.black_8, R.drawable.black_9 };
	// ��ǰͼƬid
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
		//��ô��ݹ�����number
		TIME = getIntent().getIntExtra("number", 7);
		remembers = new int[TIME];
		// ��ô��ݹ�����title
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
				Global.gold+=Integer.parseInt(Global.PRACT_SEE_NUM);//����һ�����
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
		// ΪimageSwitcher���ù�����ÿ����һ��ͼƬ��Ҫһ��ImageView���������ͼƬ
		imageSwitcher.setFactory(new ViewFactory() {
		@Override
		public View makeView() {
		// ����������ImageView
		return new ImageView(GamePracticeActivity.this);
		}
		});
		
		// ����ͼƬ�л��Ķ���
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				GamePracticeActivity.this, android.R.anim.slide_in_left));//
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				GamePracticeActivity.this, android.R.anim.slide_out_right));// ���ֻ���
		imageSwitcher.setImageResource(R.drawable.empty);//һ��ʼ������һ���հ׵�
	}

	private void initView() {
		// ʵ�����ؼ�
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
		//������ϰ���
		/*if(Global.gold<=0)
		{
			Global.gold=0;
			//�����Ի��򣬽�Ҳ��㣡
			CreateAlertDialog.goldInsifficent(this);
			return;
		}*/
		
		String num = ((Button)v).getText().toString();
		
		if(Integer.parseInt(num) == remembers[CLICK])
		{
			CLICK++;
			//����ȷ�����õ�edittext
			etAnswer.append(num+" ");

			Global.gold+=Integer.parseInt(Global.PRACT_RIGHT_NUM);

			AnimatinUtil.setUndateDataAnimatin(Global.gold,tvGold,"+"+Global.PRACT_RIGHT_NUM);
			if(CLICK==TIME)
			{
				//�����Ի�����Ӯ�ˡ�
				CreateAlertDialog.winDialog(this,true,"practice");
				//��Ϸ����
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
	 * �ײ����ְ�ť�Ƿ����
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
	//��ʼ��Ϸ
	private void startGame() {
		etAnswer.setText("");
		btnStart.setEnabled(false);
		 mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				 //ÿ��һ�뷢��һ����Ϣ
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
				if(SECOND>TIME)////���ֲ������
				{
					StringBuffer sb = new StringBuffer();
					for(int i =0;i<remembers.length;i++)
					{
						sb.append(remembers[i]+" ");
					}
					//���ֲ������
					mTimer.cancel();
					mTimer = null;
					SECOND = 0;
					//���ְ�ť�ɰ�
					enableBottomBtn(true);
					btnSeeAnswer.setEnabled(true);
					imageSwitcher.setImageResource(R.drawable.empty);
					return;
				}
				currentImageId = random.nextInt(10);
				remembers[SECOND-1] = currentImageId;//��¼���ŵ�ͼƬid
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
