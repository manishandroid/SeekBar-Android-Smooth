package com.android.customseekbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomSeekBar extends View
{
	private static final String TAG = "SeekBar";
	private static final int DEFAULT_PROGRESS_COUNT = 0;
	private static final float DEFAULT_BAR_WEIGHT_PX = 3;
	private static final int DEFAULT_BAR_COLOR = 0xff27839a;
	private static final float DEFAULT_CONNECTING_LINE_WEIGHT_PX = 3;
	private static final int DEFAULT_THUMB_IMAGE_NORMAL = R.drawable.slider_thumb;
	private static final int DEFAULT_THUMB_IMAGE_PRESSED = R.drawable.slider_thumb;
	private static final int DEFAULT_CONNECTING_LINE_COLOR = 0xffffffff;
	private static final float DEFAULT_THUMB_RADIUS_DP = 5;
	private static final int DEFAULT_THUMB_COLOR_NORMAL = 0xffffffff;
	private static final int DEFAULT_THUMB_COLOR_PRESSED = 0xffffffff;
	private int mProgressCount = DEFAULT_PROGRESS_COUNT;
	private float mBarWeight = DEFAULT_BAR_WEIGHT_PX;
	private int mBarColor = DEFAULT_BAR_COLOR;
	private float mConnectingLineWeight = DEFAULT_CONNECTING_LINE_WEIGHT_PX;
	private int mConnectingLineColor = DEFAULT_CONNECTING_LINE_COLOR;
	private int mThumbImageNormal = DEFAULT_THUMB_IMAGE_NORMAL;
	private int mThumbImagePressed = DEFAULT_THUMB_IMAGE_PRESSED;

	private float mThumbRadiusDP = DEFAULT_THUMB_RADIUS_DP;
	private int mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
	private int mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
	private boolean mFirstSetTickCount = true;
	private int mDefaultWidth = 400;
	private int mDefaultHeight = 100;
	private SeekThumb mLeftThumb;
	private BackgroundBar mBar;
	private TrailingLine mConnectingLine;
	private CustomSeekBar.OnSeekBarChangeListener mListener;
	private int mLeftIndex = 0;
	private int mRightIndex = mProgressCount - 1;

	public CustomSeekBar(Context context)
	{
		super(context);
	}

	public CustomSeekBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		SeekBarInit(context, attrs);
	}

	public CustomSeekBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		SeekBarInit(context, attrs);
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		final Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putInt("TICK_COUNT", mProgressCount);
		// bundle.putFloat("TICK_HEIGHT_DP", mTickHeightDP);
		bundle.putFloat("BAR_WEIGHT", mBarWeight);
		bundle.putInt("BAR_COLOR", mBarColor);
		bundle.putFloat("CONNECTING_LINE_WEIGHT", mConnectingLineWeight);
		bundle.putInt("CONNECTING_LINE_COLOR", mConnectingLineColor);
		bundle.putInt("THUMB_IMAGE_NORMAL", mThumbImageNormal);
		bundle.putInt("THUMB_IMAGE_PRESSED", mThumbImagePressed);
		bundle.putFloat("THUMB_RADIUS_DP", mThumbRadiusDP);
		bundle.putInt("THUMB_COLOR_NORMAL", mThumbColorNormal);
		bundle.putInt("THUMB_COLOR_PRESSED", mThumbColorPressed);
		bundle.putInt("LEFT_INDEX", mLeftIndex);
		bundle.putInt("RIGHT_INDEX", mRightIndex);
		bundle.putBoolean("FIRST_SET_TICK_COUNT", mFirstSetTickCount);
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		if (state instanceof Bundle)
		{
			final Bundle bundle = (Bundle) state;
			mProgressCount = bundle.getInt("TICK_COUNT");
			// mTickHeightDP = bundle.getFloat("TICK_HEIGHT_DP");
			mBarWeight = bundle.getFloat("BAR_WEIGHT");
			mBarColor = bundle.getInt("BAR_COLOR");
			mConnectingLineWeight = bundle.getFloat("CONNECTING_LINE_WEIGHT");
			mConnectingLineColor = bundle.getInt("CONNECTING_LINE_COLOR");
			mThumbImageNormal = bundle.getInt("THUMB_IMAGE_NORMAL");
			mThumbImagePressed = bundle.getInt("THUMB_IMAGE_PRESSED");
			mThumbRadiusDP = bundle.getFloat("THUMB_RADIUS_DP");
			mThumbColorNormal = bundle.getInt("THUMB_COLOR_NORMAL");
			mThumbColorPressed = bundle.getInt("THUMB_COLOR_PRESSED");
			mLeftIndex = bundle.getInt("LEFT_INDEX");
			mRightIndex = bundle.getInt("RIGHT_INDEX");
			mFirstSetTickCount = bundle.getBoolean("FIRST_SET_TICK_COUNT");
			setThumbIndices(mLeftIndex, mRightIndex);
			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
		}
		else
		{
			super.onRestoreInstanceState(state);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int width;
		int height;
		final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (measureWidthMode == MeasureSpec.AT_MOST)
		{
			width = measureWidth;
		}
		else
			if (measureWidthMode == MeasureSpec.EXACTLY)
			{
				width = measureWidth;
			}
			else
			{
				width = mDefaultWidth;
			}
		if (measureHeightMode == MeasureSpec.AT_MOST)
		{
			height = Math.min(mDefaultHeight, measureHeight);
		}
		else
			if (measureHeightMode == MeasureSpec.EXACTLY)
			{
				height = measureHeight;
			}
			else
			{
				height = mDefaultHeight;
			}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		final Context ctx = getContext();
		final float yPos = h / 2f;
		mLeftThumb = new SeekThumb(ctx, yPos, mThumbColorNormal, mThumbColorPressed, mThumbRadiusDP, mThumbImageNormal, mThumbImagePressed);

		final float marginLeft = mLeftThumb.getHalfWidth();
		final float barLength = w - 2 * marginLeft;
		mBar = new BackgroundBar(ctx, marginLeft, yPos, barLength, mProgressCount, mBarWeight, mBarColor);
		mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mProgressCount - 1)) * barLength);
		final int newLeftIndex = mBar.getNearestTickIndex(mLeftThumb);
		if (newLeftIndex != mLeftIndex)
		{
			mLeftIndex = newLeftIndex;
			if (mListener != null)
			{
				mListener.onIndexChangeListener(this, mLeftIndex);
			}
		}
		mConnectingLine = new TrailingLine(ctx, yPos, mConnectingLineWeight, mConnectingLineColor);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		super.onDraw(canvas);

		mBar.draw(canvas);

		mConnectingLine.draw(canvas, mLeftThumb, null);

		mLeftThumb.draw(canvas);

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		if (!isEnabled())
		{
			return false;
		}

		switch (event.getAction())
		{

		case MotionEvent.ACTION_DOWN:
			onActionDown(event.getX(), event.getY());
			return true;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			this.getParent().requestDisallowInterceptTouchEvent(false);
			onActionUp(event.getX(), event.getY());
			return true;

		case MotionEvent.ACTION_MOVE:
			onActionMove(event.getX());
			this.getParent().requestDisallowInterceptTouchEvent(true);
			return true;

		default:
			return false;
		}
	}

	public void setOnSeekBarChangeListener(CustomSeekBar.OnSeekBarChangeListener listener)
	{
		mListener = listener;
	}

	public void setTickCount(int tickCount)
	{

		if (isValidTickCount(tickCount))
		{
			mProgressCount = tickCount;

			if (mFirstSetTickCount)
			{
				mLeftIndex = 0;
				mRightIndex = mProgressCount - 1;

				if (mListener != null)
				{
					mListener.onIndexChangeListener(this, mLeftIndex);
				}
			}
			if (indexOutOfSeek(mLeftIndex, mRightIndex))
			{
				mLeftIndex = 0;
				mRightIndex = mProgressCount - 1;

				if (mListener != null)
					mListener.onIndexChangeListener(this, mLeftIndex);
			}

			createBar();
			createThumbs();
		}
		else
		{
			Log.e(TAG, "tickCount less than 2; invalid tickCount.");
			throw new IllegalArgumentException("tickCount less than 2; invalid tickCount.");
		}
	}

	public void setBarWeight(float barWeight)
	{

		mBarWeight = barWeight;
		createBar();
	}

	public void setBarColor(int barColor)
	{

		mBarColor = barColor;
		createBar();
	}

	public void setConnectingLineWeight(float connectingLineWeight)
	{

		mConnectingLineWeight = connectingLineWeight;
		createConnectingLine();
	}

	public void setConnectingLineColor(int connectingLineColor)
	{

		mConnectingLineColor = connectingLineColor;
		createConnectingLine();
	}

	public void setThumbRadius(float thumbRadius)
	{

		mThumbRadiusDP = thumbRadius;
		createThumbs();
	}

	public void setThumbImageNormal(int thumbImageNormalID)
	{
		mThumbImageNormal = thumbImageNormalID;
		createThumbs();
	}

	public void setThumbImagePressed(int thumbImagePressedID)
	{
		mThumbImagePressed = thumbImagePressedID;
		createThumbs();
	}

	public void setThumbColorNormal(int thumbColorNormal)
	{
		mThumbColorNormal = thumbColorNormal;
		createThumbs();
	}

	public void setThumbColorPressed(int thumbColorPressed)
	{
		mThumbColorPressed = thumbColorPressed;
		createThumbs();
	}

	public void setThumbIndices(int leftThumbIndex, int rightThumbIndex)
	{
		if (indexOutOfSeek(leftThumbIndex, rightThumbIndex))
		{

			Log.e(TAG, "A thumb index is out of bounds. Check that it is between 0 and mProgressCount - 1");
			throw new IllegalArgumentException("A thumb index is out of bounds. Check that it is between 0 and mProgressCount - 1");

		}
		else
		{

			if (mFirstSetTickCount == true)
				mFirstSetTickCount = false;

			mLeftIndex = leftThumbIndex;
			mRightIndex = rightThumbIndex;
			createThumbs();

			if (mListener != null)
			{
				mListener.onIndexChangeListener(this, mLeftIndex);
			}
		}

		invalidate();
		requestLayout();
	}

	public int getLeftIndex()
	{
		return mLeftIndex;
	}

	public int getRightIndex()
	{
		return mRightIndex;
	}

	private void SeekBarInit(Context context, AttributeSet attrs)
	{
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomSeekBar, 0, 0);

		try
		{
			final Integer tickCount = ta.getInteger(R.styleable.CustomSeekBar_progressCount, DEFAULT_PROGRESS_COUNT);

			if (isValidTickCount(tickCount))
			{
				mProgressCount = tickCount;
				mLeftIndex = 0;
				mRightIndex = mProgressCount - 1;

				if (mListener != null)
				{
					mListener.onIndexChangeListener(this, mLeftIndex);
				}

			}
			else
			{

				Log.e(TAG, "tickCount less than 2; invalid tickCount. XML input ignored.");
			}

			mBarWeight = ta.getDimension(R.styleable.CustomSeekBar_barWeight, DEFAULT_BAR_WEIGHT_PX);
			mBarColor = ta.getColor(R.styleable.CustomSeekBar_barColor, DEFAULT_BAR_COLOR);
			mConnectingLineWeight = ta.getDimension(R.styleable.CustomSeekBar_connectingLineWeight, DEFAULT_CONNECTING_LINE_WEIGHT_PX);
			mConnectingLineColor = ta.getColor(R.styleable.CustomSeekBar_connectingLineColor, DEFAULT_CONNECTING_LINE_COLOR);
			mThumbRadiusDP = ta.getDimension(R.styleable.CustomSeekBar_thumbRadius, DEFAULT_THUMB_RADIUS_DP);
			mThumbImageNormal = ta.getResourceId(R.styleable.CustomSeekBar_thumbImageNormal, DEFAULT_THUMB_IMAGE_NORMAL);
			mThumbImagePressed = ta.getResourceId(R.styleable.CustomSeekBar_thumbImagePressed, DEFAULT_THUMB_IMAGE_PRESSED);
			mThumbColorNormal = ta.getColor(R.styleable.CustomSeekBar_thumbColorNormal, DEFAULT_THUMB_COLOR_NORMAL);
			mThumbColorPressed = ta.getColor(R.styleable.CustomSeekBar_thumbColorPressed, DEFAULT_THUMB_COLOR_PRESSED);

		}
		finally
		{

			ta.recycle();
		}

	}

	private void createBar()
	{

		mBar = new BackgroundBar(getContext(), getMarginLeft(), getYPos(), getBarLength(), mProgressCount, mBarWeight, mBarColor);
		invalidate();
	}

	private void createConnectingLine()
	{

		mConnectingLine = new TrailingLine(getContext(), getYPos(), mConnectingLineWeight, mConnectingLineColor);
		invalidate();
	}

	private void createThumbs()
	{

		Context ctx = getContext();
		float yPos = getYPos();

		mLeftThumb = new SeekThumb(ctx, yPos, mThumbColorNormal, mThumbColorPressed, mThumbRadiusDP, mThumbImageNormal, mThumbImagePressed);

		float marginLeft = getMarginLeft();
		float barLength = getBarLength();
		mLeftThumb.setX(marginLeft + (mLeftIndex / (float) (mProgressCount - 1)) * barLength);
		invalidate();
	}

	private float getMarginLeft()
	{
		return ((mLeftThumb != null) ? mLeftThumb.getHalfWidth() : 0);
	}

	private float getYPos()
	{
		return (getHeight() / 2f);
	}

	private float getBarLength()
	{
		return (getWidth() - 2 * getMarginLeft());
	}

	private boolean indexOutOfSeek(int leftThumbIndex, int rightThumbIndex)
	{
		return (leftThumbIndex < 0 || leftThumbIndex >= mProgressCount || rightThumbIndex < 0 || rightThumbIndex >= mProgressCount);
	}

	private boolean isValidTickCount(int tickCount)
	{
		return (tickCount > 1);
	}

	private void onActionDown(float x, float y)
	{

		if (!mLeftThumb.isPressed() && mLeftThumb.isInTargetZone(x, y))
		{

			pressThumb(mLeftThumb);

		}
	}

	private void onActionUp(float x, float y)
	{

		if (mLeftThumb.isPressed())
		{

			releaseThumb(mLeftThumb);

		}
		else
		{

			final int newLeftIndex = mBar.getNearestTickIndex(mLeftThumb);
			if (newLeftIndex != mLeftIndex)
			{
				mLeftIndex = newLeftIndex;

				if (mListener != null)
				{
					mListener.onIndexChangeListener(this, mLeftIndex);
				}
			}
		}
	}

	private void onActionMove(float x)
	{

		if (mLeftThumb.isPressed())
		{
			moveThumb(mLeftThumb, x);
		}
		final int newLeftIndex = mBar.getNearestTickIndex(mLeftThumb);
		if (newLeftIndex != mLeftIndex)
		{
			mLeftIndex = newLeftIndex;

			if (mListener != null)
			{
				mListener.onIndexChangeListener(this, mLeftIndex);
			}
		}
	}

	private void pressThumb(SeekThumb thumb)
	{
		if (mFirstSetTickCount == true)
			mFirstSetTickCount = false;
		thumb.press();
		invalidate();
	}

	private void releaseThumb(SeekThumb thumb)
	{

		final float nearestTickX = mBar.getNearestTickCoordinate(thumb);
		thumb.setX(nearestTickX);
		thumb.release();
		invalidate();
	}

	private void moveThumb(SeekThumb thumb, float x)
	{
		if (x < mBar.getLeftX() || x > mBar.getRightX())
		{
		}
		else
		{
			thumb.setX(x);
			invalidate();
		}
	}

	public static interface OnSeekBarChangeListener
	{
		public void onIndexChangeListener(CustomSeekBar SeekBar, int leftThumbIndex);
	}
}