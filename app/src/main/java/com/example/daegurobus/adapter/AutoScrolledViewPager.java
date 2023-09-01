package com.example.daegurobus.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class AutoScrolledViewPager extends ViewPager {
    public static final int SCROLL_DURATION_TIME = 1000;
    public static final int IMAGE_ADAPTER_LOOP_COUNT = 1000;

    public int AUTO_SCROLL_DELAY_TIME = 5000;

    private Subscription subscription;
    private ScrollerCustomDuration scrollerCustomDuration = null;

    private int currentPosition;

    public AutoScrolledViewPager(@NonNull Context context) {
        super(context);
        postInitViewPager();
    }

    public AutoScrolledViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    public void setAutoScrollDelayTime(int autoScrollDelayTime) {
        AUTO_SCROLL_DELAY_TIME = autoScrollDelayTime;
    }

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            scrollerCustomDuration = new ScrollerCustomDuration(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, scrollerCustomDuration);
        } catch (Exception e) {
        }
    }

    public void setScrollDurationFactor(int scrollFactor) {
        scrollerCustomDuration.setScrollDurationFactor(scrollFactor);
    }

    public void autoScrollStart() {
        if (subscription != null) {
            subscription.unsubscribe();
        }

        subscription = Observable.timer(AUTO_SCROLL_DELAY_TIME, TimeUnit.MILLISECONDS)    // 5초 뒤에 스크롤 시작.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> setCurrentItem(getCurrentItem() + 1));
    }

    public void autoScrollStop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}