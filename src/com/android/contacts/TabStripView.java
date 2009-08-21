/*
 * Copyright (C) 2009 The Android Open Source Project
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

package com.android.contacts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/** Extension of LinearLayout that takes care of drawing bottom strips over the tab children. */
public class TabStripView extends LinearLayout {

    private Drawable mBottomLeftStrip;
    private Drawable mBottomRightStrip;
    private int mSelectedTabIndex;

    public TabStripView(Context context) {
        this(context, null);
    }

    public TabStripView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBottomLeftStrip = mContext.getResources().getDrawable(
                R.drawable.tab_bottom);
        mBottomRightStrip = mContext.getResources().getDrawable(
                R.drawable.tab_bottom);
    }

    public void setSelected(int index, boolean selected) {
        mSelectedTabIndex = index;
        getChildAt(index).setSelected(selected);
    }

    @Override
    public void childDrawableStateChanged(View child) {
        if (child == getChildAt(mSelectedTabIndex)) {
            // To make sure that the bottom strip is redrawn
            invalidate();
        }
        super.childDrawableStateChanged(child);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        View selectedChild = getChildAt(mSelectedTabIndex);

        mBottomRightStrip.setState(selectedChild.getDrawableState());
        mBottomLeftStrip.setState(selectedChild.getDrawableState());

        Rect selBounds = new Rect(); // Bounds of the selected tab indicator
        selBounds.left = selectedChild.getLeft() - getScrollX();
        selBounds.right = selectedChild.getRight() - getScrollX();
        final int myHeight = getHeight();
        mBottomLeftStrip.setBounds(
                Math.min(0, selBounds.left
                             - mBottomLeftStrip.getIntrinsicWidth()),
                myHeight - mBottomLeftStrip.getIntrinsicHeight(),
                selBounds.left,
                myHeight);
        mBottomRightStrip.setBounds(
                selBounds.right,
                myHeight - mBottomRightStrip.getIntrinsicHeight(),
                Math.max(getWidth(),
                        selBounds.right + mBottomRightStrip.getIntrinsicWidth()),
                myHeight);

        mBottomLeftStrip.draw(canvas);
        mBottomRightStrip.draw(canvas);
    }

}
