package com.android.customseekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

class SeekThumb
{
	private static final float MINIMUM_TARGET_RADIUS_DP = 24;
	private static final float DEFAULT_THUMB_RADIUS_DP = 14;
	private static final int DEFAULT_THUMB_COLOR_NORMAL = 0xff33b5e5;
	private static final int DEFAULT_THUMB_COLOR_PRESSED = 0xff33b5e5;
	private final float mTargetRadiusPx;
	private final Bitmap mImageNormal;
	private final Bitmap mImagePressed;
	private final float mHalfWidthNormal;
	private final float mHalfHeightNormal;
	private final float mHalfWidthPressed;
	private final float mHalfHeightPressed;
	private boolean mIsPressed = false;
	private final float mY;
	private float mX;
	private Paint mPaintNormal;
	private Paint mPaintPressed;
	private float mThumbRadiusPx;
	private boolean mUseBitmap;
	private int mThumbColorNormal;
	private int mThumbColorPressed;

	SeekThumb(Context ctx, float y, int thumbColorNormal, int thumbColorPressed, float thumbRadiusDP, int thumbImageNormal,
			int thumbImagePressed)
	{
		final Resources res = ctx.getResources();
		mImageNormal = BitmapFactory.decodeResource(res, thumbImageNormal);
		mImagePressed = BitmapFactory.decodeResource(res, thumbImagePressed);
		if (thumbRadiusDP == -1 && thumbColorNormal == -1 && thumbColorPressed == -1)
		{
			mUseBitmap = true;
		}
		else
		{
			mUseBitmap = false;
			if (thumbRadiusDP == -1)
				mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THUMB_RADIUS_DP, res.getDisplayMetrics());
			else
				mThumbRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, thumbRadiusDP, res.getDisplayMetrics());
			if (thumbColorNormal == -1)
				mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
			else
				mThumbColorNormal = thumbColorNormal;
			if (thumbColorPressed == -1)
				mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
			else
				mThumbColorPressed = thumbColorPressed;
			mPaintNormal = new Paint();
			mPaintNormal.setColor(mThumbColorNormal);
			mPaintNormal.setAntiAlias(true);
			mPaintPressed = new Paint();
			mPaintPressed.setColor(mThumbColorPressed);
			mPaintPressed.setAntiAlias(true);
		}
		mHalfWidthNormal = mImageNormal.getWidth() / 2f;
		mHalfHeightNormal = mImageNormal.getHeight() / 2f;
		mHalfWidthPressed = mImagePressed.getWidth() / 2f;
		mHalfHeightPressed = mImagePressed.getHeight() / 2f;
		int targetRadius = (int) Math.max(MINIMUM_TARGET_RADIUS_DP, thumbRadiusDP);
		mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, targetRadius, res.getDisplayMetrics());

		mX = mHalfWidthNormal;
		mY = y;
	}

	float getHalfWidth()
	{
		return mHalfWidthNormal;
	}

	float getHalfHeight()
	{
		return mHalfHeightNormal;
	}

	void setX(float x)
	{
		mX = x;
	}

	float getX()
	{
		return mX;
	}

	boolean isPressed()
	{
		return mIsPressed;
	}

	void press()
	{
		mIsPressed = true;
	}

	void release()
	{
		mIsPressed = false;
	}

	boolean isInTargetZone(float x, float y)
	{

		if (Math.abs(x - mX) <= mTargetRadiusPx && Math.abs(y - mY) <= mTargetRadiusPx)
		{
			return true;
		}
		return false;
	}

	void draw(Canvas canvas)
	{
		if (mUseBitmap)
		{

			final Bitmap bitmap = (mIsPressed) ? mImagePressed : mImageNormal;

			if (mIsPressed)
			{
				final float topPressed = mY - mHalfHeightPressed;
				final float leftPressed = mX - mHalfWidthPressed;
				canvas.drawBitmap(bitmap, leftPressed, topPressed, null);
			}
			else
			{
				final float topNormal = mY - mHalfHeightNormal;
				final float leftNormal = mX - mHalfWidthNormal;
				canvas.drawBitmap(bitmap, leftNormal, topNormal, null);
			}

		}
		else
		{
			if (mIsPressed)
				canvas.drawCircle(mX, mY, mThumbRadiusPx, mPaintPressed);
			else
				canvas.drawCircle(mX, mY, mThumbRadiusPx, mPaintNormal);
		}
	}
}
