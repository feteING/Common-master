package com.fete.basemodel.bannner.CountDownTime;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fete.basemodel.R;

import java.util.Arrays;
import java.util.List;




/**
 * 一个倒计时View
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class CountDownView extends View {

    public static float density = Resources.getSystem().getDisplayMetrics().density;

    public static int dp2px(float v) {
        return (int) (density * v + 0.5);
    }

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private CountDownTimer mCountDownTimer;
    private long mCurrentTime;

    /**
     * 显示倒计时的最小单位
     */
    private Unit mShowRangeMax;
    /**
     * 显示倒计时的最大单位
     */
    private Unit mShowRangeMin;

    /**
     * 默认样式参数
     */
    private float mNumberSize;
    private int mNumberColor;
    private float mNumberWidth;
    private float mNumberHeight;
    private float mNumberRound;
    private int mNumberBackground;
    private float mUnitSize;
    private int mUnitColor;
    private float mUnitWidth;
    private float mUnitHeight;
    private float mUnitRound;
    private int mUnitBackground;

    /**
     * 内容宽度,绘制时因为要居中绘制,所以需要根据内容宽度与View宽度进行计算
     */
    private float mContentWidth;

    /**
     * 绘制模块列表
     */
    private List<Painter> mPainters;

    private RectF mRectF = new RectF();

    /**
     * 保存上次开始倒计时的数据,页面不可见后会自动停止倒计时,当再次可见后,需要这些数据来恢复倒计时状态
     */
    private long mStartTimestamp, mStartDuration, mCountDownInterval;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mNumberSize = ta.getDimension(R.styleable.CountDownView_cdv_number_size, dp2px(12));
        mNumberColor = ta.getColor(R.styleable.CountDownView_cdv_number_color, Color.WHITE);
        mNumberWidth = ta.getDimension(R.styleable.CountDownView_cdv_number_width, dp2px(16));
        mNumberHeight = ta.getDimension(R.styleable.CountDownView_cdv_number_height, dp2px(16));
        mNumberRound = ta.getDimension(R.styleable.CountDownView_cdv_number_round, dp2px(2));
        mNumberBackground = ta.getColor(R.styleable.CountDownView_cdv_number_background, Color.BLACK);
        mUnitSize = ta.getDimension(R.styleable.CountDownView_cdv_unit_size, dp2px(12));
        mUnitColor = ta.getColor(R.styleable.CountDownView_cdv_unit_color, Color.BLACK);
        mUnitWidth = ta.getDimension(R.styleable.CountDownView_cdv_unit_width, dp2px(16));
        mUnitHeight = ta.getDimension(R.styleable.CountDownView_cdv_unit_height, dp2px(16));
        mUnitRound = ta.getDimension(R.styleable.CountDownView_cdv_unit_round, dp2px(2));
        mUnitBackground = ta.getColor(R.styleable.CountDownView_cdv_unit_background, Color.TRANSPARENT);
        Unit max = Unit.parse(ta.getInteger(R.styleable.CountDownView_cdv_range_max, Unit.DAY.index));
        Unit min = Unit.parse(ta.getInteger(R.styleable.CountDownView_cdv_range_min, Unit.MILLISECOND.index));
        setShowRange(min, max);
        ta.recycle();
        //
        mPaint.setTextAlign(Paint.Align.CENTER);
        //
        applyDefaultStyle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算内容宽高
        mContentWidth = 0;
        float bodiesHeight = 0;
        for (Painter body : mPainters) {
            if (body.visible(mShowRangeMin, mShowRangeMax)) {
                mContentWidth += body.width();
                bodiesHeight = Math.max(bodiesHeight, body.height());
            }
        }
        //计算父组件给的大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
            default:
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) mContentWidth, MeasureSpec.EXACTLY);
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
            default:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) bodiesHeight, MeasureSpec.EXACTLY);
                break;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置默认倒计时样式
     */
    private void applyDefaultStyle() {
        setPainter(
                createDefaultNumberPainter(Unit.DAY), createDefaultUnitPainter(Unit.DAY, '天'),
                createDefaultNumberPainter(Unit.HOUR), createDefaultUnitPainter(Unit.HOUR, '时'),
                createDefaultNumberPainter(Unit.MINUTE), createDefaultUnitPainter(Unit.MINUTE, '分'),
                createDefaultNumberPainter(Unit.SECOND), createDefaultUnitPainter(Unit.SECOND, '秒'),
                createDefaultNumberPainter(Unit.MILLISECOND), createDefaultUnitPainter(Unit.MILLISECOND, '毫')
        );
    }

    /**
     * 设置倒计时显示区间
     *
     * @param min 显示倒计时的最小单位
     * @param max 显示倒计时的最大单位
     * @throws IllegalArgumentException 如果大单位小于小单位,则抛出异常
     */
    public CountDownView setShowRange(Unit min, Unit max) {
        if (min.max > max.min) {
            throw new IllegalArgumentException("min.max >= max.min");
        } else {
            mShowRangeMin = min;
            mShowRangeMax = max;
            invalidate();
            return this;
        }
    }

    /**
     * @see #start(long, Unit)
     */
    public void start(int second, Unit interval) {
        start(0, 0, 0, second, interval);
    }

    /**
     * @see #start(long, Unit)
     */
    public void start(int minute, int second, Unit interval) {
        start(0, 0, minute, second, interval);
    }

    /**
     * @see #start(long, Unit)
     */
    public void start(int hour, int minute, int second, Unit interval) {
        start(0, hour, minute, second, interval);
    }

    /**
     * @see #start(long, Unit)
     */
    public void start(int day, int hour, int minute, int second, Unit interval) {
        start(day * Unit.DAY.min + hour * Unit.HOUR.min + minute * Unit.MINUTE.min + second * Unit.SECOND.min, interval);
    }



    /**
     * 启动倒计时
     *
     * @param time 时长
     */
    public void start(long time, Unit interval) {
        mStartTimestamp = System.currentTimeMillis();
        mCountDownInterval = interval.min;
        mStartDuration = time;
        restart(time, interval.min);
    }

    private void restart(long time, long interval) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(time, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentTime = millisUntilFinished;
                invalidate();
            }

            @Override
            public void onFinish() {
                mStartDuration = -1;
                mStartTimestamp = -1;
                mCountDownInterval = -1;
                mCountDownTimer = null;
            }
        };
        mCountDownTimer.start();
    }

    /**
     * @see #stop()
     */
    public void stop() {
        stop(false);
    }

    /**
     * 停止倒计时
     *
     * @param clear 是否清除倒计时数字
     */
    public void stop(boolean clear) {
        if (mCountDownTimer != null) {
            mStartDuration = -1;
            mStartTimestamp = -1;
            mCountDownInterval = -1;
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (clear) {
            mCurrentTime = 0;
            invalidate();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            //View可见,如果之前是处于倒计时状态,则恢复
            if (mCountDownTimer == null && mStartTimestamp != -1) {
                long newDuration = mStartDuration - (System.currentTimeMillis() - mStartTimestamp);
                if (newDuration > mCountDownInterval) {
                    restart(newDuration, mCountDownInterval);
                } else {
                    //倒计时在不可见期间已结束了
                    mCurrentTime = 0;
                    invalidate();
                }
            }
        } else {
            //View不可见,如果正在倒计时,则停止
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float drawX = centerX - mContentWidth / 2;
        float painterWidth, halfPainterHeight;
        for (Painter painter : mPainters) {
            if (painter.visible(mShowRangeMin, mShowRangeMax)) {
                painterWidth = painter.width();
                halfPainterHeight = painter.height() / 2;
                mRectF.set(drawX, centerY - halfPainterHeight, drawX + painterWidth, centerY + halfPainterHeight);
                painter.draw(canvas, mRectF, mShowRangeMin, mShowRangeMax, mCurrentTime);
                drawX += painterWidth;
            }
        }
    }

    public enum Unit {
        MILLISECOND(4, 10, 1000),
        SECOND(3, MILLISECOND.max, MILLISECOND.max * 60),
        MINUTE(2, SECOND.max, SECOND.max * 60),
        HOUR(1, MINUTE.max, MINUTE.max * 24),
        DAY(0, HOUR.max, MINUTE.max * 99);

        public final int index;
        public final long min, max;//[min,max)

        Unit(int index, long min, long max) {
            this.index = index;
            this.min = min;
            this.max = max;
        }

        /**
         * 本单位是否在倒计时区间内
         */
        boolean visible(Unit min, Unit max) {
            return this.min >= min.min && this.max <= max.max;
        }

        /**
         * 根据倒计时区间取出本单位的数值,如果本单位是区间的最大单位,则数值是可能超出上限的
         */
        public long value(Unit min, Unit max, long time) {
            if (this.min < min.min) {
                return 0;
            } else if (this.max < max.max) {
                return time % this.max / this.min;
            } else {
                return time / this.min;
            }
        }

        public static Unit parse(int index) {
            for (Unit unit : Unit.values()) {
                if (unit.index == index) {
                    return unit;
                }
            }
            throw new IndexOutOfBoundsException("no this index of Unit");
        }
    }

    /**
     * 设置倒计时的绘制模块列表
     * 如果对现有样式不满意,可自定义绘制
     * 最后绘制的结果是所有可见模块从左到右依次绘制
     */
    public CountDownView setPainter(Painter... painters) {
        mPainters = Arrays.asList(painters);
        invalidate();
        return this;
    }

    /**
     * 获取默认数字绘制模块
     */
    public Painter createDefaultNumberPainter(Unit unit) {
        return new NumberPainter(unit);
    }

    /**
     * 获取默认单位绘制模块
     */
    public Painter createDefaultUnitPainter(Unit unit, char show) {
        return new UnitPainter(unit, show);
    }

    /**
     * 绘制模块接口
     */
    public interface Painter {
        /**
         * 绘制模块是否可见,如果不可见,则不计算其所占空间,也不调用绘制方法
         *
         * @param min 显示倒计时的最小单位
         * @param max 显示倒计时的最大单位
         */
        boolean visible(Unit min, Unit max);

        /**
         * 绘制模块需要的宽度
         */
        float width();

        /**
         * 绘制模块需要的高度
         */
        float height();

        /**
         * 自定义绘制方法
         *
         * @param area    绘制区域
         * @param min     显示倒计时的最小单位
         * @param max     显示倒计时的最大单位
         * @param current 倒计时剩余时间
         */
        void draw(Canvas canvas, RectF area, Unit min, Unit max, long current);
    }

    public abstract static class PainterAdapter implements Painter {
        protected final boolean visible;
        protected final int width;
        protected final int height;

        public PainterAdapter() {
            this(false, 0, 0);
        }

        public PainterAdapter(boolean visible) {
            this(visible, 0, 0);
        }

        public PainterAdapter(int size) {
            this(false, size, size);
        }

        public PainterAdapter(int width, int height) {
            this(false, width, height);
        }

        public PainterAdapter(boolean visible, int width, int height) {
            this.visible = visible;
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean visible(Unit min, Unit max) {
            return visible;
        }

        @Override
        public float width() {
            return width;
        }

        @Override
        public float height() {
            return height;
        }
    }

    /**
     * 默认的绘制单位的模块
     */
    private class UnitPainter implements Painter {
        private final char[] show;
        private final Unit unit;

        private UnitPainter(Unit unit, char show) {
            this.show = new char[]{show};
            this.unit = unit;
        }

        @Override
        public boolean visible(Unit min, Unit max) {
            return unit.visible(min, max);
        }

        @Override
        public float width() {
            return mUnitWidth;
        }

        @Override
        public float height() {
            return mUnitHeight;
        }

        @Override
        public void draw(Canvas canvas, RectF area, Unit min, Unit max, long current) {
            //绘制背景
            if (mUnitBackground != Color.TRANSPARENT) {
                mPaint.setColor(mUnitBackground);
                canvas.drawRoundRect(area, mUnitRound, mUnitRound, mPaint);
            }
            //绘制文字
            mPaint.setColor(mUnitColor);
            mPaint.setTextSize(mUnitSize);
            canvas.drawText(show, 0, 1, area.centerX(), area.centerY() - (mPaint.descent() + mPaint.ascent()) / 2f, mPaint);
        }
    }

    /**
     * 默认的绘制数字的模块
     */
    private class NumberPainter implements Painter {
        private final char[] chars = new char[2];
        private final Unit unit;

        private NumberPainter(Unit unit) {
            this.unit = unit;
        }

        @Override
        public boolean visible(Unit min, Unit max) {
            return unit.visible(min, max);
        }

        @Override
        public float width() {
            return mNumberWidth;
        }

        @Override
        public float height() {
            return mNumberHeight;
        }

        @Override
        public void draw(Canvas canvas, RectF area, Unit min, Unit max, long current) {
            //填充文字
            long value = unit.value(min, max, current);
            if (value < 10) {
                chars[0] = '0';
                chars[1] = (char) (value + '0');
            } else if (value < 99) {
                chars[0] = (char) (value / 10 + '0');
                chars[1] = (char) (value % 10 + '0');
            } else {
                chars[0] = '9';
                chars[1] = '9';
            }
            //绘制背景
            if (mNumberBackground != Color.TRANSPARENT) {
                mPaint.setColor(mNumberBackground);
                canvas.drawRoundRect(area, mNumberRound, mNumberRound, mPaint);
            }
            //绘制文字
            mPaint.setColor(mNumberColor);
            mPaint.setTextSize(mNumberSize);
            canvas.drawText(chars, 0, 2, area.centerX(), area.centerY() - (mPaint.descent() + mPaint.ascent()) / 2f, mPaint);
        }
    }

    public float getNumberSize() {
        return mNumberSize;
    }

    public CountDownView setNumberSize(float numberSize) {
        mNumberSize = numberSize;
        return this;
    }

    public int getNumberColor() {
        return mNumberColor;
    }

    public CountDownView setNumberColor(int numberColor) {
        mNumberColor = numberColor;
        return this;
    }

    public float getNumberWidth() {
        return mNumberWidth;
    }

    public CountDownView setNumberWidth(float numberWidth) {
        mNumberWidth = numberWidth;
        return this;
    }

    public float getNumberHeight() {
        return mNumberHeight;
    }

    public CountDownView setNumberHeight(float numberHeight) {
        mNumberHeight = numberHeight;
        return this;
    }

    public float getNumberRound() {
        return mNumberRound;
    }

    public CountDownView setNumberRound(float numberRound) {
        mNumberRound = numberRound;
        return this;
    }

    public int getNumberBackground() {
        return mNumberBackground;
    }

    public CountDownView setNumberBackground(int numberBackground) {
        mNumberBackground = numberBackground;
        return this;
    }

    public float getUnitSize() {
        return mUnitSize;
    }

    public CountDownView setUnitSize(float unitSize) {
        mUnitSize = unitSize;
        return this;
    }

    public int getUnitColor() {
        return mUnitColor;
    }

    public CountDownView setUnitColor(int unitColor) {
        mUnitColor = unitColor;
        return this;
    }

    public float getUnitWidth() {
        return mUnitWidth;
    }

    public CountDownView setUnitWidth(float unitWidth) {
        mUnitWidth = unitWidth;
        return this;
    }

    public float getUnitHeight() {
        return mUnitHeight;
    }

    public CountDownView setUnitHeight(float unitHeight) {
        mUnitHeight = unitHeight;
        return this;
    }

    public float getUnitRound() {
        return mUnitRound;
    }

    public CountDownView setUnitRound(float unitRound) {
        mUnitRound = unitRound;
        return this;
    }

    public int getUnitBackground() {
        return mUnitBackground;
    }

    public CountDownView setUnitBackground(int unitBackground) {
        mUnitBackground = unitBackground;
        return this;
    }
}