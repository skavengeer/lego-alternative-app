package com.dino.smart.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.internal.view.SupportMenu;

import com.dino.smart.R;

public class JoystickForView extends View implements Runnable {
    public static final int BOTTOM = 7;
    public static final int BOTTOM_LEFT = 8;
    public static final long DEFAULT_LOOP_INTERVAL = 100;
    public static final int FRONT = 3;
    public static final int FRONT_RIGHT = 4;
    public static final int LEFT = 1;
    public static final int LEFT_FRONT = 2;
    public static final int RIGHT = 5;
    public static final int RIGHT_BOTTOM = 6;
    private final double RAD;
    Bitmap bgBitmap;
    Bitmap btnBitmap;
    private Paint button;
    private int buttonRadius;
    private double centerX;
    private double centerY;
    public boolean isMoveX;
    public boolean isMoveY;
    private int joystickRadius;
    private int lastAngle;
    private int lastPower;
    private long loopInterval;
    Context mContext;
    private Paint mainCircle;
    public boolean nomove;
    private OnJoystickMoveListener onJoystickMoveListener;
    private Thread thread;
    private int xPosition;
    private int yPosition;

    public interface OnJoystickMoveListener {
        void onValueChanged(int i, int i2, int i3);
    }

    public JoystickForView(Context context) {
        super(context);
        this.RAD = 57.2957795d;
        this.thread = new Thread(this);
        this.loopInterval = 100L;
        this.xPosition = 0;
        this.yPosition = 0;
        this.centerX = 0.0d;
        this.centerY = 0.0d;
        this.lastAngle = 0;
        this.lastPower = 0;
        this.isMoveX = true;
        this.isMoveY = true;
        this.nomove = false;
    }

