package com.example.FingerPaint;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sanea on 12.05.15.
 */
public class DrawingView extends View {
    //
    private Bitmap mainBitmap;

    //The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint (to describe the colors and styles for the drawing).
    private Canvas mCanvas;

    //The Path class encapsulates compound (multiple contour) geometric paths consisting of straight line segments, quadratic curves, and cubic curves. It can be drawn with canvas.drawPath(path, paint), either filled or stroked (based on the paint's Style), or it can be used for clipping or to draw text on a path.
    private Path mainPath;
    private Paint mainBitmapPaint;

    private Paint mainPaint;

    public static final String _draw = "_draw";

    public DrawingView(Context ctx, Paint paint) {
        super(ctx);
        mainPaint = paint;
        mainPath = new Path();
        mainBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    //This is called during layout when the size of this view has changed. If you were just added to the view hierarchy, you're called with the old values of 0.
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(_draw, String.format("onSizeChanged() mX=%f; mT=%f;", mX, mY));
        //Returns a mutable bitmap with the specified width and height.
        mainBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //Construct a canvas with the specified bitmap to draw into.
        mCanvas = new Canvas(mainBitmap);
    }

    @Override
    //Implement this to do your drawing.
    //the canvas on which the background will be drawn
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //(Bitmap bitmap, float left, float top, Paint paint)
        //Draw the specified bitmap, with its top/left corner at (x,y), using the specified paint, transformed by the current matrix.
        //Note: if the paint contains a maskfilter that generates a mask which extends beyond the bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be drawn as if it were in a Shader with CLAMP mode. Thus the color outside of the original width/height will be the edge color replicated.
        //If the bitmap and canvas have different densities, this function will take care of automatically scaling the bitmap to draw at the same density as the canvas.
        canvas.drawBitmap(mainBitmap, 0, 0, mainBitmapPaint);

        //Draw the specified path using the specified paint. The path will be filled or framed based on the Style in the paint.
        canvas.drawPath(mainPath, mainPaint);

        //Log.d(_draw, String.format("onDraw() mX=%f; mT=%f;", mX, mY));
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //Clear any lines and curves from the path, making it empty.
        mainPath.reset();

        //Set the beginning of the next contour to the point (x,y).
        mainPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            //Add a quadratic bezier from the last point, approaching control point (x1,y1), and ending at (x2,y2). If no moveTo() call has been made for this contour, the first point is automatically set to (0,0).
            mainPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        //Add a line from the last point to the specified point (x,y).
        mainPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mainPath, mainPaint);
        // kill this so we don't double draw
        mainPath.reset();
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
