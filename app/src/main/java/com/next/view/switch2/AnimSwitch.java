package com.next.view.switch2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.next.view.round.RoundImageView;
import com.next.view.round.RoundRelativeLayout;

/**
 * ClassName:动画切换控件类
 *
 * @author Afton
 * @time 2024/4/16
 * @auditor
 */
public class AnimSwitch extends RoundRelativeLayout {

    //选中改变监听接口
    public interface OnCheckedChangeListener {

        /**
         * 选中状态改变
         *
         * @param isChecked 是否选中
         */
        void onCheckedChanged(boolean isChecked);
    }

    //滑块控件
    private RoundImageView thumbView;

    //背景控件
    private RoundImageView backgroundView;

    //滑块颜色
    private int thumbColor = Color.parseColor("#F0F5FF");

    //背景颜色
    private int backgroundColor = Color.parseColor("#121F3A");

    //动画持续时长
    private int animDuration = 500;

    //选中状态
    private boolean isChecked = false;

    //滑块左边距
    private int leftMargin = 0;

    //滑块尺寸
    private int thumbSize = 0;

    //选中状态改变监听接口
    private OnCheckedChangeListener onCheckedChangeListener;

    public AnimSwitch(Context context) {
        super(context);

        //初始化
        this.init(context, null);
    }

