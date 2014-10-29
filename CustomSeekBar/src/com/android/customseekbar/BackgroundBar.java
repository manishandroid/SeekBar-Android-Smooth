package com.android.customseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

class BackgroundBar
{
	private final Paint mPaint;
	private final float mLeftX;
	private final float mRightX;
	private final float mY;
	private int mNumSegments;
	private float mProgressDistance;
	private final float mTickStartY;
	private final float mTickEndY;

	BackgroundBar(Context ctx, float x, float y, float length, int progressCount, float BarWeight, int BarColor)
	{

		mLeftX = x;
		mRightX = x + length;
		mY = y;

		mNumSegments = progressCount - 1;
		mProgressDistance = length / mNumSegments;
		mTickStartY = mY / 2f;
		mTickEndY = mY / 2f;
		mPaint = new Paint();
		mPaint.setColor(BarColor);
		mPaint.setStrokeWidth(BarWeight);
		mPaint.setAntiAlias(true);
	}

	void draw(Canvas canvas)
	{

		canvas.drawLine(mLeftX, mY, mRightX, mY, mPaint);

		drawTicks(canvas);
	}

	float getLeftX()
	{
		return mLeftX;
	}

	float getRightX()
	{
		return mRightX;
	}

	float getNearestTickCoordinate(SeekThumb thumb)
	{

		final int nearestTickIndex = getNearestTickIndex(thumb);

		final float nearestTickCoordinate = mLeftX + (nearestTickIndex * mProgressDistance);

		return nearestTickCoordinate;
	}

	int getNearestTickIndex(SeekThumb thumb)
	{

		final int nearestTickIndex = (int) ((thumb.getX() - mLeftX + mProgressDistance / 2f) / mProgressDistance);

		return nearestTickIndex;
	}

	void setTickCount(int tickCount)
	{

		final float barLength = mRightX - mLeftX;

		mNumSegments = tickCount - 1;
		mProgressDistance = barLength / mNumSegments;
	}

	private void drawTicks(Canvas canvas)
	{
		for (int i = 0; i < mNumSegments; i++)
		{
			final float x = i * mProgressDistance + mLeftX;
			canvas.drawLine(x, mTickStartY, x, mTickEndY, mPaint);
		}
		canvas.drawLine(mRightX, mTickStartY, mRightX, mTickEndY, mPaint);
	}
}
