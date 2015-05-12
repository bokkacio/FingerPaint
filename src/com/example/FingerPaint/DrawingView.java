package com.example.FingerPaint;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sanea on 12.05.15.
 */
public class DrawingView extends View {
    //
    private Bitmap mBitmap;

    //The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint (to describe the colors and styles for the drawing).
    private Canvas mCanvas;

    //The Path class encapsulates compound (multiple contour) geometric paths consisting of straight line segments, quadratic curves, and cubic curves. It can be drawn with canvas.drawPath(path, paint), either filled or stroked (based on the paint's Style), or it can be used for clipping or to draw text on a path.
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint circlePaint;
    private Path circlePath;
    private Paint mPaint;

    public DrawingView(Context ctx, Paint paint) {
        super(ctx);
        mPaint = paint;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();

        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
    }

    @Override
    //This is called during layout when the size of this view has changed. If you were just added to the view hierarchy, you're called with the old values of 0.
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Returns a mutable bitmap with the specified width and height.
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //Construct a canvas with the specified bitmap to draw into.
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    //Implement this to do your drawing.
    //the canvas on which the background will be drawn
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw the specified bitmap, scaling/translating automatically to fill the destination rectangle.
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        //Draw the specified path using the specified paint.
        canvas.drawPath(mPath, mPaint);

        canvas.drawPath(circlePath, circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            //Clear any lines and curves from the path, making it empty.
            circlePath.reset();

            //Add a closed circle contour to the path (float x, float y, float radius, Path.Direction dir)
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        //Add a line from the last point to the specified point (x,y).
        mPath.lineTo(mX, mY);
        circlePath.reset();

        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    //Implement this method to handle touch screen motion events.
    //If this method is used to detect click actions, it is recommended that the actions be performed by implementing and calling performClick(). This will ensure consistent system behavior
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}
