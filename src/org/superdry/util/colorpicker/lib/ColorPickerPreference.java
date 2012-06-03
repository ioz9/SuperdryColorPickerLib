package org.superdry.util.colorpicker.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ColorPickerPreference extends Preference {

    private int mColor;
    private int mBorderViewWidth;
    private SupportActivity mSupportActivity;

    public ColorPickerPreference(Context context) {
        super(context);
        setup(context, null, 0);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, 0);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs, defStyle);
    }

    private void setup(final Context context, AttributeSet attrs, int defStyle) {
        setWidgetLayoutResource(R.layout.prefcolorview);

        setColor(getPersistedInt(Color.BLUE));

        mBorderViewWidth = context.getResources().getDimensionPixelSize(R.dimen.border_width);

        setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(context, SuperdryColorPicker.class);
                intent.putExtra(SuperdryColorPicker.EXTRA_INITIAL_COLOR, mColor);
                if (mSupportActivity != null) {
                    mSupportActivity.startActivityForResult(intent, SuperdryColorPicker.ACTION_GETCOLOR);
                }
                return true;
            }
        });
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.prefcolorlayout);
        if (linearlayout.getChildCount() == 0) {
            linearlayout.addView(new BorderView(getContext(), mColor, mBorderViewWidth), mBorderViewWidth, mBorderViewWidth);
        }
    }

    public void setSupportActivity(SupportActivity supportActivity) {
        mSupportActivity = supportActivity;
        mSupportActivity.setOnActivityResultListener(new OnActivityResultListener() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                if (requestCode == SuperdryColorPicker.ACTION_GETCOLOR) {
                    if (resultCode == Activity.RESULT_OK) {
                        Bundle b = intent.getExtras();
                        if (b != null) {
                            setColor(b.getInt(SuperdryColorPicker.EXTRA_INITIAL_COLOR));
                        }
                    }
                }
            }
        });
    }

    public void setColor(int color) {
        mColor = color;
        setSummary(color2String(mColor));
        persistInt(mColor);
    }

    private String color2String(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }

    private static class BorderView extends View {

        private int mBorderViewWidth;
        private int mColor;
        private Paint mPaint = new Paint();

        public BorderView(Context context, int color, int boarderViewWidth) {
            super(context);
            mBorderViewWidth = boarderViewWidth;
            mColor = color;
            mPaint.setAntiAlias(true);
        }

        /**
         * TODO Reuse RectF, object creation during onDraw is very expensive
         */
        @Override
        protected void onDraw(Canvas canvas) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
            canvas.drawRoundRect(new RectF(0, 0, mBorderViewWidth, mBorderViewWidth), 3, 3, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.BLACK);
            canvas.drawRoundRect(new RectF(1, 1, mBorderViewWidth - 1, mBorderViewWidth - 1), 3, 3, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor);
            canvas.drawRoundRect(new RectF(2, 2, mBorderViewWidth - 2, mBorderViewWidth - 2), 3, 3, mPaint);
        }
    }

    public static interface SupportActivity {

        public void startActivityForResult(Intent intent, int requestCode);

        public void setOnActivityResultListener(OnActivityResultListener listener);
    }

    public static interface OnActivityResultListener {

        public void onActivityResult(int requestCode, int resultCode, Intent intent);
    }
}