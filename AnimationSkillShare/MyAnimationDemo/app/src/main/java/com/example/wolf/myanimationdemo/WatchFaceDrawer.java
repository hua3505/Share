package com.example.wolf.myanimationdemo;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 
 * @author wolfxu
 *
 */

public class WatchFaceDrawer {
	
    private static final String TAG = WatchFaceDrawer.class.getSimpleName();
    
    // NOTE: 为了兼容不同的分辨率，设计图坐标为px，需要转换为dp，命名还是按照常理命名规则
    private static final int WATCH_DESIGN_WIDTH_PX = 320;
    private static final int WATCH_DESIGN_HEIGHT_PX = 320;
    private static final int DRIPPING_INTERVAL = 5 * 1000;  //动画时间间隔
    private static final long DRIPPING_DURATION = 500;  //水滴下落的最大持续时间
    private static final long DRIPPING_IN_WATER_DURATION = 100; //水滴入水后继续下落的时间
    private static final long DRIP_FADE_IN_DURATION = 200; //水滴淡入的时间
    private static final long DRIP_FADE_OUT_DURATION = DRIPPING_IN_WATER_DURATION; //水滴淡出的时间
    private static final long SPRAY_FADE_IN_DURATION = 100;
    private static final long SPRAY_FADE_OUT_DURATION = 200;
    private static final long SPRAY_TOTAL_DURATION = 500;
    private static final long WAVE1_SINGLE_MOVE_DURATION = 7 * 1000;
    private static final long WAVE2_SINGLE_MOVE_DURATION = 3 * 1000;
    private static final int DRIPPING_ACCELERATION_FACTOR = 1;  //水滴下落的加速度因子
    private static final int BIG_CIRCLE_COLOR = 0x7F50E3C2;
    private static final int LITTER_CIRCLE_COLOR = 0xFF50E3C2;
    
    private Context mContext;
    private Time mTime;
    private boolean mIsInAmbientMode;
    private boolean mIsLowBitAmbient;
    private int mWeatherTemp;
    private int mWeatherIconResId;
    private String mWeatherIcon;
    private boolean mHasInited;
    private boolean mIsRound;
    private int mWidth;
    private int mHeight;
    private float mScaleRate;
    private float mWaveHeight;
    private long mLastDrippingTime;
    private float mDripHeight;
    private int mDripAlpha;
    private float mWave1Left;
    private float mWave2Left;
    private int mSprayAlpha;
    private float mSprayScaleRate;
    private boolean mHasInitScaleRate;
    
    private AnimatorSet mAnimatorSet;
    private ValueAnimator mWave1MoveAnimator;
    private ValueAnimator mWave2MoveAnimator;
     
    private int BIG_CIRCLE_RADIUS;  //大圆半径
    private int LITTLE_CIRCLE_RADIUS;  //小圆半径
    private int AMBIENT_NUMBER_CLOCK_LEFT;  //ambient模式下数字时钟到左边的距离
    private int AMBIENT_NUMBER_CLOCK_TOP;   //ambient模式下数字时钟到顶部的距离
    private int AMBIENT_PADDING_BETWEEN_NUMBER; //ambient模式下数字时钟，两个数字之间的间距
    private int AMBIENT_PADDING_BETWEEN_NUMBER_COLON;  //ambient模式下数字时钟，数字和冒号之间的间距
    private int AMBIENT_PADDING_BETWEEN_NUMBER_PM;  //ambient模式下数字时钟，数字和am、pm之间的间距
    private int AMBIENT_COLON_TOP_TO_NUMBER;  //ambient模式下数字时钟，冒号到数字顶部的距离
    private int AMBIENT_PM_TOP_TO_NUMBER;   //ambient模式下数字时钟，am、pm到数字顶部的距离
    private int NUMBER_CLOCK_LEFT;
    private int NUMBER_CLOCK_TOP;
    private int DATE_TOP; //日期区域到顶部的距离
    private int DATE_LEFT;
    private int DATE_SLASH_LEFT;
    private int DATE_SLASH_TOP;
    private int DATE_WEEKDAY_LEFT;
    private int DATE_WEEKDAY_TOP;
    private int WEATHER_AREA_LEFT;
    private int WEATHER_AREA_TOP;
    private int WEATHER_ICON_LEFT;
    private int WEATHER_MINUS_LEFT; //气温负号
    private int WEATHER_CELSIUS_LEFT; //摄氏度符号
    private int WAVE_START_HEIGHT;
    private int DRIP_WAVE_CRASH_POINT_TOP;  //碰撞点到水波顶部的距离
    private int DRIPPING_DISTANCE_IN_WATER; //水滴入水后继续下落的距离
    private int SPRAY_HEIGHT_ABOVE_CRASH_POINT; //水花离水滴入水点的距离
    private int WAVE_HEIGHT_NOT_DRIPPING; //不再滴水时的水波高度
    
