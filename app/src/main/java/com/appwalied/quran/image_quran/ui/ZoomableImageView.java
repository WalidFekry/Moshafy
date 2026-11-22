package com.appwalied.quran.image_quran.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView implements View.OnTouchListener {

    public interface OnSingleTapListener {
        void onSingleTap();
    }

    private static final float MIN_SCALE = 1.0f;
    private static final float MAX_SCALE = 3.5f;

    private final Matrix matrix = new Matrix();
    private final Matrix savedMatrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private final PointF start = new PointF();
    private final PointF mid = new PointF();
    private float[] m;

    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private OnSingleTapListener singleTapListener;

    private float currentScale = 1.0f;
    private float origWidth, origHeight;

    public ZoomableImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        super.setClickable(true);
        setScaleType(ScaleType.MATRIX);

        m = new float[9];
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());

        setOnTouchListener(this);
    }

    public void setOnSingleTapListener(OnSingleTapListener listener) {
        this.singleTapListener = listener;
    }

    public void resetZoom() {
        currentScale = 1.0f;
        matrix.reset();
        fitImageToView();
        setImageMatrix(matrix);
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    public boolean isZoomed() {
        return currentScale > 1.05f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        matrix.getValues(m);
        float x = m[Matrix.MTRANS_X];
        float y = m[Matrix.MTRANS_Y];

        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    if (currentScale > MIN_SCALE) {
                        float deltaX = curr.x - start.x;
                        float deltaY = curr.y - start.y;

                        float viewWidth = getWidth();
                        float viewHeight = getHeight();

                        float imageWidth = origWidth * currentScale;
                        float imageHeight = origHeight * currentScale;

                        if (imageWidth <= viewWidth) {
                            deltaX = 0;
                        }
                        if (imageHeight <= viewHeight) {
                            deltaY = 0;
                        }

                        matrix.set(savedMatrix);
                        matrix.postTranslate(deltaX, deltaY);
                        limitDragBounds();

                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                if (currentScale <= MIN_SCALE + 0.05f) {
                    resetZoom();
                }
                break;
        }

        setImageMatrix(matrix);
        return true;
    }

    private void limitDragBounds() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float imageWidth = origWidth * currentScale;
        float imageHeight = origHeight * currentScale;

        float fixTransX = getFixTrans(transX, viewWidth, imageWidth);
        float fixTransY = getFixTrans(transY, viewHeight, imageHeight);

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans) return minTrans - trans;
        if (trans > maxTrans) return maxTrans - trans;
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        fitImageToView();
    }

    private void fitImageToView() {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            return;
        }

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if (viewWidth == 0 || viewHeight == 0) {
            return;
        }

        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        float scale;
        float scaleX = (float) viewWidth / (float) drawableWidth;
        float scaleY = (float) viewHeight / (float) drawableHeight;
        scale = Math.min(scaleX, scaleY);

        matrix.setScale(scale, scale);

        float redundantYSpace = (float) viewHeight - (scale * (float) drawableHeight);
        float redundantXSpace = (float) viewWidth - (scale * (float) drawableWidth);

        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = viewWidth - 2 * redundantXSpace;
        origHeight = viewHeight - 2 * redundantYSpace;

        setImageMatrix(matrix);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = currentScale;
            currentScale *= mScaleFactor;

            if (currentScale > MAX_SCALE) {
                currentScale = MAX_SCALE;
                mScaleFactor = MAX_SCALE / origScale;
            } else if (currentScale < MIN_SCALE) {
                currentScale = MIN_SCALE;
                mScaleFactor = MIN_SCALE / origScale;
            }

            if (origWidth * currentScale <= getWidth() || origHeight * currentScale <= getHeight()) {
                matrix.postScale(mScaleFactor, mScaleFactor, getWidth() / 2f, getHeight() / 2f);
            } else {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
            }

            limitDragBounds();

            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(currentScale > MIN_SCALE);
            }
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (singleTapListener != null) {
                singleTapListener.onSingleTap();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (currentScale > MIN_SCALE) {
                resetZoom();
            } else {
                currentScale = 2.5f;
                matrix.postScale(2.5f, 2.5f, e.getX(), e.getY());
                limitDragBounds();
                setImageMatrix(matrix);
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            return true;
        }
    }
}
