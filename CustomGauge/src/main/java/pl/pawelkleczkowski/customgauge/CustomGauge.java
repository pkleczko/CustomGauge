package pl.pawelkleczkowski.customgauge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class CustomGauge extends View {

    private static final int DEFAULT_LONG_POINTER_SIZE = 1;

    private Paint mPaint;
    private float mStrokeWidth;
    private int mStrokeColor;
    private RectF mRect;
    private String mStrokeCap;
    private int mStartAngle;
    private int mSweepAngle;
    private int mStartValue;
    private int mEndValue;
    private int mValue;
    private double mPointAngle;
    private int mPoint;
    private int mPointSize;
    private int mPointStartColor;
    private int mPointEndColor;
    private int mDividerColor;
    private int mDividerSize;
    private int mDividerStepAngle;
    private int mDividersCount;
    private boolean mDividerDrawFirst;
    private boolean mDividerDrawLast;

    public CustomGauge(Context context) {
        super(context);
        init();
    }
    public CustomGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomGauge, 0, 0);

        // stroke style
        mStrokeWidth = a.getDimension(R.styleable.CustomGauge_gaugeStrokeWidth, 10);
        mStrokeColor = a.getColor(R.styleable.CustomGauge_gaugeStrokeColor, ContextCompat.getColor(context, android.R.color.darker_gray));
        mStrokeCap = a.getString(R.styleable.CustomGauge_gaugeStrokeCap);

        // angle start and sweep (opposite direction 0, 270, 180, 90)
        setStartAngle(a.getInt(R.styleable.CustomGauge_gaugeStartAngle, 0));
        setSweepAngle(a.getInt(R.styleable.CustomGauge_gaugeSweepAngle, 360));

        // scale (from mStartValue to mEndValue)
        setStartValue(a.getInt(R.styleable.CustomGauge_gaugeStartValue, 0));
        setEndValue(a.getInt(R.styleable.CustomGauge_gaugeEndValue, 1000));

        // pointer size and color
        mPointSize = a.getInt(R.styleable.CustomGauge_gaugePointSize, 0);
        mPointStartColor = a.getColor(R.styleable.CustomGauge_gaugePointStartColor, ContextCompat.getColor(context, android.R.color.white));
        mPointEndColor = a.getColor(R.styleable.CustomGauge_gaugePointEndColor, ContextCompat.getColor(context, android.R.color.white));

        // divider options
        int dividerSize = a.getInt(R.styleable.CustomGauge_gaugeDividerSize, 0);
        mDividerColor = a.getColor(R.styleable.CustomGauge_gaugeDividerColor, ContextCompat.getColor(context, android.R.color.white));
        int dividerStep = a.getInt(R.styleable.CustomGauge_gaugeDividerStep, 0);
        mDividerDrawFirst = a.getBoolean(R.styleable.CustomGauge_gaugeDividerDrawFirst, true);
        mDividerDrawLast = a.getBoolean(R.styleable.CustomGauge_gaugeDividerDrawLast, true);

        // calculating one point sweep
        mPointAngle = ((double) Math.abs(mSweepAngle) / (mEndValue - mStartValue));

        // calculating divider step
        if (dividerSize > 0) {
            mDividerSize = mSweepAngle / (Math.abs(mEndValue - mStartValue) / dividerSize);
            mDividersCount = 100 / dividerStep;
            mDividerStepAngle = mSweepAngle / mDividersCount;
        }
        a.recycle();
        init();
    }

    private void init() {
        //main Paint
        mPaint = new Paint();
        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        setStrokeCap(mStrokeCap, false);
        mPaint.setStyle(Paint.Style.STROKE);
        mRect = new RectF();

        mValue = mStartValue;
        mPoint = mStartAngle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float paddingLeft = getPaddingLeft();
        float paddingRight= getPaddingRight();
        float paddingTop = getPaddingTop();
        float paddingBottom = getPaddingBottom();
        float width = getWidth() - (paddingLeft+paddingRight);
        float height = getHeight() - (paddingTop+paddingBottom);
        float radius = (width > height ? width/2 : height/2);

        float rectLeft = width/2 - radius + paddingLeft;
        float rectTop = height/2 - radius + paddingTop;
        float rectRight = width/2 - radius + paddingLeft + width;
        float rectBottom = height/2 - radius + paddingTop + height;

        mRect.set(rectLeft, rectTop, rectRight, rectBottom);

        mPaint.setColor(mStrokeColor);
        mPaint.setShader(null);
        canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint);
        mPaint.setColor(mPointStartColor);
        mPaint.setShader(new LinearGradient(getWidth(), getHeight(), 0, 0, mPointEndColor, mPointStartColor, Shader.TileMode.CLAMP));
        if (mPointSize>0) {//if size of pointer is defined
            if (mPoint > mStartAngle + mPointSize/2) {
                canvas.drawArc(mRect, mPoint - mPointSize/2, mPointSize, false, mPaint);
            }
            else { //to avoid excedding start/zero point
                canvas.drawArc(mRect, mPoint, mPointSize, false, mPaint);
            }
        }
        else { //draw from start point to value point (long pointer)
            if (mValue==mStartValue) //use non-zero default value for start point (to avoid lack of pointer for start/zero value)
                canvas.drawArc(mRect, mStartAngle, DEFAULT_LONG_POINTER_SIZE, false, mPaint);
            else
                canvas.drawArc(mRect, mStartAngle, mPoint - mStartAngle, false, mPaint);
        }

        if (mDividerSize > 0) {
            mPaint.setColor(mDividerColor);
            mPaint.setShader(null);
            int i = mDividerDrawFirst ? 0 : 1;
            int max = mDividerDrawLast ? mDividersCount + 1 : mDividersCount;
            for (; i < max; i++) {
                canvas.drawArc(mRect, mStartAngle + i* mDividerStepAngle, mDividerSize, false, mPaint);
            }
        }

    }

    public void setValue(int value) {
        mValue = value;
        mPoint = (int) (mStartAngle + (mValue-mStartValue) * mPointAngle);
        invalidate();
    }

    public int getValue() {
        return mValue;
    }

    @SuppressWarnings("unused")
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    @SuppressWarnings("unused")
    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getStrokeColor() {
        return mStrokeColor;
    }

    @SuppressWarnings("unused")
    public void setStrokeColor(int strokeColor) {
        mStrokeColor = ContextCompat.getColor(getContext(), strokeColor);;
        mPaint.setColor(mStrokeColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public String getStrokeCap() {
        return mStrokeCap;
    }

    @SuppressWarnings("unused")
    public void setStrokeCap(String strokeCap) {
        setStrokeCap(strokeCap, true);
    }

    private void setStrokeCap(String strokeCap, boolean invalidate) {
        mStrokeCap = strokeCap;
        if (!TextUtils.isEmpty(mStrokeCap)) {
            if (mStrokeCap.equals("BUTT"))
                mPaint.setStrokeCap(Paint.Cap.BUTT);
            else if (mStrokeCap.equals("ROUND"))
                mPaint.setStrokeCap(Paint.Cap.ROUND);
        } else
            mPaint.setStrokeCap(Paint.Cap.BUTT);
        if (invalidate)
            invalidate();
    }

    @SuppressWarnings("unused")
    public int getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    @SuppressWarnings("unused")
    public int getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        mSweepAngle = sweepAngle;
    }

    @SuppressWarnings("unused")
    public int getStartValue() {
        return mStartValue;
    }

    public void setStartValue(int startValue) {
        mStartValue = startValue;
    }

    @SuppressWarnings("unused")
    public int getEndValue() {
        return mEndValue;
    }

    public void setEndValue(int endValue) {
        mEndValue = endValue;
    }

    @SuppressWarnings("unused")
    public int getPointSize() {
        return mPointSize;
    }

    @SuppressWarnings("unused")
    public void setPointSize(int pointSize) {
        mPointSize = pointSize;
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getPointStartColor() {
        return mPointStartColor;
    }

    @SuppressWarnings("unused")
    public void setPointStartColor(int pointStartColor) {
        mPointStartColor = ContextCompat.getColor(getContext(), pointStartColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getPointEndColor() {
        return mPointEndColor;
    }

    @SuppressWarnings("unused")
    public void setPointEndColor(int pointEndColor) {
        mPointEndColor = ContextCompat.getColor(getContext(), pointEndColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public int getDividerColor() {
        return mDividerColor;
    }

    @SuppressWarnings("unused")
    public void setDividerColor(int dividerColor) {
        mDividerColor = ContextCompat.getColor(getContext(), dividerColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isDividerDrawFirst() {
        return mDividerDrawFirst;
    }

    @SuppressWarnings("unused")
    public void setDividerDrawFirst(boolean dividerDrawFirst) {
        mDividerDrawFirst = dividerDrawFirst;
        invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isDividerDrawLast() {
        return mDividerDrawLast;
    }

    @SuppressWarnings("unused")
    public void setDividerDrawLast(boolean dividerDrawLast) {
        mDividerDrawLast = dividerDrawLast;
        invalidate();
    }

}
