package com.android.customseekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

class TrailingLine
{
	private final Paint mPaint;
	private final float mConnectingLineWeight;
	private final float mY;

	TrailingLine(Context ctx, float y, float connectingLineWeight, int connectingLineColor)
	{
		final Resources res = ctx.getResources();
		mConnectingLineWeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, connectingLineWeight, res.getDisplayMetrics());

		mPaint = new Paint();
		mPaint.setColor(connectingLineColor);
		mPaint.setStrokeWidth(mConnectingLineWeight);
		mPaint.setAntiAlias(true);
		mY = y;
	}

	void draw(Canvas canvas, SeekThumb leftThumb, SeekThumb rightThumb)
	{
		canvas.drawLine(leftThumb.getX(), mY, 0.8f, mY, mPaint);
	}
}
