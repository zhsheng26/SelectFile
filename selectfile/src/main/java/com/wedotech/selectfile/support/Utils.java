package com.wedotech.selectfile.support;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.android.internal.util.Predicate;
import com.wedotech.selectfile.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Utils {

    public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean contains(String[] types, String path) {
        for (String string : types) {
            if (path.endsWith(string)) return true;
        }
        return false;
    }

    public boolean isImage(String path) {
        String[] types = {"jpg", "png", "gif"};
        return Utils.contains(types, path);
    }

    public static final int INVALID_COLOR = -1;
    public static int colorAccent = INVALID_COLOR;

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static void highlightText(@NonNull Context context, @NonNull TextView textView,
                                     String originalText, String constraint, @ColorInt int defColor) {
        if (originalText == null) originalText = "";
        if (constraint == null) constraint = "";
        int i = originalText.toLowerCase(Locale.getDefault()).indexOf(constraint.toLowerCase(Locale.getDefault()));
        if (i != -1) {
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(originalText);
            spanText.setSpan(new ForegroundColorSpan(fetchAccentColor(context, defColor)), i,
                    i + constraint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new StyleSpan(Typeface.BOLD), i,
                    i + constraint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            textView.setText(originalText, TextView.BufferType.NORMAL);
        }
    }


    public static void resetAccentColor() {
        colorAccent = INVALID_COLOR;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int fetchAccentColor(Context context, @ColorInt int defColor) {
        if (colorAccent == INVALID_COLOR) {
            if (hasLollipop()) {
                TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent});
                colorAccent = androidAttr.getColor(0, defColor);
                androidAttr.recycle();
            } else {
                colorAccent = defColor;
            }
        }
        return colorAccent;
    }


    @Deprecated
    public static int getOrientation(RecyclerView recyclerView) {
        return getOrientation(recyclerView.getLayoutManager());
    }

    public static int getOrientation(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return OrientationHelper.HORIZONTAL;
    }

    public static int findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        }
    }

    public static int findLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
    }

    public static Activity scanForActivity(Context context) {
        if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    public static void checkArg(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void checkNotNull(Object object, String msg) {
        if (object == null) {
            throw new NullPointerException(msg);
        }
    }

    public static Bitmap asBitmap(Drawable drawable, int minWidth, int minHeight) {
        final Rect tmpRect = new Rect();
        drawable.copyBounds(tmpRect);
        if (tmpRect.isEmpty()) {
            tmpRect.set(0, 0, Math.max(minWidth, drawable.getIntrinsicWidth()), Math.max(minHeight, drawable.getIntrinsicHeight()));
            drawable.setBounds(tmpRect);
        }
        Bitmap bitmap = Bitmap.createBitmap(tmpRect.width(), tmpRect.height(), Bitmap.Config.ARGB_8888);
        drawable.draw(new Canvas(bitmap));
        return bitmap;
    }

    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final String TAG = "scissors.Utils";

    public static Future<Void> flushToFile(final Bitmap bitmap,
                                           final Bitmap.CompressFormat format,
                                           final int quality,
                                           final File file) {

        return EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;

                try {
                    file.getParentFile().mkdirs();
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(format, quality, outputStream);
                    outputStream.flush();
                } catch (final Throwable throwable) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "Error attempting to save bitmap.", throwable);
                    }
                } finally {
                    closeQuietly(outputStream);
                }
            }
        }, null);
    }

    public static Future<Void> flushToStream(final Bitmap bitmap,
                                             final Bitmap.CompressFormat format,
                                             final int quality,
                                             final OutputStream outputStream,
                                             final boolean closeWhenDone) {

        return EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap.compress(format, quality, outputStream);
                    outputStream.flush();
                } catch (final Throwable throwable) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "Error attempting to save bitmap.", throwable);
                    }
                } finally {
                    if (closeWhenDone) {
                        closeQuietly(outputStream);
                    }
                }
            }
        }, null);
    }

    private static void closeQuietly(@Nullable OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error attempting to close stream.", e);
        }
    }
}
