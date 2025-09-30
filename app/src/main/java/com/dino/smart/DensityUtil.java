package com.dino.smart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

public class DensityUtil {

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeight(Context context) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NumberFormatException {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getScreenHeightWithoutTitlebar(Context context) {
        try {
            return (getScreenWidthAndHeight(context)[1] - getStatusBarHeight(context)) - dip2px(context, 48.0f);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[] getScreenWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    public static void addOnSoftKeyBoardVisibleListener(final Activity activity, final ScrollView scrollView) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.tyb.smart.DensityUtil.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                boolean z = ((double) (rect.bottom - rect.top)) / ((double) decorView.getHeight()) < 0.8d;
                System.out.println("===监听" + z);
                if (z) {
                    new Handler().postDelayed(new Runnable() { // from class: com.tyb.smart.DensityUtil.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            scrollView.scrollTo(0, DensityUtil.dip2px(activity, 167.0f));
                        }
                    }, 50L);
                }
            }
        });
    }

    public static String getEllipsisedText(TextView textView) {
        try {
            String string = textView.getText().toString();
            int lineCount = textView.getLineCount();
            int width = textView.getWidth();
            int length = string.length();
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            TextPaint paint = textView.getPaint();
            StringBuffer stringBuffer = new StringBuffer();
            int i2 = 0;
            int beginIndex = 0;
            while (true) {
                if (i2 >= lineCount - 1) {
                    break;
                }
                int iBreakText = paint.breakText(string, beginIndex, length, true, width, null);
                if (iBreakText >= length - beginIndex) {
                    stringBuffer.append(string.substring(beginIndex));
                    break;
                }
                int i4 = beginIndex + iBreakText;
                int i5 = i4 - 1;
                int iLastIndexOf = string.lastIndexOf(10, i5);
                int n3=0;
                if (iLastIndexOf >= 0 && iLastIndexOf < i4) {
                    n3 = iLastIndexOf + 1;
                    stringBuffer.append(string.substring(beginIndex, n3));
                } else {
                    int iLastIndexOf2 = string.lastIndexOf(32, i5);
                    if (iLastIndexOf2 >= beginIndex) {
                        n3 = iLastIndexOf2 + 1;
                        stringBuffer.append(string.substring(beginIndex, n3));
                    } else {
                        stringBuffer.append(string.substring(beginIndex, iBreakText));
                        beginIndex = i4;
                        i2++;
                    }
                }
                beginIndex += n3;
                i2++;
            }
            if (beginIndex < length) {
                stringBuffer.append(TextUtils.ellipsize(string.subSequence(beginIndex, length), paint, width, truncateAt));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