    private Paint mCircleBitmapPaint;
    private Paint mCirclePaint;
    private Paint mNumberClockBitmapPaint;
    private Paint mWaveBitmapPaint;
    private Paint mDripBitmapPaint;
    private Paint mSprayBitmapPaint;
	
    private Bitmap mAmbientColonBitmap;
    private Bitmap mLetterABitmap;
    private Bitmap mLetterPBitmap;
    private Bitmap mLetterMBitmap;
    private Bitmap mSlashBitmap;
    private Bitmap mWeatherIconBitmap;
    private Bitmap mCelsiusIconBitmap;
    private Bitmap mFahrenheitIconBitmap;
    private Bitmap mMinusIconBitmap;
    private Bitmap mWave1Bitmap;
    private Bitmap mWave2Bitmap;
	private Bitmap mMaskBitmap;
    private Bitmap mDripBitmap;
    private Bitmap mSprayBitmap;	
    private Bitmap [] mAmbientNumberBitmapArray = new Bitmap[10];
    private Bitmap [] mDateNumberBitmapArray = new Bitmap[10];
    private Bitmap [] mWeekdayBitmapArray = new Bitmap[7];
    
    private static final int [] NUMBER_BITMAP_IDS = {
    	R.drawable.number_0,
    	R.drawable.number_1,
    	R.drawable.number_2,
    	R.drawable.number_3,
    	R.drawable.number_4,
    	R.drawable.number_5,
    	R.drawable.number_6,
    	R.drawable.number_7,
    	R.drawable.number_8,
    	R.drawable.number_9,
    };
    
    private static final int[] DATE_NUMBER_BITMAP_IDS = {
    	R.drawable.date_number0,
    	R.drawable.date_number1,
    	R.drawable.date_number2,
    	R.drawable.date_number3,
    	R.drawable.date_number4,
    	R.drawable.date_number5,
    	R.drawable.date_number6,
    	R.drawable.date_number7,
    	R.drawable.date_number8,
    	R.drawable.date_number9,
    };
    
    private static final int [] WEEKDAY_BITMAP_IDS = {
    	R.drawable.mon,
    	R.drawable.tue,
    	R.drawable.wed,
    	R.drawable.thu,
    	R.drawable.fri,
    	R.drawable.sat,
    	R.drawable.sun,
    };
    
    private boolean mIsFahrenheit;
    
    public WatchFaceDrawer(Context context) {
        mContext = context;
    }
    
    /**
     * 表盘设计图的px转换为设备的px
     * 表盘设计图是320px, 元素的位置坐标也是按照320px图标识的，转换为实际在设备上的px
     * @param designPxValue
     * @return
     */
    private int t(int designPx) {
        return (int) (mWidth * 1f / WATCH_DESIGN_WIDTH_PX * designPx);
    }

    /**
     * 初始化
     */
    private void init(int width, int height) {
        // Log.i(TAG, "draw width:" + width + ";height:" + height);

        if (mHasInited) {
            // 只需要初始化一次
            return;
        }
                
        mHasInited = true;
        mWidth = width;
        mHeight = height;
        
        BIG_CIRCLE_RADIUS = t(160);
        LITTLE_CIRCLE_RADIUS = t(145);
        AMBIENT_NUMBER_CLOCK_LEFT = t(98);
        AMBIENT_NUMBER_CLOCK_TOP = t(127);
        AMBIENT_PADDING_BETWEEN_NUMBER = t(4);
        AMBIENT_PADDING_BETWEEN_NUMBER_COLON = t(10);
        AMBIENT_PADDING_BETWEEN_NUMBER_PM = t(11);
        AMBIENT_COLON_TOP_TO_NUMBER = t(17);
        AMBIENT_PM_TOP_TO_NUMBER = t(44);
        NUMBER_CLOCK_LEFT = t(105);
        NUMBER_CLOCK_TOP = t(76);
        DATE_TOP = t(159);
        DATE_LEFT = t(183);
        DATE_SLASH_TOP = DATE_TOP + t(1);
        DATE_SLASH_LEFT = t(201);
        DATE_WEEKDAY_LEFT = t(235);
        DATE_WEEKDAY_TOP = t(163);
        WEATHER_AREA_LEFT = t(68);
        WEATHER_AREA_TOP = t(159);
        WEATHER_ICON_LEFT = t(102 + 14);
        WEATHER_CELSIUS_LEFT = t(91);
        WAVE_START_HEIGHT = t(30);
        DRIP_WAVE_CRASH_POINT_TOP = t(3);
        DRIPPING_DISTANCE_IN_WATER = t(8);
        SPRAY_HEIGHT_ABOVE_CRASH_POINT = t(-5);
        WAVE_HEIGHT_NOT_DRIPPING = t(184);
        
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setStyle(Style.STROKE);
        mCirclePaint.setStrokeWidth(t(2));
        
        mCircleBitmapPaint = new Paint();
        mCircleBitmapPaint.setAntiAlias(true);
        mCircleBitmapPaint.setDither(true);
        mCircleBitmapPaint.setFilterBitmap(true);
        
        mNumberClockBitmapPaint = new Paint();
        mNumberClockBitmapPaint.setAntiAlias(true);
        mNumberClockBitmapPaint.setDither(true);
        mNumberClockBitmapPaint.setFilterBitmap(true);
        
        mWaveBitmapPaint = new Paint();
        mWaveBitmapPaint.setAntiAlias(true);
        mWaveBitmapPaint.setDither(true);
        mWaveBitmapPaint.setFilterBitmap(true);
        
        mDripBitmapPaint = new Paint();
        mDripBitmapPaint.setAntiAlias(true);
        mDripBitmapPaint.setDither(true);
        mDripBitmapPaint.setFilterBitmap(true);
        
        mSprayBitmapPaint = new Paint();
        mSprayBitmapPaint.setAntiAlias(true);
        mSprayBitmapPaint.setDither(true);
        mSprayBitmapPaint.setFilterBitmap(true);
        
        mTime = new Time();
        
        mIsFahrenheit = true; //默认  华氏摄氏度
        
    }

