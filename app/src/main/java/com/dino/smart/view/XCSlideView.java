package com.dino.smart.view;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.dino.smart.DensityUtil;

public class XCSlideView extends RelativeLayout {
    private Activity mActivity;
    private Context mContext;
    private int mDuration;
    private boolean mIsMoving;
    private View mMaskView;
    private View mMenuView;
    private int mMenuWidth;
    private Positon mPositon;
    private int mScreenWidth;
    private Scroller mScroller;
    private boolean mShow;

    public enum Positon {
        LEFT,
        RIGHT
    }

    public XCSlideView(Context context) {
        this(context, null);
    }

    public XCSlideView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public XCSlideView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScroller = null;
        this.mMenuWidth = 0;
        this.mScreenWidth = 0;
        this.mIsMoving = false;
        this.mShow = false;
        this.mDuration = 600;
        this.mPositon = Positon.LEFT;
        init(context);
    }

    public static XCSlideView create(Activity activity) {
        return new XCSlideView(activity);
    }

    public static XCSlideView create(Activity activity, Positon positon) {
        XCSlideView xCSlideView = new XCSlideView(activity);
        xCSlideView.mPositon = positon;
        return xCSlideView;
    }

    private void init(Context context) {
        this.mContext = context;
        setDescendantFocusability(262144);
        setFocusable(true);
        this.mScroller = new Scroller(context);
        int i = DensityUtil.getScreenWidthAndHeight(context)[0];
        this.mScreenWidth = i;
        this.mMenuWidth = (i * 7) / 9;
        attachToContentView((Activity) context, this.mPositon);
    }

    private void attachToContentView(Activity activity, Positon positon) {
        this.mPositon = positon;
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity.findViewById(R.id.content)).getChildAt(0);
        View view = new View(activity);
        this.mMaskView = view;
        view.setBackgroundColor(this.mContext.getResources().getColor(com.dino.smart.R.color.mask_color));
        viewGroup.addView(this.mMaskView, viewGroup.getLayoutParams());
        this.mMaskView.setVisibility(View.GONE);
        this.mMaskView.setClickable(true);
        this.mMaskView.setOnClickListener(new OnClickListener() { // from class: com.tyb.smart.view.XCSlideView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (XCSlideView.this.isShow()) {
                    XCSlideView.this.dismiss();
                }
            }
        });
    }

    public void setMenuWidth(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = i;
        this.mMenuWidth = i;
        setLayoutParams(layoutParams);
    }

    public void setMenuView(Activity activity, View view) {
        this.mActivity = activity;
        this.mMenuView = view;
        addView(this.mMenuView, new LayoutParams(-1, -1));
        this.mMenuView.post(new Runnable() { // from class: com.tyb.smart.view.XCSlideView.2
            @Override // java.lang.Runnable
            public void run() {
                XCSlideView xCSlideView = XCSlideView.this;
                xCSlideView.mMenuWidth = xCSlideView.mMenuView.getWidth();
                int i = AnonymousClass3.$SwitchMap$com$tyb$smart$view$XCSlideView$Positon[XCSlideView.this.mPositon.ordinal()];
                if (i == 1) {
                    XCSlideView xCSlideView2 = XCSlideView.this;
                    xCSlideView2.scrollTo(xCSlideView2.mScreenWidth, 0);
                } else {
                    if (i != 2) {
                        return;
                    }
                    XCSlideView xCSlideView3 = XCSlideView.this;
                    xCSlideView3.scrollTo(-xCSlideView3.mScreenWidth, 0);
                }
            }
        });
        ((ViewGroup) activity.findViewById(R.id.content)).addView(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        int i = AnonymousClass3.$SwitchMap$com$tyb$smart$view$XCSlideView$Positon[this.mPositon.ordinal()];
        if (i == 1) {
            layoutParams.gravity = 3;
            layoutParams.leftMargin = 0;
        } else if (i == 2) {
            layoutParams.gravity = 5;
            layoutParams.rightMargin = 0;
        }
        if (((TextView) activity.findViewById(R.id.title)) != null) {
            try {
                layoutParams.topMargin = DensityUtil.getStatusBarHeight(this.mContext);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if ((this.mActivity.getWindow().getAttributes().flags & 67108864) == 67108864) {
            try {
                layoutParams.topMargin = DensityUtil.getStatusBarHeight(this.mContext);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        setLayoutParams(layoutParams);
    }

    /* renamed from: com.tyb.smart.view.XCSlideView$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$tyb$smart$view$XCSlideView$Positon;

        static {
            int[] iArr = new int[Positon.values().length];
            $SwitchMap$com$tyb$smart$view$XCSlideView$Positon = iArr;
            try {
                iArr[Positon.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$tyb$smart$view$XCSlideView$Positon[Positon.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && isShow()) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void show() {
        if (!isShow() || this.mIsMoving) {
            int i = AnonymousClass3.$SwitchMap$com$tyb$smart$view$XCSlideView$Positon[this.mPositon.ordinal()];
            if (i == 1) {
                int i2 = this.mMenuWidth;
                startScroll(i2, -i2, this.mDuration);
            } else if (i == 2) {
                int i3 = this.mMenuWidth;
                startScroll(-i3, i3, this.mDuration);
            }
            switchMaskView(true);
            this.mShow = true;
        }
    }

    private void switchMaskView(boolean z) {
        if (z) {
            this.mMaskView.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(this.mDuration);
            this.mMaskView.startAnimation(alphaAnimation);
            return;
        }
        this.mMaskView.setVisibility(View.GONE);
    }

    public void dismiss() {
        if (isShow() || this.mIsMoving) {
            int i = AnonymousClass3.$SwitchMap$com$tyb$smart$view$XCSlideView$Positon[this.mPositon.ordinal()];
            if (i == 1) {
                startScroll(getScrollX(), this.mMenuWidth, this.mDuration);
            } else if (i == 2) {
                startScroll(getScrollX(), -this.mMenuWidth, this.mDuration);
            }
            switchMaskView(false);
            this.mShow = false;
        }
    }

    public boolean isShow() {
        return this.mShow;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
            this.mIsMoving = true;
        } else {
            this.mIsMoving = false;
        }
        super.computeScroll();
    }

    public void startScroll(int i, int i2, int i3) {
        this.mIsMoving = true;
        this.mScroller.startScroll(i, 0, i2, 0, i3);
        invalidate();
    }
}
