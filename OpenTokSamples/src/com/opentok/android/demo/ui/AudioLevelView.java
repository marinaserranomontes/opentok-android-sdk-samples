package com.opentok.android.demo.ui;

import com.opentok.android.demo.ui.MeterView.OnClickListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class AudioLevelView extends View {

	Context mContext;
	float mValue = 0;
	Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint mPaintGradient = new Paint(Paint.ANTI_ALIAS_FLAG);
	Rect mBounds = new Rect();
	Bitmap mHeadset;
	boolean mMute = false;

	public AudioLevelView(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	public AudioLevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(0xff1f1f1f);
		mPaintGradient.setStyle(Style.FILL);
		mPaintGradient.setColor(0xff98CE00);
	}

	public interface OnClickListener {
		public void onClick(MeterView view);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(getWidth(), 0,
				500 * 0.5f, mPaint);

		if (!mMute) {
			if (mHeadset != null) {
				canvas.drawBitmap(mHeadset,
						(getWidth() - mHeadset.getWidth() - 10),
						mHeadset.getHeight() * 0.5f, mPaint);
			}
			canvas.drawCircle(getWidth(), 0,
					500 * mValue, mPaintGradient);

		} 

	}
	
	public void setIcons(Bitmap headset) {
		mHeadset = headset;
	}

	public void setMeterValue(float value) {
		// Convert linear value to logarithmic
		double db = 20 * Math.log10(value);
		float floor = -40;
		float level = 0;
		if (db > floor) {
			level = (float) db - floor;
			level /= -floor;
		}
		mValue = level;
		// force redraw
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBounds.left = (int) (0 + w * 0.10);
		mBounds.top = (int) (0 + h * 0.10);
		mBounds.right = (int) (w * 0.90);
		mBounds.bottom = (int) (h * 0.90);
		// Update gradient
		mPaintGradient.setShader(new RadialGradient(w / 2, h / 2, h / 2,
				0xff98CE00, 0x8098CE00, TileMode.CLAMP));

	}

	
}