    public void draw(Canvas canvas, int width, int height) {
        // 初始化
        init(width, height);
        // 加载图片
        loadBitmap(width, height);

        mTime.setToNow();       
        
        float centerX = width / 2f;
        float centerY = height / 2f;
        
        //背景
        canvas.drawColor(Color.BLACK);
        
        if (!isRound()) {
            mCirclePaint.setColor(BIG_CIRCLE_COLOR);
            canvas.drawCircle(centerX, centerY, BIG_CIRCLE_RADIUS, mCirclePaint);
        } 
        mCirclePaint.setColor(LITTER_CIRCLE_COLOR);
        canvas.drawCircle(centerX, centerY, LITTLE_CIRCLE_RADIUS, mCirclePaint);
        
        if (isInAmbientMode()) {
        	if (isLowBitAmbient()) {
        		canvas.save();
        		canvas.translate(AMBIENT_NUMBER_CLOCK_LEFT, AMBIENT_NUMBER_CLOCK_TOP);
        		drawNumberClock(canvas);
        		canvas.restore();
        	} else {
        		canvas.save();
        		canvas.translate(AMBIENT_NUMBER_CLOCK_LEFT, AMBIENT_NUMBER_CLOCK_TOP);
        		drawNumberClock(canvas);
        		canvas.restore();
        	}
        } else {
        	// 开启水波移动动画
            startWaveMoveAnimator();
        	
        	//数字时钟
            boolean showColon = (System.currentTimeMillis() / 500) % 2 == 1; //冒号闪动效果
        	float scaleRate = 59 * 1f / 66; //ambient模式时钟中数字和正常模式时钟中数字的高度比例
        	canvas.save();        	
        	canvas.translate(NUMBER_CLOCK_LEFT, NUMBER_CLOCK_TOP);
        	canvas.scale(scaleRate, scaleRate, 0, 0);
        	drawNumberClock(canvas, showColon);
        	canvas.restore();
        	
        	//日期
        	int monthTens = (mTime.month + 1) / 10;
        	int monthUnits = (mTime.month + 1) % 10;
        	int monthDayTens = mTime.monthDay / 10;
        	int monthDayUnits = mTime.monthDay % 10;
        	int weekDay = mTime.weekDay;
        	//绘制数字
        	canvas.save();
            scaleRate = 20 * 1f / 66;
        	canvas.translate(DATE_LEFT, DATE_TOP);
        	int numberBitmapWidth = mAmbientNumberBitmapArray[0].getWidth();
        	float left1 = 0;
        	float left2 = left1 + (numberBitmapWidth + AMBIENT_PADDING_BETWEEN_NUMBER) * scaleRate;
        	float left3 = left2 + (numberBitmapWidth + t(36)) * scaleRate;
        	float left4 = left3 + (numberBitmapWidth + AMBIENT_PADDING_BETWEEN_NUMBER) * scaleRate;
        	canvas.drawBitmap(mDateNumberBitmapArray[monthTens], left1, 0, mNumberClockBitmapPaint);
        	canvas.drawBitmap(mDateNumberBitmapArray[monthUnits], left2, 0, mNumberClockBitmapPaint);
        	canvas.drawBitmap(mDateNumberBitmapArray[monthDayTens], left3, 0, mNumberClockBitmapPaint);
        	canvas.drawBitmap(mDateNumberBitmapArray[monthDayUnits], left4, 0, mNumberClockBitmapPaint);
            canvas.restore();
            //斜杠
            canvas.drawBitmap(mSlashBitmap, DATE_SLASH_LEFT, DATE_SLASH_TOP, mNumberClockBitmapPaint);
            //星期
            canvas.drawBitmap(mWeekdayBitmapArray[weekDay], DATE_WEEKDAY_LEFT, DATE_WEEKDAY_TOP, mNumberClockBitmapPaint);
            
            //绘制天气
            if (mWeatherIconResId > 0 && mWeatherTemp != Integer.MAX_VALUE && mWeatherIconBitmap != null) {
            	boolean isPositive = mWeatherTemp > 0;
            	
            	int abWeatherTemp = (mWeatherTemp > 0) ? mWeatherTemp: -mWeatherTemp; //celsius
            	if (mIsFahrenheit) {//Fahrenheit
            		abWeatherTemp = (int) (32 + abWeatherTemp * 1.8);
            	}
	            int weatherTempTens = abWeatherTemp / 10;
	            int weatherTempUnits = abWeatherTemp % 10;
	            canvas.save();
	            scaleRate = 27 * 1f / 66;
	            canvas.translate(WEATHER_AREA_LEFT, WEATHER_AREA_TOP);
	            canvas.scale(scaleRate, scaleRate);
	            if (weatherTempTens != 0) {
	                canvas.drawBitmap(mAmbientNumberBitmapArray[weatherTempTens], 0, 0, mNumberClockBitmapPaint);
	            }
	            float left = numberBitmapWidth + AMBIENT_PADDING_BETWEEN_NUMBER;
	            canvas.drawBitmap(mAmbientNumberBitmapArray[weatherTempUnits], left, 0, mNumberClockBitmapPaint);
	            canvas.restore();
	            canvas.drawBitmap(mWeatherIconBitmap, WEATHER_ICON_LEFT, WEATHER_AREA_TOP, mNumberClockBitmapPaint);
	            if (mIsFahrenheit) {
	            	canvas.drawBitmap(mFahrenheitIconBitmap, WEATHER_CELSIUS_LEFT, WEATHER_AREA_TOP, mNumberClockBitmapPaint);
	            } else {
	            	canvas.drawBitmap(mCelsiusIconBitmap, WEATHER_CELSIUS_LEFT, WEATHER_AREA_TOP, mNumberClockBitmapPaint);
	            }
	            if (!isPositive) {
	            	//气温零下，绘制“-”号
	            	left = WEATHER_MINUS_LEFT + ((weatherTempTens != 0) ? 0: numberBitmapWidth);
	            	float top = WEATHER_AREA_TOP + (mAmbientNumberBitmapArray[0].getHeight() - mMinusIconBitmap.getHeight()) / 2f;
	            	canvas.drawBitmap(mMinusIconBitmap, left, top, mNumberClockBitmapPaint);
	            }           
            }
            
            mWaveHeight = (mTime.minute + mTime.second / 60f) / 60f * (mWave1Bitmap.getHeight() - WAVE_START_HEIGHT) + WAVE_START_HEIGHT;
            //水波
            int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
    	    canvas.drawBitmap(mWave1Bitmap, -mWave1Left, height - mWaveHeight, mWaveBitmapPaint);
    	    canvas.drawBitmap(mWave2Bitmap, -(mWave2Bitmap.getWidth() / 2f) + mWave2Left, height - mWaveHeight, mWaveBitmapPaint);
    	    mWaveBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    	    canvas.drawBitmap(mMaskBitmap, 0, 0, mWaveBitmapPaint);
    	    mWaveBitmapPaint.setXfermode(null);
    	    canvas.restoreToCount(sc);
            
            if (mWaveHeight < WAVE_HEIGHT_NOT_DRIPPING) {
	            float crashY = height - mWaveHeight + DRIP_WAVE_CRASH_POINT_TOP;
	            if (mLastDrippingTime == 0) {
	            	mLastDrippingTime = System.currentTimeMillis();
	            	startDrippingAnimator(crashY);
	            } else {
	            	long diffTime = System.currentTimeMillis() - mLastDrippingTime;
		        	if (diffTime > DRIPPING_INTERVAL || diffTime < 0) {
	            		mLastDrippingTime = System.currentTimeMillis();
	            		Log.i(TAG, "onDraw diffTime: " + diffTime + ", mLastDrippingTime: " + mLastDrippingTime);
	            		startDrippingAnimator(crashY);
	            	}
	            }
	            //水滴
	    	    mDripBitmapPaint.setAlpha(mDripAlpha);
	    	    canvas.drawBitmap(mDripBitmap, (width - mDripBitmap.getWidth()) / 2f, mDripHeight, mDripBitmapPaint);
	    	    //水花
	    	    mSprayBitmapPaint.setAlpha(mSprayAlpha);
	    	    float sprayTop = crashY - SPRAY_HEIGHT_ABOVE_CRASH_POINT - mSprayBitmap.getHeight(); 
	    	    canvas.save();
	    	    canvas.scale(mSprayScaleRate, mSprayScaleRate, width / 2f, crashY - SPRAY_HEIGHT_ABOVE_CRASH_POINT);
	    	    canvas.drawBitmap(mSprayBitmap, (width - mSprayBitmap.getWidth()) / 2f, sprayTop, mSprayBitmapPaint);
	    	    canvas.restore();
            } 	    
        }
    }
    
