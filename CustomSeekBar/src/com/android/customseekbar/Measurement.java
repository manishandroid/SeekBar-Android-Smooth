package com.android.customseekbar;

import android.view.View;

public enum Measurement
{
	AT_MOST(View.MeasureSpec.AT_MOST), EXACTLY(View.MeasureSpec.EXACTLY), UNSPECIFIED(View.MeasureSpec.UNSPECIFIED);
	private final int mModeValue;

	private Measurement(int modeValue)
	{
		mModeValue = modeValue;
	}

	public int getModeValue()
	{
		return mModeValue;
	}

	public static Measurement getMode(int measureSpec)
	{

		final int modeValue = View.MeasureSpec.getMode(measureSpec);

		for (Measurement mode : Measurement.values())
		{
			if (mode.getModeValue() == modeValue)
			{
				return mode;
			}
		}
		return null;
	}
}
