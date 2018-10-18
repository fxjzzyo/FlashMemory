package com.fxj.flashmemory.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

public class AnimatinUtil {
	@SuppressLint("NewApi")
	public static void setUndateDataAnimatin(final int data, final View v,String num) {
		((TextView)v).setText(num);
		Log.i("tag", num);
		AnimatorSet mHeartAnimator;
		ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(v,
				"translationX", new float[] { +100.0F });
		ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(v,
				"alpha", new float[] { 0.0F });
		mHeartAnimator = new AnimatorSet();
		mHeartAnimator.playTogether(new Animator[] { localObjectAnimator1,
				localObjectAnimator2 });
		mHeartAnimator.setDuration(600L);
		mHeartAnimator.setInterpolator(new LinearInterpolator());
		mHeartAnimator.start();
		mHeartAnimator.addListener(new AnimatorListener() {

			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animator arg0) {
				AnimatorSet mHeartAnimator;
				ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(v,
						"translationX", new float[] { 0.0F });
				ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(v,
						"alpha", new float[] { 1.0F });
				mHeartAnimator = new AnimatorSet();
				mHeartAnimator.playTogether(new Animator[] {
						localObjectAnimator1, localObjectAnimator2 });
				mHeartAnimator.setDuration(0L);
				mHeartAnimator.setInterpolator(new LinearInterpolator());
				mHeartAnimator.start();
				v.clearAnimation();
				((TextView) v).setText(data + "");

			}

			public void onAnimationCancel(Animator arg0) {

			}
		});
	}
}