    private void drawNumberClock(Canvas canvas) {
    	drawNumberClock(canvas, true);
    }
    
    //绘制数字时钟
    private void drawNumberClock(Canvas canvas, boolean showColon) {
    	boolean isPm = (mTime.hour / 12) == 1;
    	int hour = ((mTime.hour % 12) == 0) ? 12 : mTime.hour % 12;
        int hourTens = hour / 10;
        int hourUnits = hour % 10;
        int minuteTens = mTime.minute / 10;
        int minuteUnits = mTime.minute % 10;
    	
    	int numberBitmapWidth = mAmbientNumberBitmapArray[0].getWidth();
		int left1 = 0;
		int left2 = left1 + AMBIENT_PADDING_BETWEEN_NUMBER + numberBitmapWidth;
		int left3 = left2 + AMBIENT_PADDING_BETWEEN_NUMBER_COLON + numberBitmapWidth;
		int left4 = left3 + AMBIENT_PADDING_BETWEEN_NUMBER_COLON + mAmbientColonBitmap.getWidth();
		int left5 = left4 + AMBIENT_PADDING_BETWEEN_NUMBER + numberBitmapWidth;
		int left6 = left5 + AMBIENT_PADDING_BETWEEN_NUMBER_PM + numberBitmapWidth;
		int left7 = left6 + mLetterABitmap.getWidth();
		canvas.drawBitmap(mAmbientNumberBitmapArray[hourTens], left1, 0, mNumberClockBitmapPaint);     		
		canvas.drawBitmap(mAmbientNumberBitmapArray[hourUnits], left2, 0, mNumberClockBitmapPaint);
		canvas.drawBitmap(mAmbientNumberBitmapArray[minuteTens], left4, 0, mNumberClockBitmapPaint);       		
		canvas.drawBitmap(mAmbientNumberBitmapArray[minuteUnits], left5, 0, mNumberClockBitmapPaint);        		
		canvas.drawBitmap(isPm ? mLetterPBitmap: mLetterABitmap, left6, AMBIENT_PM_TOP_TO_NUMBER, mNumberClockBitmapPaint);        		
		canvas.drawBitmap(mLetterMBitmap, left7, AMBIENT_PM_TOP_TO_NUMBER, mNumberClockBitmapPaint);
		if (showColon) {
		    canvas.drawBitmap(mAmbientColonBitmap, left3, AMBIENT_COLON_TOP_TO_NUMBER, mNumberClockBitmapPaint);        	
		}
    }
    
