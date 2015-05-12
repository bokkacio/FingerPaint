package com.example.FingerPaint;
import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyActivity extends Activity {

    private static final int CLEAR_AREA = 1;

    private DrawingView dv;

    //The Paint class holds the style and color information about how to draw geometries, text and bitmaps.
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPaint();
        dv = new DrawingView(this, mPaint);

        //Set the activity content to an explicit view. This view is placed directly into the activity's view hierarchy. It can itself be a complex view hierarchy. When calling this method, the layout parameters of the specified view are ignored. Both the width and the height of the view are set by default to MATCH_PARENT. To use your own layout parameters, invoke setContentView(android.view.View, android.view.ViewGroup.LayoutParams) instead.
        setContentView(dv);
    }

    private void initPaint(){
        mPaint = new Paint();

        //Helper for setFlags(), setting or clearing the ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn, but is has no impact on the interior of the shape. See setDither() and setFilterBitmap() to affect how colors are treated.
        mPaint.setAntiAlias(true);

        //Helper for setFlags(), setting or clearing the DITHER_FLAG bit Dithering affects how colors that are higher precision than the device are down-sampled. No dithering is generally faster, but higher precision colors are just truncated down (e.g. 8888 -> 565). Dithering tries to distribute the error inherent in this process, to reduce the visual artifacts.
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);

        //Set the paint's style, used for controlling how primitives' geometries are interpreted (except for drawBitmap, which always assumes Fill).
        mPaint.setStyle(Paint.Style.STROKE);

        //Set the paint's Join. set the paint's Join, used whenever the paint's style is Stroke or StrokeAndFill.
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        //Set the paint's Cap. set the paint's line cap style, used whenever the paint's style is Stroke or StrokeAndFill.
        //The Cap specifies the treatment for the beginning and ending of stroked lines and paths. The default is BUTT.
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        //Set the width for stroking. Pass 0 to stroke in hairline mode. Hairlines always draws a single pixel independent of the canva's matrix.
        mPaint.setStrokeWidth(12);
    }

    @Override
    //Initialize the contents of the Activity's standard options menu. You should place your menu items in to menu.
    //The default implementation populates the menu with standard system menu items. These are placed in the CATEGORY_SYSTEM group so that they will be correctly ordered with application-defined menu items. Deriving classes should always call through to the base implementation.
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add a new item to the menu. This item displays the given title for its label.
        // groupId	The group identifier that this item should be part of. This can be used to define groups of items for batch state changes. Normally use NONE if an item should not be in a group.
        // itemId	Unique item ID. Use NONE if you do not need a unique ID.
        // order	The order for the item. Use NONE if you do not care about the order. See getOrder().
        // title	The text to display for the item.
        menu.add(0, CLEAR_AREA, 0, "Clear area");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //This hook is called whenever an item in your options menu is selected. The default implementation simply returns false to have the normal processing happen (calling the item's Runnable or sending a message to its Handler as appropriate). You can use this method for any items for which you would like to do processing without those other facilities.
    //Derived classes should call through to the base class for it to perform the default menu handling.
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case CLEAR_AREA:
            {
                Toast.makeText(this, "CLEAR_AREA", Toast.LENGTH_SHORT).show();
                dv = new DrawingView(this, mPaint);
                setContentView(dv);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