    public AnimSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化
        this.init(context, attrs);
    }

    public AnimSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化
        this.init(context, attrs);
    }

    public AnimSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //初始化
        this.init(context, attrs);
    }

    /**
     * 设置滑块颜色
     *
     * @param resId 颜色
     */
    public void setThumbColor(@ColorInt int resId) {
        this.thumbColor = resId;

        if (this.thumbView != null) {
            this.thumbView.setBackgroundColor(this.thumbColor);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param resId 颜色
     */
    public void setSwitchBackgroundColor(@ColorInt int resId) {
        this.backgroundColor = resId;

        if (this.backgroundView != null) {
            this.backgroundView.setBackgroundColor(this.backgroundColor);
        }
    }

    /**
     * 设置动画持续时长
     *
     * @param duration 持续时长
     */
    public void setAnimDuration(int duration) {
        this.animDuration = duration;
    }

    /**
     * 获取选中状态
     *
     * @return 选中状态
     */
    public boolean isChecked() {
        return this.isChecked;
    }

    /**
     * 设置选中状态
     *
     * @param isChecked 选中状态
     */
    public void setChecked(boolean isChecked) {
        this.setChecked(isChecked, false);
    }

    /**
     * 设置选中状态
     *
     * @param isChecked 选中状态
     * @param isSmooth  是否平滑
     */
    public void setChecked(boolean isChecked, boolean isSmooth) {
        if (this.isChecked == isChecked) {
            return;
        }

        if (isChecked) {
            //展示打开滑块动画
            this.showOpenThumbAnim(isSmooth ? this.animDuration : 0);
            //打开滑块
            this.openThumb();
        } else {
            //展示关闭滑块动画
            this.showCloseThumbAnim(isSmooth ? this.animDuration : 0);
            //关闭滑块
            this.closeThumb();
        }
    }

    /**
     * 设置选中状态改变监听
     *
     * @param onCheckedChangeListener 选中状态改变监听接口
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void init(Context context, AttributeSet attrs) {
        //设置选中状态
        this.isChecked = false;
        //初始化属性
        initAttrs(context, attrs);
        //初始化背景控件
        this.initBackgroundView();
        //初始化滑块控件
        this.initThumbView();
        //设置切换点击处理
        this.setSwitchClick();
        //延迟执行
        this.post(() -> {
            int width = this.getWidth();
            int height = this.getHeight();
            //获取滑块尺寸
            this.thumbSize = (int) (height / 3.0 * 2.0);
            //获取滑块左边距
            this.leftMargin = (int) (this.thumbSize / 4.0);
            //获取滑块半径
            float thumbRadius = (float) (this.thumbSize / 2.0);
            //获取滑块布局
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) this.thumbView.getLayoutParams();
            //设置滑块宽度
            layoutParams.width = this.thumbSize;
            //设置滑块高度
            layoutParams.height = this.thumbSize;
            //设置滑块左边距
            layoutParams.leftMargin = this.isChecked ? width - this.thumbSize - this.leftMargin : this.leftMargin;
            //设置滑块布局
            this.thumbView.setLayoutParams(layoutParams);
            //设置滑块半径
            this.thumbView.setRadius(thumbRadius, false);
            //设置布局半径
            this.setRadius((float) (height / 2.0), false);
        });
    }

    /**
     * 初始化属性
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimSwitch);
        this.thumbColor = ta.getColor(R.styleable.AnimSwitch_thumbColor, Color.parseColor("#F0F5FF"));
        this.backgroundColor = ta.getColor(R.styleable.AnimSwitch_switchBackgroundColor, Color.parseColor("#121F3A"));
        this.animDuration = ta.getInteger(R.styleable.AnimSwitch_duration, 500);
        ta.recycle();
    }

    /**
     * 初始化滑块控件
     */
    private void initThumbView() {
        //创建滑块控件
        this.thumbView = new RoundImageView(this.getContext());
        //设置滑块颜色
        this.thumbView.setBackgroundColor(this.thumbColor);
        //设置滑块布局
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, 0);
        //设置滑块位置
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //添加到布局
        this.addView(this.thumbView, layoutParams);
    }

    /**
     * 初始化背景控件
     */
    private void initBackgroundView() {
        //创建背景控件
        this.backgroundView = new RoundImageView(this.getContext());
        //设置背景颜色
        this.backgroundView.setBackgroundColor(this.backgroundColor);
        //设置背景透明度
        this.backgroundView.setAlpha(0.2f);
        //添加到布局
        this.addView(this.backgroundView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置切换点击处理
     */
    private void setSwitchClick() {
        this.setOnClickListener(v -> {
            //设置选中状态
            this.setChecked(!this.isChecked, true);
        });
    }

    /**
     * 关闭滑块
     */
    private void closeThumb() {
        this.isChecked = false;
        if (this.onCheckedChangeListener != null) {
            this.onCheckedChangeListener.onCheckedChanged(this.isChecked);
        }
    }

    /**
     * 展示关闭滑块动画
     *
     * @param duration 动画时长
     */
    private void showCloseThumbAnim(long duration) {
        //获取宽度
        int width = this.getWidth();
        this.setAnimBackgroundAlphaChange(1.0f, 0.2f, duration);
        this.setAnimThumbLeftMarginChange(width - this.thumbSize - this.leftMargin, this.leftMargin, duration);
    }

    /**
     * 打开滑块
     */
    private void openThumb() {
        this.isChecked = true;
        if (this.onCheckedChangeListener != null) {
            this.onCheckedChangeListener.onCheckedChanged(this.isChecked);
        }
    }

    /**
     * 展示打开滑块动画
     *
     * @param duration 动画时长
     */
    private void showOpenThumbAnim(long duration) {
        //获取宽度
        int width = this.getWidth();
        this.setAnimBackgroundAlphaChange(0.2f, 1.0f, duration);
        this.setAnimThumbLeftMarginChange(this.leftMargin, width - this.thumbSize - this.leftMargin, duration);
    }

    /**
     * 设置滑块左部间隔
     *
     * @param leftMargin 左部间隔
     */
    private void setLeftMargin(float leftMargin) {
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) this.thumbView.getLayoutParams();
        layoutParams.leftMargin = (int) leftMargin;
        this.thumbView.setLayoutParams(layoutParams);
        this.thumbView.requestLayout();
    }

    /**
     * 设置滑块左部间隔动画
     *
     * @param oldLeftMargin 旧左部间隔
     * @param newLeftMargin 新左部间隔
     * @param duration      动画时长
     */
    private void setAnimThumbLeftMarginChange(float oldLeftMargin, float newLeftMargin, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(oldLeftMargin, newLeftMargin);
        animator.setDuration(duration);
        animator.setInterpolator(new EaseCubicInterpolator(0f, 1f, 0f, 1f));
        animator.addUpdateListener(animation -> {
            //设置滑块左部间隔
            this.setLeftMargin((float) animation.getAnimatedValue());
        });
        animator.start();
    }

    /**
     * 设置背景透明度动画
     *
     * @param oldAlpha 旧透明度
     * @param newAlpha 新透明度
     * @param duration 动画时长
     */
    private void setAnimBackgroundAlphaChange(float oldAlpha, float newAlpha, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(oldAlpha, newAlpha);
        animator.setDuration(duration);
        animator.setInterpolator(new EaseCubicInterpolator(0f, 1f, 0f, 1f));
        animator.addUpdateListener(animation -> {
            this.backgroundView.setAlpha((float) animation.getAnimatedValue());
        });
        animator.start();
    }
}