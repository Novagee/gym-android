package com.jianyue.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;

import com.foound.widget.AmazingListView;

public class ArkListView extends AmazingListView {

    private boolean mIsFastScrollEnabled = true;

    private IndexScroller mScroller = null;

    private GestureDetector mGestureDetector = null;

    public ArkListView(Context context) {
        super(context);
    }

    public ArkListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArkListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFastScrollEnabled() {
        return mIsFastScrollEnabled;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        // mIsFastScrollEnabled = enabled;
        if (mIsFastScrollEnabled) {
            if (mScroller == null) {
                mScroller = new IndexScroller(getContext(), this);
                mScroller.show();
            }

        } else {
            if (mScroller != null) {
                // mScroller.hide();
                // mScroller = null;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Overlay index bar
        if (mScroller != null)
            mScroller.draw(canvas);
    }

    public void hideScroller() {
        if (mScroller != null)
            mScroller.hide();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int SWIPE_THRESHOLD = 100;
        final int SWIPE_VELOCITY_THRESHOLD = 100;

        // Intercept ListView's touch event
        if (mScroller != null && mScroller.onTouchEvent(ev))
            return true;

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(),
                    new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                float velocityY) {
                            // If fling happens, index bar shows
                            if (mScroller != null) {
                                mScroller.show();
                            }

                            Log.d("log_tag", "getFirstVisiblePosition : "
                                    + getFirstVisiblePosition());
                            if (getFirstVisiblePosition() == 0
                                    || (getFirstVisiblePosition() > 0/* && CategoryFragment
                                            .isSeachWindowOpen()*/)) {
                                boolean result = false;
                                try {
                                    float diffY = e2.getY() - e1.getY();
                                    float diffX = e2.getX() - e1.getX();
                                    if (Math.abs(diffX) > Math.abs(diffY)) {
                                        if (Math.abs(diffX) > SWIPE_THRESHOLD
                                                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                            if (diffX > 0) {
                                                // onSwipeRight();
                                            } else {
                                                // onSwipeLeft();
                                            }
                                        }
                                    } else {
                                        if (Math.abs(diffY) > SWIPE_THRESHOLD
                                                && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                                            if (diffY > 0) {
                                                try {
                                                   /* CategoryFragment.showSearchWindow();*/
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                // onSwipeBottom();
                                            } else {
                                                try {
                                                    /*CategoryFragment.hideSearchWindow();*/
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                // onSwipeTop();
                                            }
                                        }
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                return result;
                            } else {
                            	return false;
                                /*if (!CategoryFragment.isSeachWindowOpen()) {
                                    return super.onFling(e1, e2, velocityX, velocityY);
                                } else {
                                    return false;
                                }*/

                            }

                        }

                    });
        }
        mGestureDetector.onTouchEvent(ev);

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null)
            mScroller.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }

}
