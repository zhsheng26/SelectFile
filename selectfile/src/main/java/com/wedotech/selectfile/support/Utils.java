package com.wedotech.selectfile.support;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.android.internal.util.Predicate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

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

    /**
     * API 21
     *
     * @see Build.VERSION_CODES#LOLLIPOP
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * API 16
     *
     * @see Build.VERSION_CODES#JELLY_BEAN
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Sets a spannable text with the accent color (if available) into the provided TextView.
     * <p>Internally calls {@link #fetchAccentColor(Context, int)}.</p>
     *
     * @param context      context
     * @param textView     the TextView to transform
     * @param originalText the original text which the transformation is applied to
     * @param constraint   the text to highlight
     * @param defColor     the default color in case accentColor is not found
     * @see #fetchAccentColor(Context, int)
     */
    //TODO: Deprecate defColor?
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

    /**
     * Reset the internal accent color to {@link #INVALID_COLOR}, to give the possibility
     * to re-fetch it at runtime, since once it is fetched it cannot be changed.
     */
    public static void resetAccentColor() {
        colorAccent = INVALID_COLOR;
    }

    /**
     * Optimized method to fetch the accent color on devices with at least Lollipop.
     * <p>If accent color has been already fetched it is simply returned.</p>
     *
     * @param context  context
     * @param defColor value to return if the accentColor cannot be found
     */
    //TODO: Deprecate defColor and use R.attr.colorAccent?
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

    /**
     * Finds the layout orientation of the RecyclerView.
     *
     * @param recyclerView the RV instance
     * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
     * @deprecated Use {@link #getOrientation(RecyclerView.LayoutManager)} instead
     */
    @Deprecated
    public static int getOrientation(RecyclerView recyclerView) {
        return getOrientation(recyclerView.getLayoutManager());
    }

    /**
     * Finds the layout orientation of the RecyclerView, no matter which LayoutManager is in use.
     *
     * @param layoutManager the LayoutManager instance in use by the RV
     * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
     */
    public static int getOrientation(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return OrientationHelper.HORIZONTAL;
    }

    /**
     * Helper method to find the adapter position of the First completely visible view [for each
     * span], no matter which Layout is.
     *
     * @param layoutManager the layout manager in use
     * @return the adapter position of the first fully visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager)
     * @since 5.0.0-b8
     */
    public static int findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        }
    }

    /**
     * Helper method to find the adapter position of the Last completely visible view [for each
     * span], no matter which Layout is.
     *
     * @param layoutManager the layout manager in use
     * @return the adapter position of the last fully visible item or {@code RecyclerView.NO_POSITION}
     * if there aren't any visible items.
     * @see #findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager)
     * @since 5.0.0-b8
     */
    public static int findLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null)[0];
        } else {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
    }

    /**
     * Resolves bug #161. Necessary when {@code theme} attribute is used in the layout.
     * Used by {@code FlexibleAdapter.getStickySectionHeadersHolder()} method.
     */
    public static Activity scanForActivity(Context context) {
        if (context instanceof Activity)
            return (Activity) context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) context).getBaseContext());

        return null;
    }
}
