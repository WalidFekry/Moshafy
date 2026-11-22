package com.appwalied.quran.image_quran.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomableImageView extends AppCompatImageView implements View.OnTouchListener {

    public interface OnSingleTapListener {
        void onSingleTap();
    }

    private static final float MIN_SCALE = 1.0f;
    private static final float MAX_SCALE = 4.0f;

    private final Matrix matrix = new Matrix();
    private final float[] matrixValues = new float[9];

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private final PointF lastTouchPoint = new PointF();
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private OnSingleTapListener singleTapListener;

    private float currentScale = 1.0f;
    private float origWidth, origHeight;
    private boolean isImageSet = false;

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

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());

        setOnTouchListener(this);
    }

    public void setOnSingleTapListener(OnSingleTapListener listener) {
        this.singleTapListener = listener;
    }

    public void resetZoom() {
        currentScale = 1.0f;
        mode = NONE;
        fitImageToView();
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    public boolean isZoomed() {
        return currentScale > 1.05f;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        isImageSet = (drawable != null);
        fitImageToView();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed || !isImageSet) {
            fitImageToView();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastTouchPoint.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG && currentScale > MIN_SCALE + 0.02f) {
                    float deltaX = event.getX() - lastTouchPoint.x;
                    float deltaY = event.getY() - lastTouchPoint.y;

                    matrix.postTranslate(deltaX, deltaY);
                    limitDragBounds();
                    setImageMatrix(matrix);
                    lastTouchPoint.set(event.getX(), event.getY());

                    boolean canScrollHorizontally = checkCanScrollHorizontally(deltaX);
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(!canScrollHorizontally);
                    }
                } else if (getParent() != null && currentScale <= MIN_SCALE + 0.02f) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() <= 2) {
                    mode = DRAG;
                    int index = event.getActionIndex() == 0 ? 1 : 0;
                    lastTouchPoint.set(event.getX(index), event.getY(index));
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mode = NONE;
                if (currentScale < MIN_SCALE + 0.05f) {
                    resetZoom();
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        return true;
    }

    private boolean checkCanScrollHorizontally(float deltaX) {
        matrix.getValues(matrixValues);
        float transX = matrixValues[Matrix.MTRANS_X];
        float viewWidth = getWidth();
        float contentWidth = origWidth * currentScale;

        if (contentWidth <= viewWidth) {
            return true;
        }

        if (deltaX < 0 && transX <= viewWidth - contentWidth + 5f) {
            return true; // Reached right boundary
        }
        if (deltaX > 0 && transX >= -5f) {
            return true; // Reached left boundary
        }
        return false;
    }

    private void limitDragBounds() {
        matrix.getValues(matrixValues);
        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float contentWidth = origWidth * currentScale;
        float contentHeight = origHeight * currentScale;

        float fixTransX = getFixTranslation(transX, viewWidth, contentWidth);
        float fixTransY = getFixTranslation(transY, viewHeight, contentHeight);

        if (fixTransX != 0 || fixTransY != 0) {
            matrix.postTranslate(fixTransX, fixTransY);
        }
    }

    private float getFixTranslation(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = (viewSize - contentSize) / 2f;
            maxTrans = (viewSize - contentSize) / 2f;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans) return minTrans - trans;
        if (trans > maxTrans) return maxTrans - trans;
        return 0;
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

        float scaleX = (float) viewWidth / (float) drawableWidth;
        float scaleY = (float) viewHeight / (float) drawableHeight;
        float scale = Math.min(scaleX, scaleY);

        matrix.reset();
        matrix.setScale(scale, scale);

        float redundantYSpace = (viewHeight - (scale * drawableHeight)) / 2f;
        float redundantXSpace = (viewWidth - (scale * drawableWidth)) / 2f;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = drawableWidth * scale;
        origHeight = drawableHeight * scale;
        currentScale = 1.0f;

        setImageMatrix(matrix);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float origScale = currentScale;
            currentScale *= scaleFactor;

            if (currentScale > MAX_SCALE) {
                currentScale = MAX_SCALE;
                scaleFactor = MAX_SCALE / origScale;
            } else if (currentScale < MIN_SCALE) {
                currentScale = MIN_SCALE;
                scaleFactor = MIN_SCALE / origScale;
            }

            if (scaleFactor != 1.0f) {
                matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                limitDragBounds();
                setImageMatrix(matrix);
            }

            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(currentScale > MIN_SCALE + 0.02f);
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
            if (currentScale > MIN_SCALE + 0.05f) {
                resetZoom();
            } else {
                currentScale = 2.2f;
                matrix.postScale(2.2f, 2.2f, e.getX(), e.getY());
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
