package org.superdry.util.colorpicker.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ColorPickerPreference extends Preference {

    private int mColor = Color.BLACK;
    private int mBorderViewWidth;

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.prefcolorview);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWidgetLayoutResource(R.layout.prefcolorview);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        LinearLayout linearlayout = (LinearLayout) view.findViewById(R.id.prefcolorlayout);
        if (linearlayout.getChildCount() == 0) {
            linearlayout.addView(new BorderView(getContext(), mColor, mBorderViewWidth), mBorderViewWidth, mBorderViewWidth);
        }
    }

    public void setColor(int color) {
        mColor = color;
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
}