    public JoystickForView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.RAD = 57.2957795d;
        this.thread = new Thread(this);
        this.loopInterval = 100L;
        this.xPosition = 0;
        this.yPosition = 0;
        this.centerX = 0.0d;
        this.centerY = 0.0d;
        this.lastAngle = 0;
        this.lastPower = 0;
        this.isMoveX = true;
        this.isMoveY = true;
        this.nomove = false;
        this.mContext = context;
        initJoystickView();
    }

    public JoystickForView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.RAD = 57.2957795d;
        this.thread = new Thread(this);
        this.loopInterval = 100L;
        this.xPosition = 0;
        this.yPosition = 0;
        this.centerX = 0.0d;
        this.centerY = 0.0d;
        this.lastAngle = 0;
        this.lastPower = 0;
        this.isMoveX = true;
        this.isMoveY = true;
        this.nomove = false;
        initJoystickView();
    }

    protected void initJoystickView() {
        this.bgBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_p1)).getBitmap();
        this.btnBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_tpab)).getBitmap();
        Paint paint = new Paint(1);
        this.mainCircle = paint;
        paint.setColor(-1);
        this.mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint paint2 = new Paint(1);
        this.button = paint2;
        paint2.setColor(-65536);
        this.button.setStyle(Paint.Style.FILL);
    }

    public void initMoveRes() {
        this.bgBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_p1)).getBitmap();
        this.btnBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_c2)).getBitmap();
    }

    public void initMoveLeft() {
        this.isMoveX = false;
        this.bgBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_p3)).getBitmap();
        this.btnBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_tpc)).getBitmap();
    }

    public void initMoveRight() {
        this.isMoveY = false;
        this.bgBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_p4)).getBitmap();
        this.btnBitmap = ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.joy_tpc)).getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.xPosition == 0) {
            this.xPosition = getWidth() / 2;
            this.yPosition = getHeight() / 2;
            this.buttonRadius = (int) ((getHeight() / 2) * 0.5d);
            this.joystickRadius = getHeight() / 2;
        }
        this.centerX = getWidth() / 2;
        this.centerY = getHeight() / 2;
        Rect rect = new Rect(0, 0, this.bgBitmap.getWidth(), this.bgBitmap.getHeight());
        int i = this.joystickRadius;
        canvas.drawBitmap(this.bgBitmap, rect, new RectF(0.0f, 0.0f, i * 2, i * 2), (Paint) null);
        float width = ((getWidth() * 1.0f) / this.btnBitmap.getWidth()) * 0.4f;
        final int width2 = (int)(this.btnBitmap.getWidth() * width);
        final int width3 = (int)(this.btnBitmap.getHeight() * width);
        final int width4 = this.xPosition - width2 / 2;
        final int width5 = this.yPosition - width3 / 2;
        canvas.drawBitmap(this.btnBitmap, new Rect(0, 0, this.btnBitmap.getWidth(), this.btnBitmap.getHeight()), new RectF((float)width4,(float)width5,(float)(width2+width4),(float)(width3+width5)), (Paint) null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.nomove) {
            return true;
        }
        if (this.isMoveX) {
            this.xPosition = (int) motionEvent.getX();
        }
        if (this.isMoveY) {
            this.yPosition = (int) motionEvent.getY();
        }
        int i = this.xPosition;
        double d = this.centerX;
        double d2 = (i - d) * (i - d);
        int i2 = this.yPosition;
        double d3 = this.centerY;
        double dSqrt = Math.sqrt(d2 + ((i2 - d3) * (i2 - d3)));
        int i3 = this.joystickRadius;
        int i4 = this.buttonRadius;
        if (dSqrt > i3 - i4) {
            double d4 = this.xPosition;
            double d5 = this.centerX;
            this.xPosition = (int) ((((d4 - d5) * (i3 - i4)) / dSqrt) + d5);
            double d6 = this.yPosition;
            double d7 = this.centerY;
            this.yPosition = (int) ((((d6 - d7) * (i3 - i4)) / dSqrt) + d7);
        }
        if (motionEvent.getAction() == 1) {
            this.xPosition = (int) this.centerX;
            this.yPosition = (int) this.centerY;
        }
        OnJoystickMoveListener onJoystickMoveListener = this.onJoystickMoveListener;
        if (onJoystickMoveListener != null) {
            onJoystickMoveListener.onValueChanged(getAngle(), getPower(), getDirection());
        }
        invalidate();
        return true;
    }

    public int getAngle() {
        int i = this.xPosition;
        double d = i;
        double d2 = this.centerX;
        if (d > d2) {
            int i2 = this.yPosition;
            double d3 = i2;
            double d4 = this.centerY;
            if (d3 < d4) {
                int iAtan = (int) ((Math.atan((i2 - d4) / (i - d2)) * 57.2957795d) + 90.0d);
                this.lastAngle = iAtan;
                return iAtan;
            }
            if (i2 > d4) {
                int iAtan2 = ((int) (Math.atan((i2 - d4) / (i - d2)) * 57.2957795d)) + 90;
                this.lastAngle = iAtan2;
                return iAtan2;
            }
            this.lastAngle = 90;
            return 90;
        }
        if (i >= d2) {
            if (this.yPosition <= this.centerY) {
                this.lastAngle = 0;
                return 0;
            }
            if (this.lastAngle < 0) {
                this.lastAngle = -180;
                return -180;
            }
            this.lastAngle = 180;
            return 180;
        }
        int i3 = this.yPosition;
        double d5 = i3;
        double d6 = this.centerY;
        if (d5 < d6) {
            int iAtan3 = (int) ((Math.atan((i3 - d6) / (i - d2)) * 57.2957795d) - 90.0d);
            this.lastAngle = iAtan3;
            return iAtan3;
        }
        if (i3 > d6) {
            int iAtan4 = ((int) (Math.atan((i3 - d6) / (i - d2)) * 57.2957795d)) - 90;
            this.lastAngle = iAtan4;
            return iAtan4;
        }
        this.lastAngle = -90;
        return -90;
    }

    public int getPower() {
        int i = this.xPosition;
        double d = this.centerX;
        double d2 = (i - d) * (i - d);
        int i2 = this.yPosition;
        double d3 = this.centerY;
        return (int) ((Math.sqrt(d2 + ((i2 - d3) * (i2 - d3))) * 100.0d) / this.joystickRadius);
    }

    public int getDirection() {
        int i = 0;
        if (this.lastPower == 0 && this.lastAngle == 0) {
            return 0;
        }
        int i2 = this.lastAngle;
        if (i2 <= 0) {
            i = (i2 * (-1)) + 90;
        } else if (i2 > 0) {
            i = i2 <= 90 ? 90 - i2 : 360 - (i2 - 90);
        }
        int i3 = ((i + 22) / 45) + 1;
        if (i3 > 8) {
            return 1;
        }
        return i3;
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener onJoystickMoveListener, long j) {
        this.onJoystickMoveListener = onJoystickMoveListener;
        this.loopInterval = j;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (JoystickForView.this.onJoystickMoveListener != null) {
                        JoystickForView.this.onJoystickMoveListener.onValueChanged(JoystickForView.this.getAngle(), JoystickForView.this.getPower(), JoystickForView.this.getDirection());
                    }
                }
            });
            try {
                Thread.sleep(this.loopInterval);
            } catch (InterruptedException unused) {
                return;
            }
        }
    }
}