    /**
     * 启动水波荡漾动画
     */
    private void startWaveMoveAnimator() {
    	startWave1MoveAnimator();
    	startWave2MoveAnimator();
    }
    
    private void startWave1MoveAnimator() {
    	if (mWave1MoveAnimator != null && mWave1MoveAnimator.isStarted()) {
    		return;
    	}
    	
    	mWave1MoveAnimator = ValueAnimator.ofFloat(0, mWave1Bitmap.getWidth() / 2f);
    	mWave1MoveAnimator.setInterpolator(new LinearInterpolator());
    	mWave1MoveAnimator.setDuration(WAVE1_SINGLE_MOVE_DURATION);
    	mWave1MoveAnimator.setRepeatCount(Animation.INFINITE);
    	mWave1MoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mWave1Left = (Float) animation.getAnimatedValue();
			}
		});
    	mWave1MoveAnimator.start();
    }
    
    private void startWave2MoveAnimator() {
    	if (mWave2MoveAnimator != null && mWave2MoveAnimator.isStarted()) {
    		return;
    	}
    	
    	mWave2MoveAnimator = ValueAnimator.ofFloat(0, mWave2Bitmap.getWidth() / 2f);
    	mWave2MoveAnimator.setInterpolator(new LinearInterpolator());
    	mWave2MoveAnimator.setDuration(WAVE2_SINGLE_MOVE_DURATION);
    	mWave2MoveAnimator.setRepeatCount(Animation.INFINITE);
    	mWave2MoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mWave2Left = (Float) animation.getAnimatedValue();
			}
		});
    	mWave2MoveAnimator.start();
    }
    
    private void stopWaveMoveAnimator() {
    	if (mWave1MoveAnimator != null) {
    		mWave1MoveAnimator.cancel();
        }
    	if (mWave2MoveAnimator != null) {
    		mWave2MoveAnimator.cancel();
    	}
    }
    
    /**
     * 水滴下落过程的中一系列动画
     * @param crashY 水滴入水点的y轴坐标
     */
    private void startDrippingAnimator(float crashY) {
    	Log.i(TAG, "createAnimation crashY: " + crashY);
    	if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
    		return;
    	}
    	mAnimatorSet = new AnimatorSet();
    	
    	//水滴下落
    	ValueAnimator dropAnimator = ValueAnimator.ofFloat(0, crashY);
    	dropAnimator.setInterpolator(new AccelerateInterpolator(DRIPPING_ACCELERATION_FACTOR));
    	//计算下落时间
    	long duration = (long) (Math.pow(crashY / (mHeight - WAVE_START_HEIGHT + DRIP_WAVE_CRASH_POINT_TOP), 
    			               1f / (2 * DRIPPING_ACCELERATION_FACTOR)) * DRIPPING_DURATION);
    	dropAnimator.setDuration(duration);
    	dropAnimator.setRepeatCount(0);
    	dropAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {	
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mDripHeight = (Float) animation.getAnimatedValue();
			}
		});
    	
    	//水滴淡入
    	ValueAnimator fadeInAnimator = ValueAnimator.ofInt(0, 255);
    	fadeInAnimator.setInterpolator(new LinearInterpolator());
    	fadeInAnimator.setDuration(DRIP_FADE_IN_DURATION);
    	fadeInAnimator.setRepeatCount(0);
    	fadeInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mDripAlpha = (Integer) animation.getAnimatedValue();				
			}
		});
    	
    	//水滴下落
    	ValueAnimator dropInWaterAnimator = ValueAnimator.ofFloat(crashY, crashY + DRIPPING_DISTANCE_IN_WATER);
    	dropInWaterAnimator.setInterpolator(new DecelerateInterpolator(DRIPPING_ACCELERATION_FACTOR));
    	dropInWaterAnimator.setDuration(DRIPPING_IN_WATER_DURATION);
    	dropInWaterAnimator.setRepeatCount(0);
    	dropInWaterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {	
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mDripHeight = (Float) animation.getAnimatedValue();
			}
		});
    	
    	//水滴淡出
    	ValueAnimator fadeOutAnimator = ValueAnimator.ofInt(255, 0);
    	fadeOutAnimator.setInterpolator(new LinearInterpolator());
    	fadeOutAnimator.setDuration(DRIP_FADE_OUT_DURATION);
    	fadeOutAnimator.setRepeatCount(0);
    	fadeOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mDripAlpha = (Integer) animation.getAnimatedValue();				
			}
		});
    	
    	//水花淡入
    	ValueAnimator sprayFadeInAnimator = ValueAnimator.ofInt(0, 255);
    	sprayFadeInAnimator.setInterpolator(new LinearInterpolator());
    	sprayFadeInAnimator.setDuration(SPRAY_FADE_IN_DURATION);
    	sprayFadeInAnimator.setRepeatCount(0);
    	sprayFadeInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    		@Override
    		public void onAnimationUpdate(ValueAnimator animation) {
    			mSprayAlpha = (Integer) animation.getAnimatedValue();
    		}
    	});
    	
    	//水花淡出
    	ValueAnimator sprayFadeOutAnimator = ValueAnimator.ofInt(255, 0);
    	sprayFadeOutAnimator.setInterpolator(new LinearInterpolator());
    	sprayFadeOutAnimator.setDuration(SPRAY_FADE_OUT_DURATION);
    	sprayFadeOutAnimator.setRepeatCount(0);
    	sprayFadeOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    		@Override
    		public void onAnimationUpdate(ValueAnimator animation) {
    			mSprayAlpha = (Integer) animation.getAnimatedValue();
    		}
    	});
    			
    	//水花透明度为255
    	ValueAnimator sprayStayAnimator = ValueAnimator.ofInt(255, 255);
    	long sprayStayDuration = SPRAY_TOTAL_DURATION - SPRAY_FADE_IN_DURATION - SPRAY_FADE_OUT_DURATION;
    	sprayFadeOutAnimator.setDuration(sprayStayDuration);
    	sprayFadeOutAnimator.setRepeatCount(0);
    	
    	//水花放大
    	ValueAnimator sprayScaleAnimator = ValueAnimator.ofFloat(0.1f, 1.5f);
    	sprayScaleAnimator.setInterpolator(new DecelerateInterpolator());
    	sprayScaleAnimator.setDuration(SPRAY_TOTAL_DURATION);
    	sprayScaleAnimator.setRepeatCount(0);
    	sprayScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {	
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mSprayScaleRate = (Float) animation.getAnimatedValue();
			}
		});	
    	
    	mAnimatorSet.play(dropAnimator).with(fadeInAnimator).before(dropInWaterAnimator);
    	mAnimatorSet.play(dropInWaterAnimator).with(fadeOutAnimator).with(sprayFadeInAnimator).with(sprayScaleAnimator);
    	mAnimatorSet.play(sprayFadeInAnimator).before(sprayStayAnimator).before(sprayFadeOutAnimator);

    	mAnimatorSet.start();
    }
    
    private void stopDrippingAnimator() {
    	if (mAnimatorSet != null) {
    		mAnimatorSet.end();
    	}
    }
    
    private Bitmap createCircleBitmap(int w, int h) {
    	Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(bm);
    	Paint paint = new Paint();
    	paint.setAntiAlias(true);
    	canvas.drawCircle(w / 2f, h / 2f, w / 2f, paint);
    	return bm;
    }

    private int getTextDeltaY(Paint paint) {
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int textDeltaY = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        return textDeltaY;
    }

    private Bitmap getScaledBitmap(Bitmap bm, float rate) {
        if (rate == 1) {
            return bm;
        } else {
            int scaledWidth = (int) (bm.getWidth() * rate);
            int scaledHeight = (int) (bm.getHeight() * rate);
            Log.i(TAG, "getScaledBitmap scaledWidth:" + scaledWidth + ";scaledHeight:" + scaledHeight + ";rate:" + rate);
            return Bitmap.createScaledBitmap(bm, scaledWidth, scaledHeight, true);
        }
    }

    private Bitmap getScaledBitmap(int drawableResId, float rate) {
        Drawable drawable = mContext.getResources().getDrawable(drawableResId);
        Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
        return getScaledBitmap(bm, rate);
    }
    
    private Bitmap combineBitmap(int resId) {
    	Bitmap waveBitmap = getScaledBitmap(resId, mScaleRate);
        Bitmap bm = Bitmap.createBitmap(2 * waveBitmap.getWidth(), waveBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(bm);
    	Paint paint = new Paint();
    	paint.setAntiAlias(true);
    	paint.setDither(true);
    	paint.setFilterBitmap(true);
    	canvas.drawBitmap(waveBitmap, 0, 0, paint);
    	canvas.drawBitmap(waveBitmap, waveBitmap.getWidth(), 0, paint);
    	return bm;
    }

    private void loadBitmap(int width, int height) {
        // NOTE: 这里width和height是一样大的（不管是方形还是圆形的表）
        // TODO: 如果有不规则的手机尺寸，再做处理
        mScaleRate = 1; // 缩放比例
        
        if (!mHasInitScaleRate) {
        	Drawable drawable = mContext.getResources().getDrawable(R.drawable.round_preview);
            Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
            // Log.i(TAG, "loadBitmap before scaled:" + bm.getWidth() + "x" + bm.getHeight());
            mScaleRate = width * 1f / bm.getWidth();
        	mHasInitScaleRate = true;
        }
        
        if (mAmbientColonBitmap == null) {
        	mAmbientColonBitmap = getScaledBitmap(R.drawable.colon, mScaleRate);
        }
        
        for (int i = 0; i < 10; i++) {
        	if (mAmbientNumberBitmapArray[i] == null) {
        		mAmbientNumberBitmapArray[i] = getScaledBitmap(NUMBER_BITMAP_IDS[i], mScaleRate);
        	}
        }
        
        for (int i= 0; i < 10; i++) {
        	if (mDateNumberBitmapArray[i] == null) {
        		mDateNumberBitmapArray[i] = getScaledBitmap(DATE_NUMBER_BITMAP_IDS[i], mScaleRate);
        	}
        }
        
        if (mLetterABitmap == null) {
        	mLetterABitmap = getScaledBitmap(R.drawable.letter_a, mScaleRate);
        }
        
        if (mLetterPBitmap == null) {
        	mLetterPBitmap = getScaledBitmap(R.drawable.letter_p, mScaleRate);
        }
        
        if (mLetterMBitmap == null) {
        	mLetterMBitmap = getScaledBitmap(R.drawable.letter_m, mScaleRate);
        }
        
        if (mSlashBitmap == null) {
        	mSlashBitmap = getScaledBitmap(R.drawable.slash, mScaleRate);
        }
        
        for (int i = 0; i < 7; i++) {
        	if (mWeekdayBitmapArray[i] == null) {
        		mWeekdayBitmapArray[i] = getScaledBitmap(WEEKDAY_BITMAP_IDS[i], mScaleRate);
        	}
        }
        
        if (mWeatherIconBitmap == null) {
        	if (mWeatherIconResId > 0) {
        		try {
        		    mWeatherIconBitmap = getScaledBitmap(mWeatherIconResId, mScaleRate);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        }
        
        if (mCelsiusIconBitmap == null) {
        	mCelsiusIconBitmap = getScaledBitmap(R.drawable.celsius_icon, mScaleRate);
        }
        
        if (mFahrenheitIconBitmap == null) {
        	mFahrenheitIconBitmap = getScaledBitmap(R.drawable.fahrenheit_icon, mScaleRate);
        }
        
        if (mMinusIconBitmap == null) {
        	mMinusIconBitmap = getScaledBitmap(R.drawable.minus, mScaleRate);
        }
        
        if (mWave1Bitmap == null) {
        	mWave1Bitmap = combineBitmap(R.drawable.wave1);
        }
        
        if (mWave2Bitmap == null) {
        	mWave2Bitmap = combineBitmap(R.drawable.wave2);
        }
        
        if (mMaskBitmap == null) {
        	mMaskBitmap = createCircleBitmap(mWidth, mHeight);
        }
        
        if (mDripBitmap == null) {
        	mDripBitmap = getScaledBitmap(R.drawable.drip, mScaleRate);
        }
        
        if (mSprayBitmap == null) {
        	mSprayBitmap = getScaledBitmap(R.drawable.spray, mScaleRate);
        }
    }
    
    public void setTimeZone(String timeZone) {
    	mTime.clear(timeZone);
    	mTime.setToNow();
    }
    
    public boolean isInAmbientMode() {
    	return mIsInAmbientMode;
    }

    public void setInAmbientMode(boolean inAmbientMode) {
    	mIsInAmbientMode = inAmbientMode;
    	if (inAmbientMode) {
    		stopDrippingAnimator();
    	    stopWaveMoveAnimator();
    	}
    }
    
    public boolean isLowBitAmbient() {
    	return mIsLowBitAmbient;
    }
    
    public void setIsLowBitAmbient(boolean isLowBitAmbient) {
    	mIsLowBitAmbient = isLowBitAmbient;
    }
    
    public boolean isRound() {
    	return mIsRound;
    }
    
    public void setIsRound(boolean isRound) {
    	mIsRound = isRound;
    }
    
    public void setWeatherTemp(int weatherTemp) {
    	mWeatherTemp = weatherTemp;
    	Log.i(TAG, "setWeatherTemp weatherTemp: " + weatherTemp);
    }
    
    public void setWeatherIcon(String weatherIcon) {
    	Log.i(TAG, "setWeatherTemp weatherIcon: " + weatherIcon);
    	int currentWeatherIconResId = getWeatherIconResId(weatherIcon);
    	if (currentWeatherIconResId != mWeatherIconResId) {
    		mWeatherIconBitmap = null;
    	}
    	mWeatherIcon = weatherIcon;
    	mWeatherIconResId = currentWeatherIconResId;
    }

    /**
     * 天气icon资源id
     * @param weatherIcon
     * @return
     */
	private int getWeatherIconResId(String weatherIcon) {
		if (TextUtils.isEmpty(weatherIcon)) {
            return -1;
        }

        if (weatherIcon.equals("01d") || weatherIcon.equals("01n")) {
            return R.drawable.w01d;
        }

        if (weatherIcon.equals("02d") || weatherIcon.equals("02n")) {
            return R.drawable.w02d;
        }

        if (weatherIcon.equals("03d") || weatherIcon.equals("03n")) {
            return R.drawable.w03d;
        }

        if (weatherIcon.equals("04d") || weatherIcon.equals("04n")) {
            return R.drawable.w04d;
        }

        if (weatherIcon.equals("09d") || weatherIcon.equals("09n")) {
            return R.drawable.w09d;
        }

        if (weatherIcon.equals("10d") || weatherIcon.equals("10n")) {
            return R.drawable.w10d;
        }

        if (weatherIcon.equals("11d") || weatherIcon.equals("11n")) {
            return R.drawable.w11d;
        }

        if (weatherIcon.equals("13d") || weatherIcon.equals("13n")) {
            return R.drawable.w13d;
        }

        if (weatherIcon.equals("50d") || weatherIcon.equals("50n")) {
            return R.drawable.w50d;
        }

        return -1;
	}
	
	public void setFahrenheit(boolean isFahrenheit) {
		mIsFahrenheit = isFahrenheit;
	}
}
