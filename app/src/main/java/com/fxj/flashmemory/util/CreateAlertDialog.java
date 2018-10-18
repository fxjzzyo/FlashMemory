package com.fxj.flashmemory.util;

import cn.waps.AppConnect;

import com.fxj.flashmemory.GamePracticeActivity;
import com.fxj.flashmemory.GameTestActivity;
import com.fxj.flashmemory.R;
import com.fxj.flashmemory.TestActivity;
import com.fxj.flashmemory.data.Global;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAlertDialog {
	private SharedPreferences pref;
	static Context mContext;

	public static void goldInsifficent(final Context context) {
		// 弹出对话框，金币不足！
		AlertDialog.Builder builder = new Builder(context);
		View view = ((Activity) context).getLayoutInflater().inflate(
				R.layout.alert_win_dialog,
				(ViewGroup) ((Activity) context)
						.findViewById(R.layout.alert_win_dialog));
		final TextView tvWin = (TextView) view.findViewById(R.id.tv_win);
		tvWin.setText("金币不足，点击获取！下载应用获得积分（金币）");
		builder.setView(view);
		builder.setTitle("系统提示");
		builder.setPositiveButton("获取", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 显示推荐列表（综合）
				AppConnect.getInstance(context).showOffers(context);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				((Activity) context).finish();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setLayout(Global.screen_width * 2 / 3,
				Global.screen_height / 3);
	}
	public static void winDialog(final Context context,boolean win,String model){
		AlertDialog.Builder builder = new Builder(context);
		View view  = ((Activity) context).getLayoutInflater().inflate(R.layout.alert_win_dialog	, (ViewGroup)((Activity) context).findViewById(R.layout.alert_win_dialog));
		final TextView tvWin = (TextView) view.findViewById(R.id.tv_win);	
		if(model.equals("practice"))
		{
			if(win)
			{
				tvWin.setText("恭喜你，完成挑战！");
			}else
			{
				tvWin.setText("很遗憾，时间到。\n" +
						"继续加油！");
			}
		}else if(model.equals("finish"))
		{
			
		}
		else if(model.equals("test")){
				
				if(win)
				{
					tvWin.setText("恭喜你，完成挑战");
				}else
				{
					tvWin.setText("很遗憾，时间到。\n" +
							"继续加油！");
				}

		}else{//ð��ģʽ
			if(model.equals("第1关"))
			{
				Global.gold+=Global.ADVENTURE_WIN;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN+"个。");
			}else if(model.equals("第2关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*2;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*2+"个。");
			}else if(model.equals("第3关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*3;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*3+"个。");
			}
			else if(model.equals("第4关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*4;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*4+"个。");
			}
			else if(model.equals("第5关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*5;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*5+"个。");
			}
			else if(model.equals("第6关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*6;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*6+"个。");
			}
			else if(model.equals("第7关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*7;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*7+"个。");
			}
			else if(model.equals("第8关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*8;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*8+"个。");
			}
			else if(model.equals("第9关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*9;
				tvWin.setText("恭喜你，闯关成功！\n奖励金币"+Global.ADVENTURE_WIN*9+"个。");
			}
			else if(model.equals("第10关"))
			{
				Global.gold+=Global.ADVENTURE_WIN*10;
				tvWin.setText("恭喜你，通关了！\n地球人已经无法阻挡你了！\n奖励金币"+Global.ADVENTURE_WIN*10+"个。");
			}
		}
		
		builder.setView(view);
		Dialog dialog = builder.create();dialog.show();
		dialog.getWindow().setLayout(Global.screen_width*2/3, Global.screen_height/3);
	}
public static void customModelDialog(final Context context,String model){
	mContext = context;
	AlertDialog.Builder builder = new Builder(context);
	View view  = ((Activity) context).getLayoutInflater().inflate(R.layout.alert_custom_model	, (ViewGroup)((Activity) context).findViewById(R.layout.alert_custom_model));
	TextView tvTimeLimit = (TextView) view.findViewById(R.id.tv_time_tv);
	final EditText etaCardCount = (EditText) view.findViewById(R.id.et_card_count);	
	final EditText etaTimeLimit = (EditText) view.findViewById(R.id.et_time_limit);	
	if(model.equals("practice"))
	{
		etaTimeLimit.setVisibility(View.GONE);
		tvTimeLimit.setVisibility(view.GONE);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String cardCount = etaCardCount.getText().toString().trim();
				boolean leagle =  checknumber(cardCount,"10");
				if(leagle)
				{
					Intent intent = new Intent(mContext, GamePracticeActivity.class);
					intent.putExtra("number", Integer.parseInt(cardCount));
					intent.putExtra("title", "自定义练习");
					mContext.startActivity(intent);
				}
			}
		});
	}else
	{
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String cardCount = etaCardCount.getText().toString().trim();
				String timeLimit = etaTimeLimit.getText().toString().trim();
				boolean leagle =  checknumber(cardCount,timeLimit);
				if(leagle)
				{
					Intent intent = new Intent(mContext, GameTestActivity.class);
					intent.putExtra("number", Integer.parseInt(cardCount));
					intent.putExtra("title", "自定义测试");
					intent.putExtra("timer", Integer.parseInt(timeLimit));//
					mContext.startActivity(intent);
				}
			}
		});
	}
	builder.setView(view);
	builder.setNegativeButton("取消", null);
	Dialog dialog =builder.create();
	dialog.show();
	dialog.getWindow().setLayout(Global.screen_width*2/3, Global.screen_height/3);
}
private static boolean checknumber(String str1,String str2) {
	int cardCount;
	int timeLimit;
	try {
		cardCount = Integer.parseInt(str1);
		timeLimit = Integer.parseInt(str2);
	} catch (NumberFormatException e) {
		e.printStackTrace();
		Toast.makeText(mContext, "输入不合法！", Toast.LENGTH_SHORT).show();
		return false;
	}
	if(cardCount<5||cardCount>50)
	{
		Toast.makeText(mContext, "卡片个数必须大于等于5，小于等于50！", Toast.LENGTH_SHORT).show();
		return false;
	}else if(timeLimit<10||timeLimit>250)
	{
		Toast.makeText(mContext, "用时必须大于等于10，小于等于250！", Toast.LENGTH_SHORT).show();
	return false;
	}
	return true;
}

public static void newRecordDialog(Context context,int score){
	AlertDialog.Builder builder = new Builder(context);
	View view  = ((Activity) context).getLayoutInflater().inflate(R.layout.alert_win_dialog	, (ViewGroup)((Activity) context).findViewById(R.layout.alert_win_dialog));
	final TextView tvWin = (TextView) view.findViewById(R.id.tv_win);	
	tvWin.setText("一项新纪录诞生了！n"+score+"分！");
	builder.setView(view);
	Dialog dialog = builder.create();dialog.show();
	dialog.getWindow().setLayout(Global.screen_width*2/3, Global.screen_height/3);
}

}
