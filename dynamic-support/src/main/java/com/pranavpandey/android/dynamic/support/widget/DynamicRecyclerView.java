/*
 * Copyright 2018 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicColorType;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicScrollUtils;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * A RecyclerView to apply color filter according to the
 * supplied {@link R.attr#ads_colorType}.
 */
public class DynamicRecyclerView extends RecyclerView implements DynamicScrollableWidget {

    /**
     * Color type applied to this view.
     *
     * @see DynamicColorType
     */
    private @DynamicColorType int mColorType;

    /**
     * Scroll bar color type applied to this view.
     *
     * @see DynamicColorType
     */
    private @DynamicColorType int mScrollBarColorType;

    /**
     * Background color type for this view so that it will remain in
     * contrast with this color type.
     */
    private @DynamicColorType int mContrastWithColorType;

    /**
     * Color applied to this view.
     */
    private @ColorInt int mColor;

    /**
     * Scroll bar color applied to this view.
     */
    private @ColorInt int mScrollBarColor;

    /**
     * Background color for this view so that it will remain in
     * contrast with this color.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * {@code true} if this view will change its color according
     * to the background. It was introduced to provide better legibility for
     * colored texts and to avoid dark text on dark background like situations.
     *
     * <p>If this boolean is set then, it will check for the contrast color and
     * do color calculations according to that color so that this text view will
     * always be visible on that background. If no contrast color is found then,
     * it will take default background color.</p>
     *
     * @see #mContrastWithColor
     */
    private boolean mBackgroundAware;

    public DynamicRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicRecyclerView(@NonNull Context context,
                               @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DynamicTheme);

        try {
            mColorType = a.getInt(R.styleable.DynamicTheme_ads_colorType,
                    WidgetDefaults.ADS_COLOR_EDGE_EFFECT);
            mScrollBarColorType = a.getInt(R.styleable.DynamicTheme_ads_scrollBarColorType,
                    WidgetDefaults.ADS_COLOR_SCROLL_BAR);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicTheme_ads_contrastWithColorType,
                    DynamicColorType.BACKGROUND);
            mColor = a.getColor(R.styleable.DynamicTheme_ads_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mScrollBarColor = a.getColor(R.styleable.DynamicTheme_ads_scrollBarColor,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mContrastWithColor = a.getColor(R.styleable.DynamicTheme_ads_contrastWithColor,
                    WidgetDefaults.getDefaultContrastWithColor(getContext()));
            mBackgroundAware = a.getBoolean(
                    R.styleable.DynamicTheme_ads_backgroundAware,
                    WidgetDefaults.ADS_BACKGROUND_AWARE);
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() {
        if (mColorType != DynamicColorType.NONE
                && mColorType != DynamicColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().getColorFromType(mColorType);
        }

        if (mScrollBarColorType != DynamicColorType.NONE
                && mScrollBarColorType != DynamicColorType.CUSTOM) {
            mScrollBarColor = DynamicTheme.getInstance()
                    .getColorFromType(mScrollBarColorType);
        }

        if (mContrastWithColorType != DynamicColorType.NONE
                && mContrastWithColorType != DynamicColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .getColorFromType(mContrastWithColorType);
        }

        setColor(true);
    }

    @Override
    public @DynamicColorType int getColorType() {
        return mColorType;
    }

    @Override
    public void setColorType(@DynamicColorType int colorType) {
        this.mColorType = colorType;

        initialize();
    }

    @Override
    public @DynamicColorType int getScrollBarColorType() {
        return mScrollBarColorType;
    }

    @Override
    public void setScrollBarColorType(@DynamicColorType int scrollBarColorType) {
        this.mScrollBarColorType = scrollBarColorType;

        initialize();
    }

    @Override
    public @DynamicColorType int getContrastWithColorType() {
        return mContrastWithColorType;
    }

    @Override
    public void setContrastWithColorType(@DynamicColorType int contrastWithColorType) {
        this.mContrastWithColorType = contrastWithColorType;

        initialize();
    }

    @Override
    public @ColorInt int getColor() {
        return mColor;
    }

    @Override
    public void setColor(@ColorInt int color) {
        this.mColorType = DynamicColorType.CUSTOM;
        this.mColor = color;

        setColor(false);
    }

    @Override
    public @ColorInt int getScrollBarColor() {
        return mScrollBarColor;
    }

    @Override
    public void setScrollBarColor(@ColorInt int scrollBarColor) {
        this.mScrollBarColorType = DynamicColorType.CUSTOM;
        this.mScrollBarColor = scrollBarColor;

        setScrollBarColor();
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = DynamicColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setColor(true);
    }

    @Override
    public boolean isBackgroundAware() {
        return mBackgroundAware;
    }

    @Override
    public void setBackgroundAware(boolean backgroundAware) {
        this.mBackgroundAware = backgroundAware;

        setColor(true);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        setColor(true);
    }

    @Override
    public void setColor() {
        if (mColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (mBackgroundAware && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            DynamicScrollUtils.setEdgeEffectColor(this, mColor);
        }
    }

    @Override
    public void setScrollBarColor() {
        if (mScrollBarColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (mBackgroundAware && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mScrollBarColor = DynamicColorUtils.getContrastColor(
                        mScrollBarColor, mContrastWithColor);
            }

            DynamicScrollUtils.setScrollBarColor(this, mScrollBarColor);
        }
    }

    @Override
    public void setColor(boolean setScrollBarColor) {
        setColor();

        if (setScrollBarColor) {
            setScrollBarColor();
        }
    }
}
